package com.example.tyriahub.overlay

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.IBinder
import android.view.Display
import android.view.Gravity
import android.view.KeyEvent
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.MainActivity
import com.example.tyriahub.api.SecurePreferences
import com.example.tyriahub.haptics.HapticsManager
import com.example.tyriahub.ime.FastKeyInjector
import com.example.tyriahub.ime.TyriaHubInputMethodService
import com.example.tyriahub.ui.MainViewModel
import com.example.tyriahub.ui.components.TyriaHubAppContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class TyriaOverlayService : Service(), LifecycleOwner, SavedStateRegistryOwner, ViewModelStoreOwner {

    private var currentWindowManager: WindowManager? = null
    private var overlayComposeView: ComposeView? = null
    private var currentDisplayId: Int = -1
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    private val appViewModelStore = ViewModelStore()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private lateinit var haptics: HapticsManager

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry
    override val viewModelStore: ViewModelStore get() = appViewModelStore

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        isOverlayActive = true
        haptics = HapticsManager(this)
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        startForegroundNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val targetDisplay = intent?.getIntExtra(EXTRA_TARGET_DISPLAY_ID, -1) ?: -1
        attachOverlayToDisplay(targetDisplay)
        return START_NOT_STICKY
    }

    private fun startForegroundNotification() {
        val channelId = "tyria_hub_overlay"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Tyria Hub Gaming Overlay",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Non-focusable touch deck running over bottom screen"
            }
            val nm = getSystemService(NotificationManager::class.java)
            nm?.createNotificationChannel(channel)
        }

        val notification: Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, channelId)
                .setContentTitle("Tyria Hub Touch Overlay Active")
                .setContentText("Non-focusable gaming deck active for DroidDeck")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build()
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
                .setContentTitle("Tyria Hub Touch Overlay Active")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build()
        }

        startForeground(1001, notification)
    }

    fun attachOverlayToDisplay(preferredDisplayId: Int) {
        // Remove existing view if already attached
        overlayComposeView?.let { view ->
            try {
                currentWindowManager?.removeView(view)
            } catch (_: Exception) {}
            overlayComposeView = null
        }

        val allDisplays = HandheldDisplayManager.getAllDisplays(this)

        // Prioritize:
        // 1. Explicit display ID requested by user or caller
        // 2. Saved display preference in SecurePreferences
        // 3. Auto-detected bottom screen (AYN Thor 3.92" 1080x1240 screen)
        // 4. Secondary display or fallback
        val targetDisplay: Display = if (preferredDisplayId != -1) {
            allDisplays.find { it.displayId == preferredDisplayId }
                ?: HandheldDisplayManager.findBottomDisplay(this)
        } else {
            val savedPref = SecurePreferences(this).overlayDisplayId
            if (savedPref != -1) {
                allDisplays.find { it.displayId == savedPref }
                    ?: HandheldDisplayManager.findBottomDisplay(this)
            } else {
                HandheldDisplayManager.findBottomDisplay(this)
            }
        }

        currentDisplayId = targetDisplay.displayId
        SecurePreferences(this).overlayDisplayId = currentDisplayId

        // CRITICAL FOR AYN THOR & ANDROID 12/13/14+:
        // Proper WindowContext creation using createWindowContext(targetDisplay, TYPE_APPLICATION_OVERLAY, null).
        // This binds the WindowContext specifically to the target secondary display with window type 2038
        // (TYPE_APPLICATION_OVERLAY), eliminating IllegalArgumentException window type mismatches.
        val windowContext: Context = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                createWindowContext(
                    targetDisplay,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    null
                )
            } catch (_: Exception) {
                createDisplayContext(targetDisplay).let { dispCtx ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        try {
                            dispCtx.createWindowContext(
                                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                                null
                            )
                        } catch (_: Exception) {
                            dispCtx
                        }
                    } else {
                        dispCtx
                    }
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val displayContext = createDisplayContext(targetDisplay)
            try {
                displayContext.createWindowContext(
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    null
                )
            } catch (_: Exception) {
                displayContext
            }
        } else {
            createDisplayContext(targetDisplay)
        }

        val wm = windowContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        currentWindowManager = wm

        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        // CRITICAL: FLAG_NOT_FOCUSABLE keeps DroidDeck/GW2 in focus on the top screen at all times!
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.FILL
        }

        // Host the UI components inside ComposeView using the properly created windowContext
        val newComposeView = ComposeView(windowContext).apply {
            setViewTreeLifecycleOwner(this@TyriaOverlayService)
            setViewTreeSavedStateRegistryOwner(this@TyriaOverlayService)
            setViewTreeViewModelStoreOwner(this@TyriaOverlayService)
            setContent {
                CompositionLocalProvider(LocalContext provides windowContext) {
                    val context = LocalContext.current
                    val application = context.applicationContext as android.app.Application
                    val viewModel = remember { MainViewModel(application) }
                    val state by viewModel.uiState.collectAsStateWithLifecycle()

                    TyriaHubAppContent(
                        state = state,
                        viewModel = viewModel,
                        isOverlay = true,
                        onTriggerSkillExternal = { slot, label, keybind, isHeavy ->
                            injectKey(slot, label, keybind, isHeavy)
                        },
                        onExitOverlay = { stopSelf() },
                        onSwitchToWindowMode = {
                            val intent = Intent(this@TyriaOverlayService, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                putExtra(MainActivity.EXTRA_FORCE_WINDOW, true)
                            }
                            startActivity(intent)
                        }
                    )
                }
            }
        }

        try {
            wm.addView(newComposeView, params)
            overlayComposeView = newComposeView
        } catch (_: Exception) {
            stopSelf()
        }
    }

    private fun injectKey(slot: String, label: String, keybind: String, isHeavy: Boolean) {
        if (isHeavy) haptics.playHeavyTap() else haptics.playSkillTap()

        val keyCode = TyriaHubInputMethodService.mapKeyToCode(keybind)
        if (keyCode != KeyEvent.KEYCODE_UNKNOWN) {
            FastKeyInjector.injectKeyAsync(keyCode, keybind)
        } else {
            TyriaHubInputMethodService.sendKey(keybind)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isOverlayActive = false
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        appViewModelStore.clear()

        overlayComposeView?.let {
            try {
                currentWindowManager?.removeView(it)
            } catch (_: Exception) {}
        }
    }

    companion object {
        const val EXTRA_TARGET_DISPLAY_ID = "EXTRA_TARGET_DISPLAY_ID"
        var isOverlayActive: Boolean = false
            private set
    }
}

