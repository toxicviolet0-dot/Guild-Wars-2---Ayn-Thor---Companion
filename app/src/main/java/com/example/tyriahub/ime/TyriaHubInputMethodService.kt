package com.example.tyriahub.ime

import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import com.example.tyriahub.model.PresetBuilds
import com.example.tyriahub.theme.Gw2Themes
import com.example.tyriahub.ui.HubUiState
import com.example.tyriahub.ui.components.ThumbWingsView
import java.lang.ref.WeakReference

class TyriaHubInputMethodService : InputMethodService() {

    override fun onCreate() {
        super.onCreate()
        activeInstance = WeakReference(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (activeInstance?.get() == this) {
            activeInstance = null
        }
    }

    override fun onCreateInputView(): View {
        return ComposeView(this).apply {
            setContent {
                val theme = Gw2Themes.Guardian
                val customDarkScheme = darkColorScheme(
                    primary = theme.primaryAccent,
                    secondary = theme.secondaryAccent,
                    background = Color(0xFF090A0E),
                    surface = Color(0xFF11131A)
                )

                MaterialTheme(colorScheme = customDarkScheme) {
                    ThumbWingsView(
                        state = HubUiState(
                            selectedBuild = PresetBuilds.Dragonhunter,
                            theme = theme,
                            lastActionFeedback = "IME Input Ready"
                        ),
                        onTriggerSkill = { slot, label, keybind, _ ->
                            sendKey(keybind)
                        },
                        onChangeBuild = { }
                    )
                }
            }
        }
    }

    fun dispatchKey(keyName: String) {
        val keyCode = mapKeyToCode(keyName)
        if (keyCode != KeyEvent.KEYCODE_UNKNOWN) {
            val ic = currentInputConnection
            if (ic != null) {
                ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, keyCode))
                ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, keyCode))
            } else {
                sendDownUpKeyEvents(keyCode)
            }
        }
    }

    companion object {
        private var activeInstance: WeakReference<TyriaHubInputMethodService>? = null

        val isImeActive: Boolean
            get() = activeInstance?.get() != null

        fun sendKey(keyName: String): Boolean {
            val service = activeInstance?.get()
            return if (service != null) {
                service.dispatchKey(keyName)
                true
            } else {
                false
            }
        }

        fun mapKeyToCode(keyName: String): Int {
            val clean = keyName.trim().uppercase()
            return when (clean) {
                "1" -> KeyEvent.KEYCODE_1
                "2" -> KeyEvent.KEYCODE_2
                "3" -> KeyEvent.KEYCODE_3
                "4" -> KeyEvent.KEYCODE_4
                "5" -> KeyEvent.KEYCODE_5
                "6" -> KeyEvent.KEYCODE_6
                "7" -> KeyEvent.KEYCODE_7
                "8" -> KeyEvent.KEYCODE_8
                "9" -> KeyEvent.KEYCODE_9
                "0" -> KeyEvent.KEYCODE_0
                "F1" -> KeyEvent.KEYCODE_F1
                "F2" -> KeyEvent.KEYCODE_F2
                "F3" -> KeyEvent.KEYCODE_F3
                "F4" -> KeyEvent.KEYCODE_F4
                "F5" -> KeyEvent.KEYCODE_F5
                "TAB" -> KeyEvent.KEYCODE_TAB
                "V" -> KeyEvent.KEYCODE_V
                "Y" -> KeyEvent.KEYCODE_Y
                "N" -> KeyEvent.KEYCODE_N
                "T" -> KeyEvent.KEYCODE_T
                "M" -> KeyEvent.KEYCODE_M
                "I" -> KeyEvent.KEYCODE_I
                "H", "C" -> KeyEvent.KEYCODE_C
                "O" -> KeyEvent.KEYCODE_O
                "G" -> KeyEvent.KEYCODE_G
                "X" -> KeyEvent.KEYCODE_X
                "B" -> KeyEvent.KEYCODE_B
                "W" -> KeyEvent.KEYCODE_W
                "J" -> KeyEvent.KEYCODE_J
                "`", "~" -> KeyEvent.KEYCODE_GRAVE
                "SPACE" -> KeyEvent.KEYCODE_SPACE
                "ENTER" -> KeyEvent.KEYCODE_ENTER
                else -> KeyEvent.KEYCODE_UNKNOWN
            }
        }
    }
}
