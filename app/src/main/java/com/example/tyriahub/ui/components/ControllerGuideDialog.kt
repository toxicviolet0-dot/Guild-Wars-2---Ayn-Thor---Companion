package com.example.tyriahub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

@Composable
fun ControllerGuideDialog(
    onDismiss: () -> Unit
) {
    TyriaModalOverlay(
        onDismissRequest = onDismiss,
        modifier = Modifier.padding(16.dp),
        zIndex = 200f
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 660.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0F121A))
                .border(1.dp, Gw2Themes.BorderSubtle, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Gamepad,
                        contentDescription = "Gamepad",
                        tint = Gw2Themes.GoldAccent,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AYN Thor Controller Mapping Guide",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Gw2Themes.TextMuted, modifier = Modifier.size(18.dp))
                }
            }

            Text(
                text = "Recommended physical stick and trigger bindings for Guild Wars 2 with DroidDeck so your bottom touch deck handles profession skills seamlessly:",
                color = Gw2Themes.TextMuted,
                fontSize = 11.sp
            )

            // Mapping Grid
            val mappings = listOf(
                "Left Thumbstick" to "W / S / Q / E (Smooth Strafe Movement)",
                "Right Thumbstick" to "Mouse Freelook (Camera Orbit)",
                "R3 (Right Stick Click)" to "Toggle Action Camera Mode (Lock crosshair)",
                "L3 (Left Stick Click)" to "Sprint / About-Face",
                "L1 (Left Bumper)" to "Target Nearest Enemy (TAB)",
                "R1 (Right Bumper)" to "Evade Roll / Dodge (V)",
                "L2 (Left Trigger)" to "Weapon Skill 1 / Auto-Attack",
                "R2 (Right Trigger)" to "Interact / Loot / F-Key",
                "D-Pad Up" to "Special Action Key (N)",
                "D-Pad Down" to "Mount Action / Skyscale (Y)",
                "D-Pad Left / Right" to "Weapon Swap (~) / Call Target (T)",
                "Face Buttons (A / B / X / Y)" to "Jump (Space) / Dodge (V) / Stomp / Stow (X)"
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                mappings.forEach { (control, mapping) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF141724))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = control,
                            color = Gw2Themes.GoldAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = mapping,
                            color = Color.White,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Direct IME Soft Keyboard Integration
            val context = androidx.compose.ui.platform.LocalContext.current
            androidx.compose.material3.Button(
                onClick = {
                    try {
                        val intent = android.content.Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS)
                        context.startActivity(intent)
                    } catch (_: Exception) {}
                },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Gw2Themes.Guardian.primaryAccent
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(42.dp)
            ) {
                Text(
                    text = "⚙️ Enable Tyria Hub as Android Keyboard (IME)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
            Text(
                text = "When enabled as an Android Keyboard, tapping skills injects hardware keystrokes (F1-F5, 1-0, V, TAB, ~) directly into DroidDeck without needing any daemon or bridge.",
                color = Gw2Themes.TextMuted,
                fontSize = 10.sp
            )
        }
    }
}
}
