package com.example.tyriahub.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.KeyEvent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tyriahub.api.Gw2ApiClient
import com.example.tyriahub.api.SecurePreferences
import com.example.tyriahub.haptics.HapticsManager
import com.example.tyriahub.ime.TyriaHubInputMethodService
import com.example.tyriahub.model.AccountResponse
import com.example.tyriahub.model.ActiveBossStatus
import com.example.tyriahub.model.CharacterResponse
import com.example.tyriahub.model.PresetBuilds
import com.example.tyriahub.model.ProfessionBuild
import com.example.tyriahub.model.TokenInfoResponse
import com.example.tyriahub.model.TyrianTimeStatus
import com.example.tyriahub.model.WorldBossRepository
import com.example.tyriahub.theme.Gw2Themes
import com.example.tyriahub.theme.ProfessionTheme
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.tyriahub.ime.FastKeyInjector

enum class HubTab {
    TOUCH_DECK,
    TIMERS,
    WIZARDS_VAULT,
    CHARACTERS
}

enum class DeckSubMode {
    THUMB_WINGS,
    COMBAT_BAR,
    MOUNTS_QOL
}

data class HubUiState(
    val currentTab: HubTab = HubTab.TOUCH_DECK,
    val deckSubMode: DeckSubMode = DeckSubMode.THUMB_WINGS,
    val sizeMode: String = "MD",       // "XS", "MD", "LG"
    val isSoundEnabled: Boolean = true,
    val isHapticsEnabled: Boolean = true,
    val hapticIntensity: String = "MEDIUM", // "LIGHT", "MEDIUM", "HEAVY"
    val selectedBuild: ProfessionBuild = PresetBuilds.Dragonhunter,
    val theme: ProfessionTheme = Gw2Themes.Guardian,
    val showBuildSelector: Boolean = false,
    val showSetupWizard: Boolean = false,
    val showControllerGuide: Boolean = false,
    val showSettingsDialog: Boolean = false,
    val topInsetPaddingDp: Int = 12,
    val bottomInsetPaddingDp: Int = 8,
    val overlayAlpha: Float = 1.0f,
    val chatMacroDelayMs: Long = 40L,
    val defaultDeckSubMode: DeckSubMode = DeckSubMode.THUMB_WINGS,
    val leftHandedMode: Boolean = false,
    val amoledBlack: Boolean = false,
    val highContrast: Boolean = false,
    val themeOverride: String = "AUTO",
    val keepScreenAwake: Boolean = true,
    val inputMethodPreference: String = "IME",
    val targetOverlayDisplayId: Int = -1,
    val autoLaunchOverlay: Boolean = true,
    val lastActionFeedback: String = "Ready for DroidDeck Input",
    val activePressedSlot: String? = null,
    val activeBosses: List<ActiveBossStatus> = emptyList(),
    val liveBoss: ActiveBossStatus? = null,
    val tyrianTime: TyrianTimeStatus = TyrianTimeStatus("Day", 20, 14, 30),
    val toastMessage: String? = null,
    val apiKey: String = "",
    val tokenInfo: TokenInfoResponse? = null,
    val account: AccountResponse? = null,
    val characterList: List<CharacterResponse> = emptyList(),
    val selectedCharacter: CharacterResponse? = null,
    val walletCoins: Long = 0,
    val karma: Long = 0,
    val laurels: Long = 0,
    val gems: Long = 0,
    val astralAcclaim: Long = 0,
    val isApiLoading: Boolean = false,
    val apiErrorMessage: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val prefs = SecurePreferences(application)
    val haptics = HapticsManager(application)

    private val initialSubMode = when (prefs.defaultDeckSubMode) {
        "COMBAT_BAR" -> DeckSubMode.COMBAT_BAR
        "MOUNTS_QOL" -> DeckSubMode.MOUNTS_QOL
        else -> DeckSubMode.THUMB_WINGS
    }

    private val _uiState = MutableStateFlow(
        HubUiState(
            sizeMode = prefs.buttonSizeMode,
            isHapticsEnabled = prefs.hapticsEnabled,
            hapticIntensity = prefs.hapticIntensity,
            isSoundEnabled = prefs.soundEnabled,
            apiKey = prefs.apiKey,
            showSetupWizard = !prefs.isSetupCompleted,
            deckSubMode = initialSubMode,
            defaultDeckSubMode = initialSubMode,
            topInsetPaddingDp = prefs.topInsetPaddingDp,
            bottomInsetPaddingDp = prefs.bottomInsetPaddingDp,
            overlayAlpha = prefs.overlayAlpha,
            chatMacroDelayMs = prefs.chatMacroDelayMs,
            leftHandedMode = prefs.leftHandedMode,
            amoledBlack = prefs.amoledBlack,
            highContrast = prefs.highContrast,
            themeOverride = prefs.themeOverride,
            keepScreenAwake = prefs.keepScreenAwake,
            inputMethodPreference = prefs.inputMethodPreference,
            targetOverlayDisplayId = prefs.overlayDisplayId,
            autoLaunchOverlay = prefs.autoLaunchOverlay
        )
    )
    val uiState: StateFlow<HubUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        haptics.isEnabled = prefs.hapticsEnabled
        haptics.intensity = prefs.hapticIntensity
        loadSavedBuild(prefs.selectedBuildId)
        applyThemeOverride(prefs.themeOverride)
        startTimerTicker()
        if (prefs.apiKey.isNotBlank()) {
            fetchAccountData(prefs.apiKey)
        }
    }

    private fun loadSavedBuild(buildId: String) {
        val build = PresetBuilds.all.find { it.id == buildId } ?: PresetBuilds.Dragonhunter
        val chosenTheme = if (prefs.themeOverride != "AUTO") {
            Gw2Themes.forProfession(prefs.themeOverride)
        } else {
            Gw2Themes.forProfession(build.profession)
        }
        _uiState.update {
            it.copy(
                selectedBuild = build,
                theme = chosenTheme
            )
        }
    }

    private fun applyThemeOverride(override: String) {
        if (override != "AUTO") {
            val theme = Gw2Themes.forProfession(override)
            _uiState.update { it.copy(theme = theme) }
        }
    }

    private fun startTimerTicker() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                val currentEvents = WorldBossRepository.calculateCurrentEvents()
                val tyrian = WorldBossRepository.calculateTyrianTime()
                val live = currentEvents.firstOrNull { it.isLive } ?: currentEvents.firstOrNull()

                _uiState.update {
                    it.copy(
                        activeBosses = currentEvents,
                        liveBoss = live,
                        tyrianTime = tyrian
                    )
                }
                delay(10000) // update every 10s
            }
        }
    }

    fun selectTab(tab: HubTab) {
        haptics.playSkillTap()
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun selectDeckSubMode(mode: DeckSubMode) {
        haptics.playSkillTap()
        _uiState.update { it.copy(deckSubMode = mode) }
    }

    fun setSizeMode(mode: String) {
        haptics.playSkillTap()
        prefs.buttonSizeMode = mode
        _uiState.update { it.copy(sizeMode = mode) }
    }

    fun toggleHaptics() {
        val next = !_uiState.value.isHapticsEnabled
        prefs.hapticsEnabled = next
        haptics.isEnabled = next
        if (next) haptics.playSkillTap()
        _uiState.update { it.copy(isHapticsEnabled = next) }
    }

    fun toggleSound() {
        val next = !_uiState.value.isSoundEnabled
        prefs.soundEnabled = next
        _uiState.update { it.copy(isSoundEnabled = next) }
    }

    fun selectBuild(build: ProfessionBuild) {
        haptics.playHeavyTap()
        prefs.selectedBuildId = build.id
        val newTheme = Gw2Themes.forProfession(build.profession)
        _uiState.update {
            it.copy(
                selectedBuild = build,
                theme = newTheme,
                showBuildSelector = false,
                lastActionFeedback = "Switched to ${build.eliteSpec} (${build.profession})"
            )
        }
    }

    fun setShowBuildSelector(show: Boolean) {
        haptics.playSkillTap()
        _uiState.update { it.copy(showBuildSelector = show) }
    }

    fun setShowControllerGuide(show: Boolean) {
        haptics.playSkillTap()
        _uiState.update { it.copy(showControllerGuide = show) }
    }

    fun setShowSettingsDialog(show: Boolean) {
        haptics.playSkillTap()
        _uiState.update { it.copy(showSettingsDialog = show) }
    }

    fun setShowSetupWizard(show: Boolean) {
        _uiState.update { it.copy(showSetupWizard = show) }
    }

    fun setHapticIntensity(intensity: String) {
        prefs.hapticIntensity = intensity
        haptics.intensity = intensity
        haptics.playSkillTap()
        _uiState.update { it.copy(hapticIntensity = intensity) }
    }

    fun setTopInsetPaddingDp(dp: Int) {
        prefs.topInsetPaddingDp = dp
        _uiState.update { it.copy(topInsetPaddingDp = dp) }
    }

    fun setBottomInsetPaddingDp(dp: Int) {
        prefs.bottomInsetPaddingDp = dp
        _uiState.update { it.copy(bottomInsetPaddingDp = dp) }
    }

    fun setOverlayAlpha(alpha: Float) {
        prefs.overlayAlpha = alpha
        _uiState.update { it.copy(overlayAlpha = alpha) }
    }

    fun setChatMacroDelayMs(ms: Long) {
        prefs.chatMacroDelayMs = ms
        _uiState.update { it.copy(chatMacroDelayMs = ms) }
    }

    fun setDefaultDeckSubMode(mode: DeckSubMode) {
        prefs.defaultDeckSubMode = mode.name
        _uiState.update { it.copy(defaultDeckSubMode = mode) }
    }

    fun setLeftHandedMode(enabled: Boolean) {
        prefs.leftHandedMode = enabled
        _uiState.update { it.copy(leftHandedMode = enabled) }
    }

    fun setAmoledBlack(enabled: Boolean) {
        prefs.amoledBlack = enabled
        _uiState.update { it.copy(amoledBlack = enabled) }
    }

    fun setHighContrast(enabled: Boolean) {
        prefs.highContrast = enabled
        _uiState.update { it.copy(highContrast = enabled) }
    }

    fun setThemeOverride(override: String) {
        prefs.themeOverride = override
        val newTheme = if (override != "AUTO") {
            Gw2Themes.forProfession(override)
        } else {
            Gw2Themes.forProfession(_uiState.value.selectedBuild.profession)
        }
        _uiState.update { it.copy(themeOverride = override, theme = newTheme) }
    }

    fun setKeepScreenAwake(enabled: Boolean) {
        prefs.keepScreenAwake = enabled
        _uiState.update { it.copy(keepScreenAwake = enabled) }
    }

    fun setInputMethodPreference(method: String) {
        prefs.inputMethodPreference = method
        _uiState.update { it.copy(inputMethodPreference = method) }
    }

    fun setTargetOverlayDisplayId(displayId: Int) {
        prefs.overlayDisplayId = displayId
        _uiState.update { it.copy(targetOverlayDisplayId = displayId) }
    }

    fun setAutoLaunchOverlay(enabled: Boolean) {
        prefs.autoLaunchOverlay = enabled
        _uiState.update { it.copy(autoLaunchOverlay = enabled) }
    }

    fun resetToDefaults() {
        prefs.resetToDefaults()
        haptics.isEnabled = true
        haptics.intensity = "MEDIUM"
        _uiState.update {
            it.copy(
                topInsetPaddingDp = 12,
                bottomInsetPaddingDp = 8,
                overlayAlpha = 1.0f,
                chatMacroDelayMs = 40L,
                sizeMode = "MD",
                isHapticsEnabled = true,
                hapticIntensity = "MEDIUM",
                isSoundEnabled = true,
                autoLaunchOverlay = true,
                leftHandedMode = false,
                amoledBlack = false,
                highContrast = false,
                themeOverride = "AUTO",
                keepScreenAwake = true,
                inputMethodPreference = "IME",
                toastMessage = "Settings reset to defaults"
            )
        }
    }

    fun completeSetup(apiKey: String, sizeMode: String, hapticsEnabled: Boolean) {
        prefs.isSetupCompleted = true
        prefs.apiKey = apiKey.trim()
        prefs.buttonSizeMode = sizeMode
        prefs.hapticsEnabled = hapticsEnabled
        haptics.isEnabled = hapticsEnabled
        _uiState.update {
            it.copy(
                showSetupWizard = false,
                sizeMode = sizeMode,
                isHapticsEnabled = hapticsEnabled,
                apiKey = apiKey.trim(),
                toastMessage = "Setup complete! Tyria Hub ready."
            )
        }
        if (apiKey.isNotBlank()) {
            fetchAccountData(apiKey)
        }
    }

    fun pasteWaypointToChat(waypointCode: String) {
        copyWaypoint(waypointCode)
        haptics.playHeavyTap()

        viewModelScope.launch(Dispatchers.IO) {
            val delayMs = _uiState.value.chatMacroDelayMs.coerceAtLeast(60L)

            // Step 1: Open chat (ENTER) via dedicated multithreaded shell stream
            FastKeyInjector.sendEnter()
            delay(delayMs)

            // Step 2: Paste waypoint code into chat asynchronously
            FastKeyInjector.sendTextAsync(waypointCode)
            delay(delayMs.coerceAtLeast(80L))

            // Step 3: CRITICAL - Press ENTER after pasting to chat to broadcast the message!
            FastKeyInjector.sendEnter()
            delay(60L)
            FastKeyInjector.sendEnter()

            _uiState.update {
                it.copy(
                    lastActionFeedback = "💬 Broadcast $waypointCode to GW2 Chat",
                    toastMessage = "Broadcast $waypointCode to GW2 Chat!"
                )
            }
        }
    }

    fun triggerSkill(slotName: String, actionLabel: String, keybind: String, isHeavy: Boolean = false) {
        if (isHeavy) {
            haptics.playHeavyTap()
        } else {
            haptics.playSkillTap()
        }

        val keyCode = TyriaHubInputMethodService.mapKeyToCode(keybind)
        if (keyCode != KeyEvent.KEYCODE_UNKNOWN) {
            FastKeyInjector.injectKeyAsync(keyCode, keybind)
        } else {
            TyriaHubInputMethodService.sendKey(keybind)
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    activePressedSlot = slotName,
                    lastActionFeedback = "⚡ $actionLabel [Key: $keybind] Injected"
                )
            }
            delay(120)
            _uiState.update { it.copy(activePressedSlot = null) }
        }
    }

    fun copyWaypoint(waypointCode: String) {
        haptics.playSkillTap()
        val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("GW2 Waypoint", waypointCode)
        clipboard.setPrimaryClip(clip)
        _uiState.update { it.copy(toastMessage = "Copied $waypointCode to clipboard!") }
        viewModelScope.launch {
            delay(2500)
            _uiState.update { it.copy(toastMessage = null) }
        }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun fetchAccountData(apiKey: String) {
        val cleanKey = apiKey.trim()
        if (cleanKey.isBlank()) return

        _uiState.update { it.copy(isApiLoading = true, apiErrorMessage = null) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bearer = "Bearer $cleanKey"

                // Multithreaded concurrent fetching of core account endpoints in parallel
                val tokenDeferred = async { Gw2ApiClient.api.getTokenInfo(bearer) }
                val accountDeferred = async {
                    try { Gw2ApiClient.api.getAccount(bearer) } catch (_: Exception) { null }
                }
                val walletDeferred = async {
                    try { Gw2ApiClient.api.getWallet(bearer) } catch (_: Exception) { emptyList() }
                }
                val charNamesDeferred = async {
                    try { Gw2ApiClient.api.getCharacterNames(bearer) } catch (_: Exception) { emptyList() }
                }

                val token = tokenDeferred.await()
                val account = accountDeferred.await()
                val wallet = walletDeferred.await()
                val characterNames = charNamesDeferred.await()

                val coins = wallet.find { it.id == 1 }?.value ?: 0
                val karma = wallet.find { it.id == 2 }?.value ?: 0
                val laurels = wallet.find { it.id == 3 }?.value ?: 0
                val gems = wallet.find { it.id == 4 }?.value ?: 0
                val astral = wallet.find { it.id == 67 }?.value ?: 0

                // Multithreaded parallel fetching of up to 8 character details simultaneously
                val characterDeferreds = characterNames.take(8).map { name ->
                    async {
                        try {
                            Gw2ApiClient.api.getCharacter(name, bearer)
                        } catch (_: Exception) {
                            null
                        }
                    }
                }
                val characters = characterDeferreds.awaitAll().filterNotNull()

                _uiState.update {
                    it.copy(
                        isApiLoading = false,
                        apiKey = cleanKey,
                        tokenInfo = token,
                        account = account,
                        characterList = characters,
                        selectedCharacter = characters.firstOrNull(),
                        walletCoins = coins,
                        karma = karma,
                        laurels = laurels,
                        gems = gems,
                        astralAcclaim = astral,
                        toastMessage = "Connected to GW2 Account: ${account?.name ?: token.name}"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isApiLoading = false,
                        apiErrorMessage = "API Key Error: ${e.message ?: "Failed to verify token"}"
                    )
                }
            }
        }
    }

    fun selectCharacter(character: CharacterResponse) {
        haptics.playSkillTap()
        val matchedBuild = PresetBuilds.all.find {
            it.profession.equals(character.profession, ignoreCase = true)
        } ?: _uiState.value.selectedBuild

        val newTheme = Gw2Themes.forProfession(character.profession)
        _uiState.update {
            it.copy(
                selectedCharacter = character,
                selectedBuild = matchedBuild,
                theme = newTheme,
                lastActionFeedback = "Active Character: ${character.name} (${character.profession})"
            )
        }
    }
}
