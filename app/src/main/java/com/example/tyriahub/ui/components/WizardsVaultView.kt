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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tyriahub.theme.Gw2Themes
import com.example.tyriahub.ui.HubUiState

data class VaultObjective(
    val id: String,
    val title: String,
    val rewardAa: Int,
    val category: String
)

@Composable
fun WizardsVaultView(
    state: HubUiState,
    modifier: Modifier = Modifier
) {
    val completedIds = remember { mutableStateListOf<String>() }

    val dailyObjectives = remember {
        listOf(
            VaultObjective("d1", "Complete an Event or World Boss", 20, "Daily"),
            VaultObjective("d2", "Dodge 3 enemy attacks in combat", 10, "Daily"),
            VaultObjective("d3", "Salvage 10 pieces of equipment", 10, "Daily"),
            VaultObjective("d4", "View a Vista in any Tyrian zone", 10, "Daily"),
            VaultObjective("d5", "Defeat 10 enemies with weapon skills", 10, "Daily")
        )
    }

    val weeklyObjectives = remember {
        listOf(
            VaultObjective("w1", "Complete 10 Events across Tyria", 40, "Weekly"),
            VaultObjective("w2", "Defeat 100 enemies in HoT, PoF, or EoD", 40, "Weekly"),
            VaultObjective("w3", "Complete 1 Fractal of the Mists scale", 40, "Weekly"),
            VaultObjective("w4", "Defeat 1 Strike Mission or Raid Boss", 40, "Weekly")
        )
    }

    val completedDailyCount = dailyObjectives.count { completedIds.contains(it.id) }
    val isDailyMetaComplete = completedDailyCount >= 4

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0C10))
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Top Astral Acclaim Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF161A28))
                .border(1.dp, Gw2Themes.GoldAccent, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Vault",
                    tint = Gw2Themes.GoldAccent,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Wizard's Vault",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Daily & Weekly Objectives • Astral Acclaim Rewards",
                        color = Gw2Themes.TextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            // Astral Acclaim Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF261F12))
                    .border(1.dp, Gw2Themes.GoldAccent, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Stars,
                    contentDescription = "Astral Acclaim",
                    tint = Gw2Themes.GoldAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${state.astralAcclaim} AA",
                    color = Gw2Themes.GoldAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Daily Completion Meta Chest Progress
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF12141F))
                .border(1.dp, if (isDailyMetaComplete) Color(0xFF4ADE80) else Gw2Themes.BorderSubtle, RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daily Completionist Meta (${completedDailyCount.coerceAtMost(4)}/4 Completed)",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isDailyMetaComplete) "🎉 Meta Chest Claimed (+20 AA)" else "Reward: +20 AA & Large Chest",
                        color = if (isDailyMetaComplete) Color(0xFF4ADE80) else Gw2Themes.WaypointYellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                LinearProgressIndicator(
                    progress = { (completedDailyCount.coerceAtMost(4) / 4f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (isDailyMetaComplete) Color(0xFF4ADE80) else Gw2Themes.GoldAccent,
                    trackColor = Color(0xFF1F2436)
                )
            }
        }

        // Daily Tasks List
        Text(
            text = "Daily Objectives",
            color = state.theme.textAccent,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        dailyObjectives.forEach { obj ->
            val isChecked = completedIds.contains(obj.id)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isChecked) Color(0xFF141F1A) else Color(0xFF11141C))
                    .border(1.dp, if (isChecked) Color(0xFF22C55E) else Gw2Themes.BorderSubtle, RoundedCornerShape(6.dp))
                    .clickable {
                        if (isChecked) completedIds.remove(obj.id) else completedIds.add(obj.id)
                    }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            if (checked) completedIds.add(obj.id) else completedIds.remove(obj.id)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF22C55E),
                            uncheckedColor = Gw2Themes.TextMuted
                        ),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = obj.title,
                        color = if (isChecked) Color(0xFFCBD5E1) else Color.White,
                        fontSize = 11.5.sp,
                        fontWeight = if (isChecked) FontWeight.Normal else FontWeight.Medium
                    )
                }

                Text(
                    text = "+${obj.rewardAa} AA",
                    color = Gw2Themes.GoldAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Weekly Tasks List
        Text(
            text = "Weekly Objectives",
            color = Gw2Themes.WaypointYellow,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        weeklyObjectives.forEach { obj ->
            val isChecked = completedIds.contains(obj.id)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isChecked) Color(0xFF141F1A) else Color(0xFF11141C))
                    .border(1.dp, if (isChecked) Color(0xFF22C55E) else Gw2Themes.BorderSubtle, RoundedCornerShape(6.dp))
                    .clickable {
                        if (isChecked) completedIds.remove(obj.id) else completedIds.add(obj.id)
                    }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            if (checked) completedIds.add(obj.id) else completedIds.remove(obj.id)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF22C55E),
                            uncheckedColor = Gw2Themes.TextMuted
                        ),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = obj.title,
                        color = if (isChecked) Color(0xFFCBD5E1) else Color.White,
                        fontSize = 11.5.sp,
                        fontWeight = if (isChecked) FontWeight.Normal else FontWeight.Medium
                    )
                }

                Text(
                    text = "+${obj.rewardAa} AA",
                    color = Gw2Themes.GoldAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
