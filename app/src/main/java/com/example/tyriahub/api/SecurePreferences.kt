package com.example.tyriahub.api

import android.content.Context
import android.content.SharedPreferences

class SecurePreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("tyria_hub_config", Context.MODE_PRIVATE)

    var apiKey: String
        get() = prefs.getString(KEY_API_KEY, "") ?: ""
        set(value) = prefs.edit().putString(KEY_API_KEY, value.trim()).apply()

    var isSetupCompleted: Boolean
        get() = prefs.getBoolean(KEY_SETUP_COMPLETED, false)
        set(value) = prefs.edit().putBoolean(KEY_SETUP_COMPLETED, value).apply()

    var buttonSizeMode: String
        get() = prefs.getString(KEY_SIZE_MODE, "MD") ?: "MD"
        set(value) = prefs.edit().putString(KEY_SIZE_MODE, value).apply()

    var hapticsEnabled: Boolean
        get() = prefs.getBoolean(KEY_HAPTICS, true)
        set(value) = prefs.edit().putBoolean(KEY_HAPTICS, value).apply()

    var soundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND, true)
        set(value) = prefs.edit().putBoolean(KEY_SOUND, value).apply()

    var selectedBuildId: String
        get() = prefs.getString(KEY_BUILD_ID, "guardian_dragonhunter") ?: "guardian_dragonhunter"
        set(value) = prefs.edit().putString(KEY_BUILD_ID, value).apply()

    var overlayDisplayId: Int
        get() = prefs.getInt(KEY_OVERLAY_DISPLAY_ID, -1)
        set(value) = prefs.edit().putInt(KEY_OVERLAY_DISPLAY_ID, value).apply()

    var autoLaunchOverlay: Boolean
        get() = prefs.getBoolean(KEY_AUTO_LAUNCH_OVERLAY, true)
        set(value) = prefs.edit().putBoolean(KEY_AUTO_LAUNCH_OVERLAY, value).apply()

    var overlayCompactMode: Boolean
        get() = prefs.getBoolean(KEY_OVERLAY_COMPACT_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_OVERLAY_COMPACT_MODE, value).apply()

    var topInsetPaddingDp: Int
        get() = prefs.getInt(KEY_TOP_INSET_DP, 12)
        set(value) = prefs.edit().putInt(KEY_TOP_INSET_DP, value).apply()

    var bottomInsetPaddingDp: Int
        get() = prefs.getInt(KEY_BOTTOM_INSET_DP, 8)
        set(value) = prefs.edit().putInt(KEY_BOTTOM_INSET_DP, value).apply()

    var overlayAlpha: Float
        get() = prefs.getFloat(KEY_OVERLAY_ALPHA, 1.0f)
        set(value) = prefs.edit().putFloat(KEY_OVERLAY_ALPHA, value).apply()

    var chatMacroDelayMs: Long
        get() = prefs.getLong(KEY_CHAT_MACRO_DELAY, 40L)
        set(value) = prefs.edit().putLong(KEY_CHAT_MACRO_DELAY, value).apply()

    var defaultDeckSubMode: String
        get() = prefs.getString(KEY_DEFAULT_DECK_SUBMODE, "THUMB_WINGS") ?: "THUMB_WINGS"
        set(value) = prefs.edit().putString(KEY_DEFAULT_DECK_SUBMODE, value).apply()

    var hapticIntensity: String
        get() = prefs.getString(KEY_HAPTIC_INTENSITY, "MEDIUM") ?: "MEDIUM"
        set(value) = prefs.edit().putString(KEY_HAPTIC_INTENSITY, value).apply()

    var leftHandedMode: Boolean
        get() = prefs.getBoolean(KEY_LEFT_HANDED_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_LEFT_HANDED_MODE, value).apply()

    var amoledBlack: Boolean
        get() = prefs.getBoolean(KEY_AMOLED_BLACK, false)
        set(value) = prefs.edit().putBoolean(KEY_AMOLED_BLACK, value).apply()

    var highContrast: Boolean
        get() = prefs.getBoolean(KEY_HIGH_CONTRAST, false)
        set(value) = prefs.edit().putBoolean(KEY_HIGH_CONTRAST, value).apply()

    var themeOverride: String
        get() = prefs.getString(KEY_THEME_OVERRIDE, "AUTO") ?: "AUTO"
        set(value) = prefs.edit().putString(KEY_THEME_OVERRIDE, value).apply()

    var keepScreenAwake: Boolean
        get() = prefs.getBoolean(KEY_KEEP_SCREEN_AWAKE, true)
        set(value) = prefs.edit().putBoolean(KEY_KEEP_SCREEN_AWAKE, value).apply()

    var inputMethodPreference: String
        get() = prefs.getString(KEY_INPUT_METHOD, "IME") ?: "IME"
        set(value) = prefs.edit().putString(KEY_INPUT_METHOD, value).apply()

    fun resetToDefaults() {
        prefs.edit()
            .putInt(KEY_TOP_INSET_DP, 12)
            .putInt(KEY_BOTTOM_INSET_DP, 8)
            .putFloat(KEY_OVERLAY_ALPHA, 1.0f)
            .putLong(KEY_CHAT_MACRO_DELAY, 40L)
            .putString(KEY_SIZE_MODE, "MD")
            .putBoolean(KEY_HAPTICS, true)
            .putString(KEY_HAPTIC_INTENSITY, "MEDIUM")
            .putBoolean(KEY_SOUND, true)
            .putBoolean(KEY_AUTO_LAUNCH_OVERLAY, true)
            .putBoolean(KEY_LEFT_HANDED_MODE, false)
            .putBoolean(KEY_AMOLED_BLACK, false)
            .putBoolean(KEY_HIGH_CONTRAST, false)
            .putString(KEY_THEME_OVERRIDE, "AUTO")
            .putBoolean(KEY_KEEP_SCREEN_AWAKE, true)
            .putString(KEY_INPUT_METHOD, "IME")
            .apply()
    }

    companion object {
        private const val KEY_API_KEY = "gw2_api_key"
        private const val KEY_SETUP_COMPLETED = "setup_completed_v1"
        private const val KEY_SIZE_MODE = "button_size_mode"
        private const val KEY_HAPTICS = "haptics_enabled"
        private const val KEY_HAPTIC_INTENSITY = "haptic_intensity"
        private const val KEY_SOUND = "sound_enabled"
        private const val KEY_BUILD_ID = "selected_build_id"
        private const val KEY_OVERLAY_DISPLAY_ID = "target_overlay_display_id"
        private const val KEY_AUTO_LAUNCH_OVERLAY = "auto_launch_overlay"
        private const val KEY_OVERLAY_COMPACT_MODE = "overlay_compact_mode"
        private const val KEY_TOP_INSET_DP = "top_inset_padding_dp"
        private const val KEY_BOTTOM_INSET_DP = "bottom_inset_padding_dp"
        private const val KEY_OVERLAY_ALPHA = "overlay_window_alpha"
        private const val KEY_CHAT_MACRO_DELAY = "chat_macro_delay_ms"
        private const val KEY_DEFAULT_DECK_SUBMODE = "default_deck_submode"
        private const val KEY_LEFT_HANDED_MODE = "left_handed_mode"
        private const val KEY_AMOLED_BLACK = "amoled_black_enabled"
        private const val KEY_HIGH_CONTRAST = "high_contrast_enabled"
        private const val KEY_THEME_OVERRIDE = "theme_override_profession"
        private const val KEY_KEEP_SCREEN_AWAKE = "keep_screen_awake"
        private const val KEY_INPUT_METHOD = "input_method_preference"
    }
}
