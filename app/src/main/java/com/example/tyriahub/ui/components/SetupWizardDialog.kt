package com.example.tyriahub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.ViewCompact
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tyriahub.haptics.HapticsManager
import com.example.tyriahub.theme.Gw2Themes

import androidx.compose.foundation.layout.Box

@Composable
fun SetupWizardDialog(
    initialApiKey: String,
    initialSizeMode: String,
    initialHaptics: Boolean,
    haptics: HapticsManager,
    onComplete: (apiKey: String, sizeMode: String, hapticsEnabled: Boolean) -> Unit
) {
    var sizeMode by remember { mutableStateOf(initialSizeMode) }
    var hapticsEnabled by remember { mutableStateOf(initialHaptics) }
    var apiKey by remember { mutableStateOf(initialApiKey) }

    TyriaModalOverlay(
        onDismissRequest = { /* Modal must be completed or finished */ },
        modifier = Modifier.padding(16.dp),
        zIndex = 300f
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 660.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0F121A))
                .border(1.dp, Gw2Themes.GoldAccent, RoundedCornerShape(12.dp))
                .padding(18.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Smartphone,
                    contentDescription = "Setup",
                    tint = Gw2Themes.GoldAccent,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = "Tyria Hub Setup & Calibration",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Configuring for AYN Thor dual-screen landscape deck",
                        color = Gw2Themes.TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            // Step 1: Touch Deck Sizing
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF141724))
                    .border(1.dp, Gw2Themes.BorderSubtle, RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ViewCompact, contentDescription = "Size", tint = Gw2Themes.GoldAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "1. Bottom Screen Button Sizing", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "Choose the height and spacing for thumb reach on the Thor's lower display:",
                        color = Gw2Themes.TextMuted,
                        fontSize = 11.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("XS" to "XS (Compact)", "MD" to "MD (Recommended)", "LG" to "LG (Large)").forEach { (key, label) ->
                            val isSelected = sizeMode == key
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) Gw2Themes.Guardian.primaryAccent else Color(0xFF1D2233))
                                    .border(1.dp, if (isSelected) Color.White else Gw2Themes.BorderSubtle, RoundedCornerShape(6.dp))
                                    .clickable {
                                        sizeMode = key
                                        haptics.playSkillTap()
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) Color.White else Gw2Themes.TextBright,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            // Step 2: Native Android Haptics
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF141724))
                    .border(1.dp, Gw2Themes.BorderSubtle, RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Vibration, contentDescription = "Haptics", tint = Gw2Themes.GoldAccent, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "2. Native Android Vibration Haptics", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Switch(
                            checked = hapticsEnabled,
                            onCheckedChange = {
                                hapticsEnabled = it
                                haptics.isEnabled = it
                                if (it) haptics.playSkillTap()
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Gw2Themes.Guardian.primaryAccent
                            )
                        )
                    }
                    Text(
                        text = "Native VibrationEffect provides physical button click simulation for every skill tap:",
                        color = Gw2Themes.TextMuted,
                        fontSize = 11.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { haptics.playSkillTap() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF232A3D)),
                            modifier = Modifier.weight(1f).height(36.dp)
                        ) {
                            Text("Test Skill Click", fontSize = 11.sp, color = Color.White)
                        }
                        Button(
                            onClick = { haptics.playHeavyTap() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF232A3D)),
                            modifier = Modifier.weight(1f).height(36.dp)
                        ) {
                            Text("Test Heavy Elite", fontSize = 11.sp, color = Color.White)
                        }
                        Button(
                            onClick = { haptics.playDodgePulse() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF232A3D)),
                            modifier = Modifier.weight(1f).height(36.dp)
                        ) {
                            Text("Test Dodge Pulse", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }

            // Step 3: Optional GW2 API Key
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF141724))
                    .border(1.dp, Gw2Themes.BorderSubtle, RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Key, contentDescription = "API", tint = Gw2Themes.GoldAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "3. Guild Wars 2 API Key (Optional)", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "Leave blank to start with basic Core/Dragonhunter templates, or paste your API key to fetch your real account characters and wallet.",
                        color = Gw2Themes.TextMuted,
                        fontSize = 11.sp
                    )
                    OutlinedTextField(
                        value = apiKey,
                        onValueChange = { apiKey = it },
                        placeholder = { Text("Enter API key or leave blank...", fontSize = 11.sp, color = Gw2Themes.TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Gw2Themes.GoldAccent,
                            unfocusedBorderColor = Gw2Themes.BorderSubtle,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(6.dp)
                    )
                }
            }

            // Finish Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onComplete("", sizeMode, hapticsEnabled)
                    },
                    modifier = Modifier.weight(1f).height(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Gw2Themes.TextMuted)
                ) {
                    Text("Skip API", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = {
                        onComplete("", sizeMode, hapticsEnabled)
                    },
                    modifier = Modifier.weight(1.3f).height(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Gw2Themes.CyanAccent),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Gw2Themes.CyanAccent)
                ) {
                    Text("🧪 Test Data", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        onComplete(apiKey, sizeMode, hapticsEnabled)
                    },
                    modifier = Modifier.weight(1.4f).height(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gw2Themes.GoldAccent)
                ) {
                    Text("Launch Deck", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}
}
