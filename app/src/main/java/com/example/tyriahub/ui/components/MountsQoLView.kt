package com.example.tyriahub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tyriahub.theme.Gw2Themes
import com.example.tyriahub.ui.HubUiState

@Composable
fun MountsQoLView(
    state: HubUiState,
    onTriggerSkill: (slotName: String, actionLabel: String, keybind: String, isHeavy: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val btnHeight: Dp = when (state.sizeMode) {
        "XS" -> 46.dp
        "LG" -> 64.dp
        else -> 54.dp
    }

    val mountGrad = Brush.verticalGradient(listOf(Color(0xFF382B14), Color(0xFF1F170B)))
    val mountBorder = Color(0xFF7A5C2A)

    val qolGrad = Brush.verticalGradient(listOf(Color(0xFF1B2333), Color(0xFF10151F)))
    val qolBorder = Color(0xFF2E3D59)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0C10))
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "🦅 Guild Wars 2 Mounts Quick-Deploy",
            color = Gw2Themes.GoldAccent,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        // Row 1: Skyscale, Griffon, Roller Beetle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TouchButton(
                title = "SKYSCALE",
                subLabel = "Wall Latch & Climb",
                keyHint = "Y",
                gradient = mountGrad,
                borderColor = mountBorder,
                isActivePressed = state.activePressedSlot == "mount_skyscale",
                height = btnHeight,
                onClick = { onTriggerSkill("mount_skyscale", "Deploy Skyscale", "Y", true) },
                modifier = Modifier.weight(1f)
            )
            TouchButton(
                title = "GRIFFON",
                subLabel = "High-Speed Dive",
                keyHint = "G",
                gradient = mountGrad,
                borderColor = mountBorder,
                isActivePressed = state.activePressedSlot == "mount_griffon",
                height = btnHeight,
                onClick = { onTriggerSkill("mount_griffon", "Deploy Griffon", "G", true) },
                modifier = Modifier.weight(1f)
            )
            TouchButton(
                title = "ROLLER BEETLE",
                subLabel = "Supersonic Drift",
                keyHint = "B",
                gradient = mountGrad,
                borderColor = mountBorder,
                isActivePressed = state.activePressedSlot == "mount_beetle",
                height = btnHeight,
                onClick = { onTriggerSkill("mount_beetle", "Deploy Roller Beetle", "B", true) },
                modifier = Modifier.weight(1f)
            )
        }

        // Row 2: Raptor, Springer, Skimmer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TouchButton(
                title = "RAPTOR",
                subLabel = "Canyon Leap",
                keyHint = "X",
                gradient = mountGrad,
                borderColor = mountBorder,
                isActivePressed = state.activePressedSlot == "mount_raptor",
                height = btnHeight,
                onClick = { onTriggerSkill("mount_raptor", "Deploy Raptor", "X", true) },
                modifier = Modifier.weight(1f)
            )
            TouchButton(
                title = "SPRINGER",
                subLabel = "High Vertical Vault",
                keyHint = "SHIFT+X",
                gradient = mountGrad,
                borderColor = mountBorder,
                isActivePressed = state.activePressedSlot == "mount_springer",
                height = btnHeight,
                onClick = { onTriggerSkill("mount_springer", "Deploy Springer", "SHIFT+X", true) },
                modifier = Modifier.weight(1f)
            )
            TouchButton(
                title = "SKIMMER",
                subLabel = "Hover & Diving",
                keyHint = "CTRL+X",
                gradient = mountGrad,
                borderColor = mountBorder,
                isActivePressed = state.activePressedSlot == "mount_skimmer",
                height = btnHeight,
                onClick = { onTriggerSkill("mount_skimmer", "Deploy Skimmer", "CTRL+X", true) },
                modifier = Modifier.weight(1f)
            )
        }

        // Row 3: Jackal, Warclaw, Siege Turtle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TouchButton(
                title = "JACKAL",
                subLabel = "Sand Portals",
                keyHint = "ALT+X",
                gradient = mountGrad,
                borderColor = mountBorder,
                isActivePressed = state.activePressedSlot == "mount_jackal",
                height = btnHeight,
                onClick = { onTriggerSkill("mount_jackal", "Deploy Jackal", "ALT+X", true) },
                modifier = Modifier.weight(1f)
            )
            TouchButton(
                title = "WARCLAW",
                subLabel = "Battle Mount / Sniff",
                keyHint = "W",
                gradient = mountGrad,
                borderColor = mountBorder,
                isActivePressed = state.activePressedSlot == "mount_warclaw",
                height = btnHeight,
                onClick = { onTriggerSkill("mount_warclaw", "Deploy Warclaw", "W", true) },
                modifier = Modifier.weight(1f)
            )
            TouchButton(
                title = "SIEGE TURTLE",
                subLabel = "2-Player Cannons",
                keyHint = "T",
                gradient = mountGrad,
                borderColor = mountBorder,
                isActivePressed = state.activePressedSlot == "mount_turtle",
                height = btnHeight,
                onClick = { onTriggerSkill("mount_turtle", "Deploy Siege Turtle", "T", true) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "⚡ In-Game Panels & Quality of Life",
            color = state.theme.textAccent,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        // QoL Row 1: Map, Inventory, Hero Panel, Trading Post
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TouchButton(
                title = "WORLD MAP",
                subLabel = "Open Tyria Map",
                keyHint = "M",
                gradient = qolGrad,
                borderColor = qolBorder,
                isActivePressed = state.activePressedSlot == "qol_map",
                height = btnHeight,
                onClick = { onTriggerSkill("qol_map", "Open Map", "M", false) },
                modifier = Modifier.weight(1f)
            )
            TouchButton(
                title = "INVENTORY",
                subLabel = "Bags & Bank",
                keyHint = "I",
                gradient = qolGrad,
                borderColor = qolBorder,
                isActivePressed = state.activePressedSlot == "qol_inventory",
                height = btnHeight,
                onClick = { onTriggerSkill("qol_inventory", "Open Inventory", "I", false) },
                modifier = Modifier.weight(1f)
            )
            TouchButton(
                title = "HERO PANEL",
                subLabel = "Equipment & Build",
                keyHint = "H",
                gradient = qolGrad,
                borderColor = qolBorder,
                isActivePressed = state.activePressedSlot == "qol_hero",
                height = btnHeight,
                onClick = { onTriggerSkill("qol_hero", "Open Hero Panel", "H", false) },
                modifier = Modifier.weight(1f)
            )
            TouchButton(
                title = "TRADING POST",
                subLabel = "Black Lion Market",
                keyHint = "O",
                gradient = qolGrad,
                borderColor = qolBorder,
                isActivePressed = state.activePressedSlot == "qol_tp",
                height = btnHeight,
                onClick = { onTriggerSkill("qol_tp", "Open Trading Post", "O", false) },
                modifier = Modifier.weight(1f)
            )
        }

        // QoL Row 2: Guild, LFG & Friends, Stow Weapon, Fishing
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TouchButton(
                title = "GUILD",
                subLabel = "Guild Roster",
                keyHint = "G",
                gradient = qolGrad,
                borderColor = qolBorder,
                isActivePressed = state.activePressedSlot == "qol_guild",
                height = btnHeight,
                onClick = { onTriggerSkill("qol_guild", "Open Guild Panel", "G", false) },
                modifier = Modifier.weight(1f)
            )
            TouchButton(
                title = "LFG & FRIENDS",
                subLabel = "Party Finder",
                keyHint = "Y",
                gradient = qolGrad,
                borderColor = qolBorder,
                isActivePressed = state.activePressedSlot == "qol_lfg",
                height = btnHeight,
                onClick = { onTriggerSkill("qol_lfg", "Open LFG Panel", "Y", false) },
                modifier = Modifier.weight(1f)
            )
            TouchButton(
                title = "STOW WEAPON",
                subLabel = "Sheathe / Draw",
                keyHint = "X",
                gradient = qolGrad,
                borderColor = qolBorder,
                isActivePressed = state.activePressedSlot == "qol_stow",
                height = btnHeight,
                onClick = { onTriggerSkill("qol_stow", "Stow/Draw Weapon", "X", false) },
                modifier = Modifier.weight(1f)
            )
            TouchButton(
                title = "FISHING",
                subLabel = "Cast Line",
                keyHint = "J",
                gradient = qolGrad,
                borderColor = qolBorder,
                isActivePressed = state.activePressedSlot == "qol_fish",
                height = btnHeight,
                onClick = { onTriggerSkill("qol_fish", "Cast Fishing Line", "J", false) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
