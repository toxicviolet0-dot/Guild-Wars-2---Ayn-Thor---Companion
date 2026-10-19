package com.example.tyriahub.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tyriahub.ui.HubTab
import com.example.tyriahub.ui.HubUiState
import com.example.tyriahub.ui.MainViewModel

/**
 * Unified Tyria Hub Application Content.
 * Ensures the overlay and the main windowed app remain 100% synchronized,
 * using the identical navigation bar, event ticker, touch deck views, and modals.
 */
@Composable
fun TyriaHubAppContent(
    state: HubUiState,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    isOverlay: Boolean = false,
    onTriggerSkillExternal: ((slot: String, label: String, keybind: String, isHeavy: Boolean) -> Unit)? = null,
    onExitOverlay: (() -> Unit)? = null,
    onSwitchToWindowMode: (() -> Unit)? = null,
    bannerContent: (@Composable () -> Unit)? = null
) {
    val customDarkScheme = darkColorScheme(
        primary = state.theme.primaryAccent,
        secondary = state.theme.secondaryAccent,
        background = Color(0xFF090A0E),
        surface = Color(0xFF11131A)
    )

    MaterialTheme(colorScheme = customDarkScheme) {
        val baseBg = if (state.amoledBlack) Color.Black else Color(0xFF090A0E)
        val bgColor = if (isOverlay) baseBg.copy(alpha = state.overlayAlpha) else baseBg

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(bgColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = if (isOverlay) state.topInsetPaddingDp.dp else 0.dp,
                        bottom = if (isOverlay) state.bottomInsetPaddingDp.dp else 0.dp
                    )
            ) {
                // Optional contextual banner (e.g., overlay permission reminder)
                bannerContent?.invoke()

                // Top Navigation Bar
                TopNavBar(
                    state = state,
                    onSelectTab = { viewModel.selectTab(it) },
                    onSetSize = { viewModel.setSizeMode(it) },
                    onToggleSound = { viewModel.toggleSound() },
                    onToggleHaptics = { viewModel.toggleHaptics() },
                    onOpenSettings = { viewModel.setShowSettingsDialog(true) }
                )

                // Live World Boss / Meta Event Ticker
                LiveTickerBar(
                    liveBoss = state.liveBoss,
                    onPasteWaypointToChat = { viewModel.pasteWaypointToChat(it) }
                )

                // Main View Content depending on selected Tab
                Box(modifier = Modifier.weight(1f)) {
                    when (state.currentTab) {
                        HubTab.TOUCH_DECK -> {
                            TouchDeckContainer(
                                state = state,
                                onSelectSubMode = { viewModel.selectDeckSubMode(it) },
                                onTriggerSkill = { slot, label, keybind, isHeavy ->
                                    viewModel.triggerSkill(slot, label, keybind, isHeavy)
                                    onTriggerSkillExternal?.invoke(slot, label, keybind, isHeavy)
                                },
                                onChangeBuild = { viewModel.setShowBuildSelector(true) }
                            )
                        }
                        HubTab.TIMERS -> {
                            BossHudView(
                                state = state,
                                onCopyWaypoint = { viewModel.copyWaypoint(it) }
                            )
                        }
                        HubTab.WIZARDS_VAULT -> {
                            WizardsVaultView(
                                state = state
                            )
                        }
                        HubTab.CHARACTERS -> {
                            CharactersView(
                                state = state,
                                onConnectApiKey = { viewModel.fetchAccountData(it) },
                                onSelectCharacter = { viewModel.selectCharacter(it) }
                            )
                        }
                    }
                }
            }

            // Floating Toast Notification
            AnimatedVisibility(
                visible = state.toastMessage != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 70.dp)
            ) {
                state.toastMessage?.let { msg ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E2333))
                            .border(1.dp, state.theme.primaryAccent, RoundedCornerShape(8.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = msg,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // 1. Settings Modal (Base Modal Layer - zIndex 100f)
            if (state.showSettingsDialog) {
                SettingsDialog(
                    state = state,
                    viewModel = viewModel,
                    onDismiss = { viewModel.setShowSettingsDialog(false) },
                    onExitOverlay = onExitOverlay,
                    onSwitchToWindowMode = onSwitchToWindowMode
                )
            }

            // 2. Build Selector Modal (Layer 2 - zIndex 200f, renders in front of Settings)
            if (state.showBuildSelector) {
                BuildSelectorDialog(
                    currentBuild = state.selectedBuild,
                    onSelectBuild = { viewModel.selectBuild(it) },
                    onDismiss = { viewModel.setShowBuildSelector(false) }
                )
            }

            // 3. Controller Guide Modal (Layer 2 - zIndex 200f, renders in front of Settings)
            if (state.showControllerGuide) {
                ControllerGuideDialog(
                    onDismiss = { viewModel.setShowControllerGuide(false) }
                )
            }

            // 4. Setup Wizard Modal (Topmost Layer - zIndex 300f)
            if (state.showSetupWizard) {
                SetupWizardDialog(
                    initialApiKey = state.apiKey,
                    initialSizeMode = state.sizeMode,
                    initialHaptics = state.isHapticsEnabled,
                    haptics = viewModel.haptics,
                    onComplete = { key, size, haptics ->
                        viewModel.completeSetup(key, size, haptics)
                    }
                )
            }
        }
    }
}
