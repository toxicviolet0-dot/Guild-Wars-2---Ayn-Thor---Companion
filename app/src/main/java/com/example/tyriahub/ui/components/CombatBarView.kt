package com.example.tyriahub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tyriahub.theme.Gw2Themes
import com.example.tyriahub.theme.LocalDeckSizeConfig
import com.example.tyriahub.ui.HubUiState

@Composable
fun CombatBarView(
    state: HubUiState,
    onTriggerSkill: (slotName: String, actionLabel: String, keybind: String, isHeavy: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val sizing = LocalDeckSizeConfig.current
    val btnHeight: Dp = sizing.combatBarSlotSize

    val weaponGrad = Brush.verticalGradient(listOf(Color(0xFF4A2518), Color(0xFF2E1710)))
    val weaponBorder = Color(0xFF8C3E1F)

    val healGrad = Brush.verticalGradient(listOf(Color(0xFF144D30), Color(0xFF0C2E1D)))
    val healBorder = Color(0xFF288C57)

    val utilityGrad = Brush.verticalGradient(listOf(Color(0xFF381A54), Color(0xFF221033)))
    val utilityBorder = Color(0xFF6B31A1)

    val eliteGrad = Brush.verticalGradient(listOf(Color(0xFF5E4515), Color(0xFF3B2B0D)))
    val eliteBorder = Color(0xFFA67A24)

    val mechGrad = Brush.verticalGradient(listOf(state.theme.mechanicBadgeBg, Color(0xFF0D131C)))

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0C10))
            .padding(sizing.cardPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(sizing.cardSpacing)
    ) {
        // Top Profession Mechanics
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${state.selectedBuild.profession} Mechanics: ",
                color = state.theme.textAccent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )
            state.selectedBuild.mechanics.forEach { mech ->
                TouchButton(
                    title = mech.slot,
                    subLabel = mech.name,
                    keyHint = mech.keybind,
                    gradient = mechGrad,
                    borderColor = state.theme.mechanicBadgeBorder,
                    isActivePressed = state.activePressedSlot == mech.slot,
                    height = btnHeight * 0.8f,
                    onClick = {
                        onTriggerSkill(mech.slot, "${mech.slot} ${mech.name}", mech.keybind, true)
                    },
                    modifier = Modifier
                        .width(96.dp)
                        .padding(horizontal = 4.dp),
                    testTag = "combat_mech_${mech.slot}"
                )
            }
        }

        // Full 1 - 0 Skill Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Weapon Skills 1 to 5
            for (i in 1..5) {
                TouchButton(
                    title = "$i",
                    subLabel = if (i == 1) "Auto" else "Skill $i",
                    keyHint = "$i",
                    gradient = weaponGrad,
                    borderColor = weaponBorder,
                    isActivePressed = state.activePressedSlot == "$i",
                    height = btnHeight,
                    onClick = { onTriggerSkill("$i", "Skill $i", "$i", false) },
                    modifier = Modifier.weight(1f),
                    testTag = "combat_$i"
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Heal 6
            TouchButton(
                title = state.selectedBuild.healName,
                subLabel = "Heal",
                keyHint = "6",
                gradient = healGrad,
                borderColor = healBorder,
                isActivePressed = state.activePressedSlot == "6",
                height = btnHeight,
                onClick = { onTriggerSkill("6", state.selectedBuild.healName, "6", true) },
                modifier = Modifier.weight(1.2f),
                testTag = "combat_6"
            )

            // Utility 7, 8, 9
            val utils = listOf(
                state.selectedBuild.util1Name to "7",
                state.selectedBuild.util2Name to "8",
                state.selectedBuild.util3Name to "9"
            )
            utils.forEach { (name, slot) ->
                TouchButton(
                    title = name,
                    subLabel = "Util",
                    keyHint = slot,
                    gradient = utilityGrad,
                    borderColor = utilityBorder,
                    isActivePressed = state.activePressedSlot == slot,
                    height = btnHeight,
                    onClick = { onTriggerSkill(slot, name, slot, false) },
                    modifier = Modifier.weight(1.1f),
                    testTag = "combat_$slot"
                )
            }

            // Elite 0
            TouchButton(
                title = state.selectedBuild.eliteName,
                subLabel = "Elite",
                keyHint = "0",
                gradient = eliteGrad,
                borderColor = eliteBorder,
                isActivePressed = state.activePressedSlot == "0",
                height = btnHeight,
                onClick = { onTriggerSkill("0", state.selectedBuild.eliteName, "0", true) },
                modifier = Modifier.weight(1.2f),
                testTag = "combat_0"
            )
        }

        // Auxiliary Combat Bar (Swap, Dodge, Special, Target, Stomp, Call)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TouchButton(
                title = "WEAPON SWAP",
                subLabel = "Cycle Weapon Sets",
                keyHint = "`",
                gradient = Brush.verticalGradient(listOf(Color(0xFF13526E), Color(0xFF0C3142))),
                borderColor = Color(0xFF1C7DA6),
                isActivePressed = state.activePressedSlot == "swap",
                height = btnHeight * 0.85f,
                onClick = { onTriggerSkill("swap", "Weapon Swap", "`", true) },
                modifier = Modifier.weight(1f)
            )

            TouchButton(
                title = "DODGE ROLL",
                subLabel = "Evade Incoming Damage",
                keyHint = "V",
                gradient = Brush.verticalGradient(listOf(Color(0xFF5C1929), Color(0xFF380F19))),
                borderColor = Color(0xFF9E2C47),
                isActivePressed = state.activePressedSlot == "dodge",
                height = btnHeight * 0.85f,
                onClick = { onTriggerSkill("dodge", "Evade Roll", "V", true) },
                modifier = Modifier.weight(1f)
            )

            TouchButton(
                title = "SPECIAL ACTION",
                subLabel = "Raid / Scribing Key",
                keyHint = "N",
                gradient = Brush.verticalGradient(listOf(Color(0xFF14524A), Color(0xFF0B332E))),
                borderColor = Color(0xFF268C7F),
                isActivePressed = state.activePressedSlot == "special",
                height = btnHeight * 0.85f,
                onClick = { onTriggerSkill("special", "Special Action", "N", false) },
                modifier = Modifier.weight(1f)
            )

            TouchButton(
                title = "NEAREST TARGET",
                subLabel = "Lock Next Foe",
                keyHint = "TAB",
                gradient = Brush.verticalGradient(listOf(Color(0xFF28204D), Color(0xFF17132E))),
                borderColor = Color(0xFF493B8C),
                isActivePressed = state.activePressedSlot == "target",
                height = btnHeight * 0.85f,
                onClick = { onTriggerSkill("target", "Target Foe", "TAB", false) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
