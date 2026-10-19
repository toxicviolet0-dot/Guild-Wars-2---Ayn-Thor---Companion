package com.example

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tyriahub.api.SecurePreferences
import com.example.tyriahub.overlay.HandheldDisplayManager
import com.example.tyriahub.overlay.TyriaOverlayService
import com.example.tyriahub.theme.Gw2Themes
import com.example.tyriahub.ui.MainViewModel
import com.example.tyriahub.ui.components.TyriaHubAppContent

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var hasAutoLaunched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val prefs = remember { SecurePreferences(this) }
            var canDrawOverlays by remember {
                mutableStateOf(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Settings.canDrawOverlays(this)
                    } else true
                )
            }
            var dismissedBanner by remember { mutableStateOf(false) }

            val customDarkScheme = darkColorScheme(
                primary = state.theme.primaryAccent,
                secondary = state.theme.secondaryAccent,
                background = Color(0xFF090A0E),
                surface = Color(0xFF11131A)
            )

            MaterialTheme(colorScheme = customDarkScheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    containerColor = Color(0xFF090A0E)
                ) { innerPadding ->
                    TyriaHubAppContent(
                        state = state,
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding),
                        isOverlay = false,
                        bannerContent = {
                            if (!canDrawOverlays && !dismissedBanner) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF1E1710))
                                        .border(1.dp, Gw2Themes.GoldAccent)
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Gamepad,
                                            contentDescription = "Dual Screen",
                                            tint = Gw2Themes.GoldAccent,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = "AYN Thor Dual-Screen Overlay Ready",
                                                color = Gw2Themes.GoldAccent,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "Enable 'Draw over other apps' so Tyria Hub runs on the bottom screen without interrupting Guild Wars 2 / DroidDeck.",
                                                color = Color(0xFFCCCCCC),
                                                fontSize = 10.sp
                                            )
                                        }
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    val intent = Intent(
                                                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                                        Uri.parse("package:$packageName")
                                                    )
                                                    startActivity(intent)
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Gw2Themes.GoldAccent,
                                                contentColor = Color.Black
                                            ),
                                            shape = RoundedCornerShape(4.dp),
                                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text("Grant & Launch", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }

                                        OutlinedButton(
                                            onClick = { dismissedBanner = true },
                                            shape = RoundedCornerShape(4.dp),
                                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text("Dismiss", fontSize = 10.sp, color = Gw2Themes.TextMuted)
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkAndTriggerAutoOverlay()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    private fun checkAndTriggerAutoOverlay() {
        val forceWindow = intent?.getBooleanExtra(EXTRA_FORCE_WINDOW, false) ?: false
        if (forceWindow) return

        val prefs = SecurePreferences(this)
        if (!prefs.autoLaunchOverlay) return
        if (hasAutoLaunched) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
            hasAutoLaunched = true
            launchOverlayService()
            moveTaskToBack(true)
        }
    }

    fun launchOverlayService() {
        val prefs = SecurePreferences(this)
        val allDisplays = HandheldDisplayManager.getAllDisplays(this)
        val targetDisplay = if (prefs.overlayDisplayId != -1) {
            allDisplays.find { it.displayId == prefs.overlayDisplayId }
                ?: HandheldDisplayManager.findBottomDisplay(this)
        } else {
            HandheldDisplayManager.findBottomDisplay(this)
        }

        val intent = Intent(this, TyriaOverlayService::class.java).apply {
            putExtra(TyriaOverlayService.EXTRA_TARGET_DISPLAY_ID, targetDisplay.displayId)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    companion object {
        const val EXTRA_FORCE_WINDOW = "EXTRA_FORCE_WINDOW"
    }
}
