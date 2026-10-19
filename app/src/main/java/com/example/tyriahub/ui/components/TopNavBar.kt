package com.example.tyriahub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import com.example.tyriahub.theme.Gw2Themes
import com.example.tyriahub.ui.HubTab
import com.example.tyriahub.ui.HubUiState

@Composable
fun TopNavBar(
    state: HubUiState,
    onSelectTab: (HubTab) -> Unit,
    onSetSize: (String) -> Unit,
    onToggleSound: () -> Unit,
    onToggleHaptics: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenBuildSelector: () -> Unit = {},
    onOpenControllerGuide: () -> Unit = {},
    onResetSetup: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(38.dp)
            .background(Color(0xFF0F1117))
            .horizontalScroll(scrollState)
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Collapsed 4 Tabs
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            TabPill(
                label = "🎮 Touch Deck",
                isSelected = state.currentTab == HubTab.TOUCH_DECK,
                activeBorderColor = state.theme.primaryAccent,
                onClick = { onSelectTab(HubTab.TOUCH_DECK) }
            )
            TabPill(
                label = "⏱️ Timers",
                isSelected = state.currentTab == HubTab.TIMERS,
                activeBorderColor = state.theme.primaryAccent,
                onClick = { onSelectTab(HubTab.TIMERS) }
            )
            TabPill(
                label = "✨ Wizard's Vault",
                isSelected = state.currentTab == HubTab.WIZARDS_VAULT,
                activeBorderColor = state.theme.primaryAccent,
                onClick = { onSelectTab(HubTab.WIZARDS_VAULT) }
            )
            TabPill(
                label = "👤 Characters",
                isSelected = state.currentTab == HubTab.CHARACTERS,
                activeBorderColor = state.theme.primaryAccent,
                onClick = { onSelectTab(HubTab.CHARACTERS) }
            )
        }

        // Right Controls
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Size Toggle: Size: XS | MD | LG
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF181B26))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Size:",
                    color = Gw2Themes.TextMuted,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(end = 2.dp)
                )
                listOf("XS", "MD", "LG").forEach { size ->
                    val isSelected = state.sizeMode == size
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isSelected) state.theme.primaryAccent else Color.Transparent)
                            .clickable { onSetSize(size) }
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = size,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else Gw2Themes.TextMuted
                        )
                    }
                }
            }

            // Audio Toggle
            IconButton(
                onClick = onToggleSound,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (state.isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                    contentDescription = "Audio Toggle",
                    tint = if (state.isSoundEnabled) state.theme.primaryAccent else Gw2Themes.TextMuted,
                    modifier = Modifier.size(14.dp)
                )
            }

            // Haptics Toggle
            IconButton(
                onClick = onToggleHaptics,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Vibration,
                    contentDescription = "Haptics Toggle",
                    tint = if (state.isHapticsEnabled) state.theme.primaryAccent else Gw2Themes.TextMuted,
                    modifier = Modifier.size(14.dp)
                )
            }

            // Settings Menu Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF1B2030))
                    .border(1.dp, state.theme.primaryAccent, RoundedCornerShape(4.dp))
                    .clickable { onOpenSettings() }
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "⚙️ Settings",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TabPill(
    label: String,
    isSelected: Boolean,
    activeBorderColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) Color(0xFF1C2230) else Color(0xFF13151D))
            .border(
                width = 1.dp,
                color = if (isSelected) activeBorderColor else Gw2Themes.BorderSubtle,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Color.White else Gw2Themes.TextMuted
        )
    }
}
