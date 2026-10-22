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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.tyriahub.model.CharacterResponse
import com.example.tyriahub.model.FormattedCurrency
import com.example.tyriahub.theme.Gw2Themes
import com.example.tyriahub.ui.HubUiState

@Composable
fun DailiesView(
    state: HubUiState,
    onConnectApiKey: (String) -> Unit,
    onSelectCharacter: (CharacterResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    var apiKeyInput by remember(state.apiKey) { mutableStateOf(state.apiKey) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0C10))
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val isAccountUnlocked = state.isExampleData || (state.apiKey.isNotBlank() && state.tokenInfo != null)
        if (!isAccountUnlocked) {
            // Locked State - API Key required per user requirement
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF12151F))
                    .border(1.dp, Gw2Themes.BorderSubtle, RoundedCornerShape(10.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "API Locked",
                        tint = Gw2Themes.GoldAccent,
                        modifier = Modifier.size(36.dp)
                    )
                    Text(
                        text = "Guild Wars 2 Account & Characters Locked",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "To inspect your characters, wallet, and gear, and to automatically sync your Touch Deck skills to your active character, enter your official Guild Wars 2 API Key below.",
                        color = Gw2Themes.TextMuted,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    OutlinedTextField(
                        value = apiKeyInput,
                        onValueChange = { apiKeyInput = it },
                        placeholder = { Text("XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXXXXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", fontSize = 11.sp, color = Gw2Themes.TextMuted) },
                        leadingIcon = {
                            Icon(Icons.Default.Key, contentDescription = "API Key", tint = Gw2Themes.GoldAccent, modifier = Modifier.size(16.dp))
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = state.theme.primaryAccent,
                            unfocusedBorderColor = Gw2Themes.BorderSubtle,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    if (state.apiErrorMessage != null && !state.isExampleData) {
                        Text(
                            text = state.apiErrorMessage,
                            color = Gw2Themes.DangerRed,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = { onConnectApiKey(apiKeyInput) },
                        enabled = apiKeyInput.isNotBlank() && !state.isApiLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = state.theme.primaryAccent
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(44.dp)
                    ) {
                        if (state.isApiLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text("Connect Account", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Text(
                        text = "Recommended Scopes: account, characters, inventories, builds. Stored locally.",
                        color = Gw2Themes.TextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        } else {
            // Connected Account State
            val coins = FormattedCurrency.fromCoins(state.walletCoins)

            // Header Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF131722))
                    .border(1.dp, state.theme.primaryAccent, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Account",
                        tint = state.theme.primaryAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = state.account?.name ?: state.tokenInfo?.name ?: "GW2 Account",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Token: ${state.tokenInfo?.name ?: "Active"} • Permissions: ${state.tokenInfo?.permissions?.joinToString(", ") ?: "All"}",
                            color = Gw2Themes.TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                // Currency summary
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${coins.gold}g ", color = Color(0xFFFFD175), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("${coins.silver}s ", color = Color(0xFFE2E8F0), fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        Text("${coins.copper}c", color = Color(0xFFD97706), fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }
                    Text("•", color = Gw2Themes.TextMuted, fontSize = 10.sp)
                    Text("Karma: ${state.karma}", color = Color(0xFFC084FC), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    Text("•", color = Gw2Themes.TextMuted, fontSize = 10.sp)
                    Text("Laurels: ${state.laurels}", color = Color(0xFF4ADE80), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
            }

            // Characters Section
            Text(
                text = "Select Character to Sync Touch Deck:",
                color = state.theme.textAccent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            if (state.characterList.isEmpty()) {
                Text(
                    text = "No characters found on this account.",
                    color = Gw2Themes.TextMuted,
                    fontSize = 12.sp
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(state.characterList) { character ->
                        val isSelected = state.selectedCharacter?.name == character.name
                        val charTheme = Gw2Themes.forProfession(character.profession)

                        Box(
                            modifier = Modifier
                                .width(170.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) charTheme.surfaceColor else Color(0xFF12141C))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) charTheme.primaryAccent else Gw2Themes.BorderSubtle,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { onSelectCharacter(character) }
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = character.name,
                                        color = if (isSelected) Color.White else Gw2Themes.TextBright,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        maxLines = 1
                                    )
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Active",
                                            tint = charTheme.primaryAccent,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Lvl ${character.level} ${character.profession}",
                                    color = charTheme.textAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${character.race} ${character.gender}",
                                    color = Gw2Themes.TextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }

            // Equipment sheet for active character
            state.selectedCharacter?.let { activeChar ->
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${activeChar.name}'s Equipment Gear (${activeChar.equipment.size} items)",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Touch Deck Active Profile: ${activeChar.profession}",
                        color = state.theme.textAccent,
                        fontSize = 11.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val gearSlots = activeChar.equipment.take(8)
                    gearSlots.forEach { item ->
                        val slotName = item.slot.replace("Helm", "Head").replace("Boots", "Feet")
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF141724))
                                .border(1.dp, state.theme.primaryAccent.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = slotName,
                                    tint = state.theme.textAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = slotName,
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
