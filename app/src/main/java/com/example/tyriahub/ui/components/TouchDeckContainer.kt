package com.example.tyriahub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tyriahub.theme.Gw2Themes
import com.example.tyriahub.ui.DeckSubMode
import com.example.tyriahub.ui.HubUiState

@Composable
fun TouchDeckContainer(
    state: HubUiState,
    onSelectSubMode: (DeckSubMode) -> Unit,
    onTriggerSkill: (slotName: String, actionLabel: String, keybind: String, isHeavy: Boolean) -> Unit,
    onChangeBuild: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0C10))
    ) {
        // Slim Mode Switcher Bar inside the Touch Deck tab
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0D0F17))
                .padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            DeckSubTabPill(
                label = "🎮 Thumb Wings",
                isSelected = state.deckSubMode == DeckSubMode.THUMB_WINGS,
                activeColor = state.theme.primaryAccent,
                onClick = { onSelectSubMode(DeckSubMode.THUMB_WINGS) }
            )
            Spacer(modifier = Modifier.padding(horizontal = 3.dp))
            DeckSubTabPill(
                label = "⚔️ Combat Bar",
                isSelected = state.deckSubMode == DeckSubMode.COMBAT_BAR,
                activeColor = state.theme.primaryAccent,
                onClick = { onSelectSubMode(DeckSubMode.COMBAT_BAR) }
            )
            Spacer(modifier = Modifier.padding(horizontal = 3.dp))
            DeckSubTabPill(
                label = "🦅 Mounts & QoL",
                isSelected = state.deckSubMode == DeckSubMode.MOUNTS_QOL,
                activeColor = state.theme.primaryAccent,
                onClick = { onSelectSubMode(DeckSubMode.MOUNTS_QOL) }
            )
        }

        // Render the active IME view
        Box(modifier = Modifier.weight(1f)) {
            when (state.deckSubMode) {
                DeckSubMode.THUMB_WINGS -> {
                    ThumbWingsView(
                        state = state,
                        onTriggerSkill = onTriggerSkill,
                        onChangeBuild = onChangeBuild
                    )
                }
                DeckSubMode.COMBAT_BAR -> {
                    CombatBarView(
                        state = state,
                        onTriggerSkill = onTriggerSkill
                    )
                }
                DeckSubMode.MOUNTS_QOL -> {
                    MountsQoLView(
                        state = state,
                        onTriggerSkill = onTriggerSkill
                    )
                }
            }
        }
    }
}

@Composable
private fun DeckSubTabPill(
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) activeColor.copy(alpha = 0.22f) else Color(0xFF141724))
            .border(
                width = 1.dp,
                color = if (isSelected) activeColor else Gw2Themes.BorderSubtle,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else Gw2Themes.TextMuted
        )
    }
}
