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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Tv
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
import com.example.tyriahub.overlay.DisplayItemInfo
import com.example.tyriahub.theme.Gw2Themes

@Composable
fun DisplayPickerDialog(
    displays: List<DisplayItemInfo>,
    selectedDisplayId: Int,
    onSelectDisplay: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    TyriaModalOverlay(
        onDismissRequest = onDismiss,
        modifier = Modifier.padding(16.dp),
        zIndex = 250f
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 520.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF131620))
                .border(1.5.dp, Gw2Themes.GoldAccent, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Smartphone,
                        contentDescription = "Screen Picker",
                        tint = Gw2Themes.GoldAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SELECT OVERLAY DISPLAY",
                        color = Gw2Themes.GoldAccent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Gw2Themes.TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Target which screen the non-focusable touch deck appears on. For the AYN Thor handheld, select the 3.92\" Bottom Screen so DroidDeck/GW2 can remain undisturbed on the 6\" Top Screen.",
                color = Gw2Themes.TextMuted,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Auto Option
            val isAutoSelected = selectedDisplayId == -1
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isAutoSelected) Color(0xFF1E2638) else Color(0xFF181B26))
                    .border(
                        1.dp,
                        if (isAutoSelected) Gw2Themes.CyanAccent else Gw2Themes.BorderSubtle,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        onSelectDisplay(-1)
                        onDismiss()
                    }
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "⚡ Auto-Detect Bottom Screen (Recommended)",
                        color = if (isAutoSelected) Gw2Themes.CyanAccent else Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Automatically targets the smaller 3.92\" secondary display (1080x1240)",
                        color = Gw2Themes.TextMuted,
                        fontSize = 10.sp
                    )
                }

                if (isAutoSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Gw2Themes.CyanAccent,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // List of detected displays
            displays.forEach { info ->
                val isSelected = selectedDisplayId == info.displayId
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Color(0xFF282318) else Color(0xFF181B26))
                        .border(
                            1.dp,
                            if (isSelected) Gw2Themes.GoldAccent else Gw2Themes.BorderSubtle,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            onSelectDisplay(info.displayId)
                            onDismiss()
                        }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (info.isBottomScreen) Icons.Default.Smartphone else Icons.Default.Tv,
                            contentDescription = info.name,
                            tint = if (info.isBottomScreen) Gw2Themes.GoldAccent else Gw2Themes.CyanAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = info.label,
                                    color = if (isSelected) Gw2Themes.GoldAccent else Color.White,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (info.isBottomScreen) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(Color(0xFF382A13))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "THOR BOTTOM",
                                            color = Gw2Themes.GoldAccent,
                                            fontSize = 8.5.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Resolution: ${info.width} × ${info.height}  •  ${if (info.isDefault) "Default Primary Display" else "Secondary Display"}",
                                color = Gw2Themes.TextMuted,
                                fontSize = 9.5.sp
                            )
                        }
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = Gw2Themes.GoldAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
