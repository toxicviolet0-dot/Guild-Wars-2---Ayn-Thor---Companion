package com.example.tyriahub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tyriahub.model.CharacterBag
import com.example.tyriahub.model.CharacterResponse
import com.example.tyriahub.model.FormattedCurrency
import com.example.tyriahub.model.Gw2ItemCatalog
import com.example.tyriahub.model.InventoryItem
import com.example.tyriahub.theme.Gw2Themes
import com.example.tyriahub.theme.LocalDeckSizeConfig
import com.example.tyriahub.ui.HubUiState

enum class InventoryFilter(val label: String, val icon: String) {
    ALL("All", "🎒"),
    EQUIPMENT("Gear", "⚔️"),
    CONSUMABLES("Food", "🍲"),
    MATERIALS("Materials", "💎"),
    UTILITY("Utility", "🛠️"),
    CONTAINERS("Chests", "📦"),
    JUNK("Junk", "🗑️")
}

/**
 * Guild Wars 2 Character Inventory & Bag Viewer.
 *
 * Engineered for handheld aspect ratios (e.g., AYN Thor):
 * - Uses [LocalDeckSizeConfig] to adapt slot sizing, typography, and default column counts.
 * - Renders item slots with chunked-row grids to prevent nested [LazyVerticalGrid] runtime crashes.
 * - Supports filtering (Equipment, Consumables, Materials, Utility, Containers, Junk), live text search,
 *   bag collapsing, and 1-tap item chat code broadcasting ([FastKeyInjector]).
 */
@Composable
fun InventoryView(
    state: HubUiState,
    onSelectCharacter: (CharacterResponse) -> Unit,
    onPasteItemToChat: (InventoryItem) -> Unit,
    onLoadExampleData: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val sizing = LocalDeckSizeConfig.current
    val character = state.selectedCharacter ?: state.characterList.firstOrNull()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(InventoryFilter.ALL) }
    var showEmptySlots by remember { mutableStateOf(true) }
    var viewByBags by remember { mutableStateOf(true) }
    var gridColumns by remember(sizing.inventoryColumns) { mutableStateOf(sizing.inventoryColumns) }
    var selectedItemDetail by remember { mutableStateOf<InventoryItem?>(null) }
    val collapsedBags = remember { mutableStateMapOf<Int, Boolean>() }

    if (character == null) {
        // Locked / empty state
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF090A0E))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF121520))
                    .border(1.dp, Gw2Themes.BorderSubtle, RoundedCornerShape(10.dp))
                    .padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Backpack,
                    contentDescription = "No Character",
                    tint = state.theme.primaryAccent,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = "Character Inventory Locked",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Connect a GW2 API key with the 'characters' and 'inventories' permissions or load test data to view your inventory bags.",
                    color = Gw2Themes.TextMuted,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
                if (onLoadExampleData != null) {
                    Button(
                        onClick = onLoadExampleData,
                        colors = ButtonDefaults.buttonColors(containerColor = state.theme.primaryAccent),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("🧪 Load Example Characters & Bags", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        return
    }

    val professionTheme = Gw2Themes.forProfession(character.profession)
    val bags = character.bags.filterNotNull()
    val allSlots = bags.flatMap { it.inventory }
    val filledSlots = allSlots.filterNotNull()
    val totalCapacity = bags.sumOf { it.size }
    val usedCapacity = filledSlots.size
    val freeCapacity = (totalCapacity - usedCapacity).coerceAtLeast(0)
    val usageRatio = if (totalCapacity > 0) usedCapacity.toFloat() / totalCapacity.toFloat() else 0f

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF090A0E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Character Switcher Ribbon
            if (state.characterList.size > 1) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(state.characterList) { char ->
                        val isSelected = char.name == character.name
                        val charTheme = Gw2Themes.forProfession(char.profession)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) charTheme.primaryAccent.copy(alpha = 0.25f) else Color(0xFF131622))
                                .border(
                                    1.dp,
                                    if (isSelected) charTheme.primaryAccent else Gw2Themes.BorderSubtle,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { onSelectCharacter(char) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = char.profession.take(3).uppercase(),
                                    color = charTheme.primaryAccent,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = char.name,
                                    color = if (isSelected) Color.White else Gw2Themes.TextMuted,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // Character Header & Capacity Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF131622))
                    .border(1.dp, professionTheme.primaryAccent.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🎒 ${character.name}",
                                color = Color.White,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(professionTheme.primaryAccent.copy(alpha = 0.2f))
                                    .border(0.5.dp, professionTheme.primaryAccent, RoundedCornerShape(3.dp))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "${character.race} • ${character.profession}",
                                    color = professionTheme.primaryAccent,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Text(
                            text = "$usedCapacity / $totalCapacity Slots ($freeCapacity Free)",
                            color = if (freeCapacity <= 5) Color(0xFFFF5277) else Gw2Themes.GoldAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Progress bar
                    LinearProgressIndicator(
                        progress = { usageRatio },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = if (usageRatio > 0.9f) Color(0xFFFF4D6D) else professionTheme.primaryAccent,
                        trackColor = Color(0xFF222636)
                    )
                }
            }

            // Search Bar & View Mode Toggles
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search inventory...", fontSize = 10.5.sp, color = Gw2Themes.TextMuted) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Gw2Themes.TextMuted, modifier = Modifier.size(14.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }, modifier = Modifier.size(20.dp)) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Gw2Themes.TextMuted, modifier = Modifier.size(12.dp))
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = professionTheme.primaryAccent,
                        unfocusedBorderColor = Gw2Themes.BorderSubtle,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = professionTheme.primaryAccent
                    ),
                    singleLine = true
                )

                // View Mode Toggle (By Bags vs All Slots Grid)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF141824))
                        .border(1.dp, Gw2Themes.BorderSubtle, RoundedCornerShape(6.dp))
                        .clickable { viewByBags = !viewByBags }
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (viewByBags) "🎒 By Bag" else "🔲 All Grid",
                        color = Gw2Themes.GoldAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Column density toggle for AYN Thor format (8 cols, 10 cols, 6 cols)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF141824))
                        .border(1.dp, Gw2Themes.BorderSubtle, RoundedCornerShape(6.dp))
                        .clickable {
                            gridColumns = when (gridColumns) {
                                8 -> 10
                                10 -> 6
                                else -> 8
                            }
                        }
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "📐 ${gridColumns} Col",
                        color = Gw2Themes.CyanAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Show / Hide empty slots
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (showEmptySlots) Color(0xFF1A2235) else Color(0xFF141824))
                        .border(
                            1.dp,
                            if (showEmptySlots) professionTheme.primaryAccent else Gw2Themes.BorderSubtle,
                            RoundedCornerShape(6.dp)
                        )
                        .clickable { showEmptySlots = !showEmptySlots }
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (showEmptySlots) "Hide Empty" else "Show Empty",
                        color = if (showEmptySlots) Color.White else Gw2Themes.TextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            // Category Filter Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                InventoryFilter.values().forEach { filter ->
                    val isSelected = selectedFilter == filter
                    val count = when (filter) {
                        InventoryFilter.ALL -> filledSlots.size
                        InventoryFilter.EQUIPMENT -> filledSlots.count { it.type == "Weapon" || it.type == "Armor" || it.type == "Trinket" }
                        InventoryFilter.CONSUMABLES -> filledSlots.count { it.type == "Consumable" }
                        InventoryFilter.MATERIALS -> filledSlots.count { it.type == "Material" }
                        InventoryFilter.UTILITY -> filledSlots.count { it.type == "Utility" || it.type == "Gizmo" }
                        InventoryFilter.CONTAINERS -> filledSlots.count { it.type == "Container" || it.type == "Bag" }
                        InventoryFilter.JUNK -> filledSlots.count { it.rarity == "Junk" || it.type == "Junk" }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isSelected) professionTheme.primaryAccent else Color(0xFF141824))
                            .border(
                                1.dp,
                                if (isSelected) professionTheme.primaryAccent else Gw2Themes.BorderSubtle,
                                RoundedCornerShape(4.dp)
                            )
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 7.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${filter.icon} ${filter.label} ($count)",
                            color = if (isSelected) Color.White else Gw2Themes.TextMuted,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            // Inventory Content Area
            if (viewByBags) {
                // Bags View with collapsible bag headers
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(bags.indices.toList()) { bagIndex ->
                        val bag = bags[bagIndex]
                        val isCollapsed = collapsedBags[bagIndex] == true
                        val bagItems = bag.inventory

                        // Filter items within this bag
                        val filteredItems = bagItems.filter { item ->
                            if (item == null) {
                                showEmptySlots && searchQuery.isBlank() && selectedFilter == InventoryFilter.ALL
                            } else {
                                matchesFilter(item, selectedFilter) && matchesSearch(item, searchQuery)
                            }
                        }

                        val bagFilledCount = bagItems.count { it != null }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF10131E))
                                .border(0.5.dp, Gw2Themes.BorderSubtle, RoundedCornerShape(6.dp))
                                .padding(4.dp)
                        ) {
                            // Bag Header
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { collapsedBags[bagIndex] = !isCollapsed }
                                    .padding(vertical = 4.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isCollapsed) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Toggle Bag",
                                        tint = Gw2Themes.TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = getBagName(bagIndex, bag.size),
                                        color = Color.White,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = "$bagFilledCount / ${bag.size} slots",
                                    color = Gw2Themes.TextMuted,
                                    fontSize = 10.5.sp
                                )
                            }

                            if (!isCollapsed) {
                                Spacer(modifier = Modifier.height(4.dp))
                                if (filteredItems.isEmpty()) {
                                    Text(
                                        text = "No matching items in this bag.",
                                        color = Gw2Themes.TextMuted,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                                    )
                                } else {
                                    BagItemsGrid(
                                        items = filteredItems,
                                        columns = gridColumns,
                                        onItemClick = { item -> selectedItemDetail = item }
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Unified continuous grid of all bags combined
                val filteredAll = allSlots.filter { item ->
                    if (item == null) {
                        showEmptySlots && searchQuery.isBlank() && selectedFilter == InventoryFilter.ALL
                    } else {
                        matchesFilter(item, selectedFilter) && matchesSearch(item, searchQuery)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF10131E))
                        .border(0.5.dp, Gw2Themes.BorderSubtle, RoundedCornerShape(6.dp))
                        .padding(6.dp)
                ) {
                    if (filteredAll.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "No items matched your search filter.",
                                color = Gw2Themes.TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    } else {
                        AllSlotsGrid(
                            items = filteredAll,
                            columns = gridColumns,
                            onItemClick = { item -> selectedItemDetail = item }
                        )
                    }
                }
            }
        }

        // Selected Item Detail Floating Sheet
        selectedItemDetail?.let { item ->
            ItemDetailSheet(
                item = item,
                onDismiss = { selectedItemDetail = null },
                onPasteToChat = {
                    onPasteItemToChat(item)
                    selectedItemDetail = null
                }
            )
        }
    }
}

/**
 * Renders inventory items within a bag header inside a LazyColumn.
 * Uses regular Rows with equal-weight columns to avoid nesting LazyVerticalGrid inside LazyColumn.
 * Default 8 columns makes items compact and prevents cutoff on handheld dual-screens like AYN Thor.
 */
@Composable
private fun BagItemsGrid(
    items: List<InventoryItem?>,
    columns: Int = 8,
    onItemClick: (InventoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val chunked = remember(items, columns) { items.chunked(columns) }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        chunked.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                rowItems.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        InventorySlotCell(item = item, onClick = { if (item != null) onItemClick(item) })
                    }
                }
                // Fill any remaining space in incomplete final rows so slots stay uniform width
                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

/**
 * Continuous grid for the unified "All Grid" view mode where there is no parent LazyColumn.
 */
@Composable
private fun AllSlotsGrid(
    items: List<InventoryItem?>,
    columns: Int = 8,
    onItemClick: (InventoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        contentPadding = PaddingValues(2.dp)
    ) {
        items(items) { item ->
            InventorySlotCell(item = item, onClick = { if (item != null) onItemClick(item) })
        }
    }
}

@Composable
private fun InventorySlotCell(
    item: InventoryItem?,
    onClick: () -> Unit
) {
    val sizing = LocalDeckSizeConfig.current
    val borderColor = if (item != null) Gw2ItemCatalog.colorForRarity(item.rarity) else Color(0xFF222636)
    val bgColor = if (item != null) Color(0xFF141724) else Color(0xFF0D0F17)

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(sizing.touchButtonCornerRadius * 0.7f))
            .background(bgColor)
            .border(
                width = if (item != null) 1.dp else 0.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(sizing.touchButtonCornerRadius * 0.7f)
            )
            .clickable(enabled = item != null, onClick = onClick)
            .padding(sizing.inventorySlotPadding),
        contentAlignment = Alignment.Center
    ) {
        if (item != null) {
            // Item Icon / Emoji scaled to active sizeMode
            Text(
                text = item.icon ?: "📦",
                fontSize = sizing.inventorySlotIconSp,
                textAlign = TextAlign.Center
            )

            // Top-left binding indicator
            if (item.binding != null) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Bound",
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier
                        .size(6.5.dp * sizing.iconScale)
                        .align(Alignment.TopStart)
                )
            }

            // Bottom-right count or charges badge
            if (item.count > 1) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.Black.copy(alpha = 0.88f))
                        .padding(horizontal = 1.5.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = "${item.count}",
                        color = Color.White,
                        fontSize = sizing.inventoryBadgeSp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (item.charges != null && item.charges > 1) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFF261D10).copy(alpha = 0.9f))
                        .padding(horizontal = 1.5.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = "⚡${item.charges}",
                        color = Gw2Themes.GoldAccent,
                        fontSize = sizing.inventoryBadgeSp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemDetailSheet(
    item: InventoryItem,
    onDismiss: () -> Unit,
    onPasteToChat: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val rarityColor = Gw2ItemCatalog.colorForRarity(item.rarity)
    var copiedToast by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .clickable(onClick = onDismiss)
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF141724))
                .border(2.dp, rarityColor, RoundedCornerShape(10.dp))
                .clickable(enabled = false) {} // Prevent clickthrough
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Top Header with Rarity Accent & Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = item.icon ?: "📦", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = item.name ?: "Unknown Item",
                                color = rarityColor,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = (item.rarity ?: "Basic").uppercase(),
                                    color = rarityColor,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(text = "•", color = Gw2Themes.TextMuted, fontSize = 9.sp)
                                Text(
                                    text = item.type ?: "Item",
                                    color = Gw2Themes.TextMuted,
                                    fontSize = 9.5.sp
                                )
                            }
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(26.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Gw2Themes.TextMuted, modifier = Modifier.size(16.dp))
                    }
                }

                // Stats & Quantities
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF0F121C))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Stack Count:", color = Gw2Themes.TextMuted, fontSize = 10.sp)
                        Text(text = "${item.count} items", color = Color.White, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
                    }

                    if (item.charges != null) {
                        Column {
                            Text(text = "Charges Left:", color = Gw2Themes.TextMuted, fontSize = 10.sp)
                            Text(text = "${item.charges} uses", color = Gw2Themes.GoldAccent, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Column {
                        Text(text = "Binding:", color = Gw2Themes.TextMuted, fontSize = 10.sp)
                        Text(
                            text = item.binding ?: "Tradable",
                            color = if (item.binding != null) Gw2Themes.CyanAccent else Gw2Themes.GreenAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Description / Lore
                if (!item.description.isNullOrBlank()) {
                    Text(
                        text = item.description,
                        color = Color(0xFFD4D8E2),
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF1B1F2D))
                            .padding(8.dp)
                    )
                }

                // Vendor Value in Gold/Silver/Copper
                if (item.vendorValue != null && item.vendorValue > 0) {
                    val curr = FormattedCurrency.fromCoins(item.vendorValue * item.count)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Vendor Value (Total):", color = Gw2Themes.TextMuted, fontSize = 10.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (curr.gold > 0) {
                                Text(text = "${curr.gold}g ", color = Gw2Themes.GoldAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            if (curr.silver > 0 || curr.gold > 0) {
                                Text(text = "${curr.silver}s ", color = Gw2Themes.SilverAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(text = "${curr.copper}c", color = Gw2Themes.CopperAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Chat Link Code
                item.chatCode?.let { code ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF0F121C))
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Chat Link: $code", color = Gw2Themes.TextMuted, fontSize = 10.sp, maxLines = 1)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFF1E2538))
                                .clickable {
                                    clipboardManager.setText(AnnotatedString(code))
                                    copiedToast = true
                                }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (copiedToast) "✓ Copied" else "Copy Link",
                                color = Gw2Themes.GoldAccent,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Send to Chat Macro
                    Button(
                        onClick = onPasteToChat,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = rarityColor),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Ping", tint = Color.Black, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "💬 Ping in Game Chat",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    // Copy name button
                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(item.name ?: ""))
                            copiedToast = true
                        },
                        border = androidx.compose.foundation.BorderStroke(1.dp, Gw2Themes.BorderSubtle),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy Name", tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy Name", color = Color.White, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

private fun matchesFilter(item: InventoryItem, filter: InventoryFilter): Boolean {
    return when (filter) {
        InventoryFilter.ALL -> true
        InventoryFilter.EQUIPMENT -> item.type == "Weapon" || item.type == "Armor" || item.type == "Trinket"
        InventoryFilter.CONSUMABLES -> item.type == "Consumable"
        InventoryFilter.MATERIALS -> item.type == "Material"
        InventoryFilter.UTILITY -> item.type == "Utility" || item.type == "Gizmo"
        InventoryFilter.CONTAINERS -> item.type == "Container" || item.type == "Bag"
        InventoryFilter.JUNK -> item.rarity == "Junk" || item.type == "Junk"
    }
}

private fun matchesSearch(item: InventoryItem, query: String): Boolean {
    if (query.isBlank()) return true
    val q = query.trim().lowercase()
    val name = item.name?.lowercase() ?: ""
    val type = item.type?.lowercase() ?: ""
    val rarity = item.rarity?.lowercase() ?: ""
    return name.contains(q) || type.contains(q) || rarity.contains(q)
}

private fun getBagName(index: Int, bagSize: Int): String {
    return when (index) {
        0 -> "🎒 Starter Backpack ($bagSize Slots)"
        1 -> "🧰 Safe Box ($bagSize Slots)"
        2 -> "🎒 Gossamer Bag ($bagSize Slots)"
        3 -> "🎒 Invisible Bag ($bagSize Slots)"
        4 -> "🎒 Olmakhan Leather Pack ($bagSize Slots)"
        else -> "🎒 Bag ${index + 1} ($bagSize Slots)"
    }
}
