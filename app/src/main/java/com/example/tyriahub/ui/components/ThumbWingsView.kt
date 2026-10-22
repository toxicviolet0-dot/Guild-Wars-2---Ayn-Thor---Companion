package com.example.tyriahub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tyriahub.theme.Gw2Themes
import com.example.tyriahub.theme.LocalDeckSizeConfig
import com.example.tyriahub.ui.HubUiState

@Composable
fun ThumbWingsView(
    state: HubUiState,
    onTriggerSkill: (slotName: String, actionLabel: String, keybind: String, isHeavy: Boolean) -> Unit,
    onChangeBuild: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Dynamic Gradients
    val weaponGrad = Brush.verticalGradient(listOf(Color(0xFF4A2518), Color(0xFF2E1710)))
    val weaponBorder = Color(0xFF8C3E1F)

    val healGrad = Brush.verticalGradient(listOf(Color(0xFF144D30), Color(0xFF0C2E1D)))
    val healBorder = Color(0xFF288C57)

    val utilityGrad = Brush.verticalGradient(listOf(Color(0xFF381A54), Color(0xFF221033)))
    val utilityBorder = Color(0xFF6B31A1)

    val eliteGrad = Brush.verticalGradient(listOf(Color(0xFF5E4515), Color(0xFF3B2B0D)))
    val eliteBorder = Color(0xFFA67A24)

    val swapGrad = Brush.verticalGradient(listOf(Color(0xFF13526E), Color(0xFF0C3142)))
    val swapBorder = Color(0xFF1C7DA6)

    val dodgeGrad = Brush.verticalGradient(listOf(Color(0xFF5C1929), Color(0xFF380F19)))
    val dodgeBorder = Color(0xFF9E2C47)

    val targetGrad = Brush.verticalGradient(listOf(Color(0xFF28204D), Color(0xFF17132E)))
    val targetBorder = Color(0xFF493B8C)

    val skyscaleGrad = Brush.verticalGradient(listOf(Color(0xFF473319), Color(0xFF2B1F0F)))
    val skyscaleBorder = Color(0xFF825D2E)

    val specialGrad = Brush.verticalGradient(listOf(Color(0xFF14524A), Color(0xFF0B332E)))
    val specialBorder = Color(0xFF268C7F)

    val assistGrad = Brush.verticalGradient(listOf(Color(0xFF232552), Color(0xFF141530)))
    val assistBorder = Color(0xFF3F4294)

    val ggGrad = Brush.verticalGradient(listOf(Color(0xFF1E212B), Color(0xFF12141A)))
    val ggBorder = Color(0xFF323747)

    val mechGrad = Brush.verticalGradient(
        listOf(state.theme.mechanicBadgeBg, Color(0xFF0D131C))
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0C10))
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        val sizing = LocalDeckSizeConfig.current
        val totalHeight = maxHeight
        // Calculate dynamic button height to fit Thor screen, scaled with sizing.wingsRowHeightMultiplier
        val calculatedRowHeight: Dp = ((totalHeight - 72.dp) / 3.1f).coerceIn(34.dp, 68.dp)
        val btnHeight: Dp = (calculatedRowHeight * sizing.wingsRowHeightMultiplier).coerceIn(30.dp, 76.dp)

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Top Mechanics Header Row with F1-F5 centered over center column
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = sizing.wingsGap * 0.5f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "● ",
                            color = Gw2Themes.WaypointYellow,
                            fontSize = sizing.captionSp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = state.selectedBuild.mechanicsTitle,
                            color = Gw2Themes.WaypointYellow,
                            fontSize = sizing.bodySp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }

                    // Center: F1 - F5 Keys
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1.1f)
                    ) {
                        state.selectedBuild.mechanics.forEach { mech ->
                            TouchButton(
                                title = mech.slot,
                                subLabel = mech.name,
                                keyHint = "",
                                gradient = mechGrad,
                                borderColor = state.theme.mechanicBadgeBorder,
                                isActivePressed = state.activePressedSlot == mech.slot,
                                height = sizing.fKeyHeight,
                                onClick = {
                                    onTriggerSkill(mech.slot, "${mech.slot} ${mech.name}", mech.keybind, true)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = sizing.wingsGap * 0.4f),
                                testTag = "mech_${mech.slot}"
                            )
                        }
                    }

                    // Right: Change Build link
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(0.6f)
                    ) {
                        Text(
                            text = "Change Build →",
                            color = Gw2Themes.TextMuted,
                            fontSize = 10.5.sp,
                            modifier = Modifier.clickable { onChangeBuild() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(1.dp))

                // Subheaders: LEFT THUMB / RIGHT THUMB
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "‹ LEFT THUMB",
                        color = Gw2Themes.GoldAccent,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "RIGHT THUMB ›",
                        color = state.theme.primaryAccent,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Main Thumb Wings Layout (Left Wing, Center Column, Right Wing)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    // LEFT THUMB CLUSTER (Weapon skills & Movement)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        // Row 1: Skills 1, 2, 3
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            TouchButton(
                                title = "1",
                                subLabel = "Auto",
                                keyHint = "",
                                gradient = weaponGrad,
                                borderColor = weaponBorder,
                                isActivePressed = state.activePressedSlot == "1",
                                height = btnHeight,
                                onClick = { onTriggerSkill("1", "Auto Attack", "1", false) },
                                modifier = Modifier.weight(1f),
                                testTag = "skill_1"
                            )
                            TouchButton(
                                title = "2",
                                subLabel = "Skill 2",
                                keyHint = "",
                                gradient = weaponGrad,
                                borderColor = weaponBorder,
                                isActivePressed = state.activePressedSlot == "2",
                                height = btnHeight,
                                onClick = { onTriggerSkill("2", "Weapon 2", "2", false) },
                                modifier = Modifier.weight(1f),
                                testTag = "skill_2"
                            )
                            TouchButton(
                                title = "3",
                                subLabel = "Skill 3",
                                keyHint = "",
                                gradient = weaponGrad,
                                borderColor = weaponBorder,
                                isActivePressed = state.activePressedSlot == "3",
                                height = btnHeight,
                                onClick = { onTriggerSkill("3", "Weapon 3", "3", false) },
                                modifier = Modifier.weight(1f),
                                testTag = "skill_3"
                            )
                        }

                        // Row 2: Skills 4, 5
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            TouchButton(
                                title = "4",
                                subLabel = "Skill 4",
                                keyHint = "",
                                gradient = weaponGrad,
                                borderColor = weaponBorder,
                                isActivePressed = state.activePressedSlot == "4",
                                height = btnHeight,
                                onClick = { onTriggerSkill("4", "Weapon 4", "4", false) },
                                modifier = Modifier.weight(1f),
                                testTag = "skill_4"
                            )
                            TouchButton(
                                title = "5",
                                subLabel = "Skill 5",
                                keyHint = "",
                                gradient = weaponGrad,
                                borderColor = weaponBorder,
                                isActivePressed = state.activePressedSlot == "5",
                                height = btnHeight,
                                onClick = { onTriggerSkill("5", "Weapon 5", "5", false) },
                                modifier = Modifier.weight(1f),
                                testTag = "skill_5"
                            )
                        }

                        // Row 3: SWAP (~) and DODGE (V)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            TouchButton(
                                title = "SWAP",
                                subLabel = "Swap (~)",
                                keyHint = "",
                                gradient = swapGrad,
                                borderColor = swapBorder,
                                isActivePressed = state.activePressedSlot == "swap",
                                height = btnHeight,
                                onClick = { onTriggerSkill("swap", "Weapon Swap", "`", true) },
                                modifier = Modifier.weight(1f),
                                testTag = "skill_swap"
                            )
                            TouchButton(
                                title = "DODGE",
                                subLabel = "Evade (V)",
                                keyHint = "",
                                gradient = dodgeGrad,
                                borderColor = dodgeBorder,
                                isActivePressed = state.activePressedSlot == "dodge",
                                height = btnHeight,
                                onClick = { onTriggerSkill("dodge", "Evade Roll", "V", true) },
                                modifier = Modifier.weight(1f),
                                testTag = "skill_dodge"
                            )
                        }
                    }

                    // CENTER CLUSTER (Target, Skyscale, /gg)
                    Column(
                        modifier = Modifier.weight(0.46f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        TouchButton(
                            title = "TARGET",
                            subLabel = "Tab",
                            keyHint = "",
                            gradient = targetGrad,
                            borderColor = targetBorder,
                            isActivePressed = state.activePressedSlot == "target",
                            height = btnHeight,
                            onClick = { onTriggerSkill("target", "Target Nearest", "TAB", false) },
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "skill_target"
                        )

                        TouchButton(
                            title = "SKYSCALE",
                            subLabel = "Y",
                            keyHint = "",
                            gradient = skyscaleGrad,
                            borderColor = skyscaleBorder,
                            isActivePressed = state.activePressedSlot == "skyscale",
                            height = btnHeight,
                            onClick = { onTriggerSkill("skyscale", "Mount Skyscale", "Y", true) },
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "skill_skyscale"
                        )

                        TouchButton(
                            title = "/gg",
                            subLabel = "Reset",
                            keyHint = "",
                            gradient = ggGrad,
                            borderColor = ggBorder,
                            isActivePressed = state.activePressedSlot == "gg",
                            height = btnHeight,
                            onClick = { onTriggerSkill("gg", "Chat Command /gg", "ENTER", false) },
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "skill_gg"
                        )
                    }

                    // RIGHT THUMB CLUSTER (Heal, Utility, Elite, Special Action)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        // Row 1: Heal (6), Utility 1 (7), Utility 2 (8)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            TouchButton(
                                title = state.selectedBuild.shortHeal,
                                subLabel = "[6]",
                                keyHint = "",
                                gradient = healGrad,
                                borderColor = healBorder,
                                isActivePressed = state.activePressedSlot == "6",
                                height = btnHeight,
                                onClick = { onTriggerSkill("6", state.selectedBuild.healName, "6", true) },
                                modifier = Modifier.weight(1f),
                                testTag = "skill_6"
                            )
                            TouchButton(
                                title = state.selectedBuild.shortUtil1,
                                subLabel = "[7]",
                                keyHint = "",
                                gradient = utilityGrad,
                                borderColor = utilityBorder,
                                isActivePressed = state.activePressedSlot == "7",
                                height = btnHeight,
                                onClick = { onTriggerSkill("7", state.selectedBuild.util1Name, "7", false) },
                                modifier = Modifier.weight(1f),
                                testTag = "skill_7"
                            )
                            TouchButton(
                                title = state.selectedBuild.shortUtil2,
                                subLabel = "[8]",
                                keyHint = "",
                                gradient = utilityGrad,
                                borderColor = utilityBorder,
                                isActivePressed = state.activePressedSlot == "8",
                                height = btnHeight,
                                onClick = { onTriggerSkill("8", state.selectedBuild.util2Name, "8", false) },
                                modifier = Modifier.weight(1f),
                                testTag = "skill_8"
                            )
                        }

                        // Row 2: Utility 3 (9) and Elite (0)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            TouchButton(
                                title = state.selectedBuild.shortUtil3,
                                subLabel = "[9]",
                                keyHint = "",
                                gradient = utilityGrad,
                                borderColor = utilityBorder,
                                isActivePressed = state.activePressedSlot == "9",
                                height = btnHeight,
                                onClick = { onTriggerSkill("9", state.selectedBuild.util3Name, "9", false) },
                                modifier = Modifier.weight(1.15f),
                                testTag = "skill_9"
                            )
                            TouchButton(
                                title = state.selectedBuild.shortElite,
                                subLabel = "[0]",
                                keyHint = "",
                                gradient = eliteGrad,
                                borderColor = eliteBorder,
                                isActivePressed = state.activePressedSlot == "0",
                                height = btnHeight,
                                onClick = { onTriggerSkill("0", state.selectedBuild.eliteName, "0", true) },
                                modifier = Modifier.weight(1.15f),
                                testTag = "skill_0"
                            )
                        }

                        // Row 3: SPECIAL (N) and ASSIST (T)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            TouchButton(
                                title = "SPECIAL",
                                subLabel = "Action (N)",
                                keyHint = "",
                                gradient = specialGrad,
                                borderColor = specialBorder,
                                isActivePressed = state.activePressedSlot == "special",
                                height = btnHeight,
                                onClick = { onTriggerSkill("special", "Special Action", "N", false) },
                                modifier = Modifier.weight(1.15f),
                                testTag = "skill_special"
                            )
                            TouchButton(
                                title = "ASSIST",
                                subLabel = "Call (T)",
                                keyHint = "",
                                gradient = assistGrad,
                                borderColor = assistBorder,
                                isActivePressed = state.activePressedSlot == "assist",
                                height = btnHeight,
                                onClick = { onTriggerSkill("assist", "Assist / Call", "T", false) },
                                modifier = Modifier.weight(1.15f),
                                testTag = "skill_assist"
                            )
                        }
                    }
                }
            }

            // Bottom Telemetry Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F1117))
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = state.lastActionFeedback,
                        color = state.theme.textAccent,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                    Text(text = "•", color = Gw2Themes.TextMuted, fontSize = 9.5.sp)
                    Text(
                        text = "Tyria: ${state.tyrianTime.phaseName} (${state.tyrianTime.minutesRemainingInPhase}m)",
                        color = Gw2Themes.WaypointYellow,
                        fontSize = 9.5.sp,
                        maxLines = 1
                    )
                    Text(text = "•", color = Gw2Themes.TextMuted, fontSize = 9.5.sp)
                    Text(
                        text = "Reset ${state.tyrianTime.nextResetHours}h ${state.tyrianTime.nextResetMinutes}m",
                        color = Gw2Themes.TextMuted,
                        fontSize = 9.5.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
