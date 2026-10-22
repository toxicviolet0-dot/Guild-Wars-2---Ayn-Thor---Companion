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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tyriahub.model.ActiveBossStatus
import com.example.tyriahub.theme.Gw2Themes
import com.example.tyriahub.theme.LocalDeckSizeConfig

@Composable
fun LiveTickerBar(
    liveBoss: ActiveBossStatus?,
    onPasteWaypointToChat: ((String) -> Unit)? = null,
    onCopyWaypoint: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val sizing = LocalDeckSizeConfig.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(sizing.tickerHeight)
            .background(Color(0xFF0A0C11))
            .border(width = 0.5.dp, color = Color(0xFF1E2230))
            .padding(horizontal = sizing.navTabPaddingH * 0.8f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (liveBoss != null && liveBoss.isLive) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF3B5C))
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "LIVE: ",
                    color = Color(0xFFFF4D6D),
                    fontWeight = FontWeight.Bold,
                    fontSize = sizing.tickerFontSp
                )
                Text(
                    text = "${liveBoss.definition.name} ",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = sizing.tickerFontSp
                )
                Text(
                    text = "(${liveBoss.minutesLeft}m left)",
                    color = Gw2Themes.TextMuted,
                    fontSize = sizing.tickerFontSp * 0.9f
                )
            } else if (liveBoss != null) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Gw2Themes.WaypointYellow)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "UPCOMING: ",
                    color = Gw2Themes.WaypointYellow,
                    fontWeight = FontWeight.Bold,
                    fontSize = sizing.tickerFontSp
                )
                Text(
                    text = "${liveBoss.definition.name} ",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = sizing.tickerFontSp
                )
                Text(
                    text = "(in ${liveBoss.minutesUntil}m at ${liveBoss.definition.zone})",
                    color = Gw2Themes.TextMuted,
                    fontSize = sizing.tickerFontSp * 0.9f
                )
            } else {
                Text(
                    text = "Tyria World Event Tracker Active",
                    color = Gw2Themes.TextMuted,
                    fontSize = sizing.tickerFontSp
                )
            }
        }

        if (liveBoss != null && onPasteWaypointToChat != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF1A2B20))
                    .border(0.5.dp, Gw2Themes.GreenAccent.copy(alpha = 0.7f), RoundedCornerShape(3.dp))
                    .clickable { onPasteWaypointToChat(liveBoss.definition.waypointCode) }
                    .padding(horizontal = sizing.tickerBtnPaddingH, vertical = 2.5.dp)
            ) {
                Text(
                    text = "💬 Paste to Chat",
                    color = Gw2Themes.GreenAccent,
                    fontSize = sizing.tickerFontSp * 0.92f,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
