package com.example.tyriahub.ui.components

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import android.app.Activity
import com.example.tyriahub.model.PresetBuilds
import com.example.tyriahub.model.ProfessionBuild
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainActivity
import com.example.tyriahub.ime.TyriaHubInputMethodService
import com.example.tyriahub.overlay.HandheldDisplayManager
import com.example.tyriahub.overlay.TyriaOverlayService
import com.example.tyriahub.theme.Gw2Themes
import com.example.tyriahub.ui.DeckSubMode
import com.example.tyriahub.ui.HubUiState
import com.example.tyriahub.ui.MainViewModel

private enum class SettingsCategory(val title: String, val icon: String) {
    GW2_API("GW2 API", "🔑"),
    DISPLAY("Display & Overlay", "📱"),
    TOUCH_DECK("Touch Deck", "🎮"),
    INPUT("Input & Macros", "⚡"),
    APPEARANCE("Appearance", "🎨"),
    SYSTEM("System & Diagnostics", "🛠️")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsDialog(
    state: HubUiState,
    viewModel: MainViewModel,
    onDismiss: () -> Unit,
    onExitOverlay: (() -> Unit)? = null,
    onSwitchToWindowMode: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(SettingsCategory.GW2_API) }

    TyriaModalOverlay(
        onDismissRequest = onDismiss,
        scrimColor = Color.Black.copy(alpha = 0.85f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.96f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (state.amoledBlack) Color.Black else Color(0xFF0F1118))
                    .border(1.5.dp, state.theme.primaryAccent, RoundedCornerShape(12.dp))
            ) {
                // Top Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF161924))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = state.theme.primaryAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Tyria Hub Configuration",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "AYN Thor Dual-Screen & Gaming Settings",
                                color = Gw2Themes.TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Reset defaults button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF261D15))
                                .border(0.5.dp, Gw2Themes.GoldAccent.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                                .clickable { viewModel.resetToDefaults() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "↺ Defaults",
                                color = Gw2Themes.GoldAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Close button
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Category Selector Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF12141F))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SettingsCategory.values().forEach { cat ->
                        val isSelected = selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) state.theme.primaryAccent else Color(0xFF1A1D2A))
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) state.theme.primaryAccent else Gw2Themes.BorderSubtle,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    viewModel.haptics.playSkillTap()
                                    selectedCategory = cat
                                }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${cat.icon} ${cat.title}",
                                color = if (isSelected) Color.White else Gw2Themes.TextMuted,
                                fontSize = 10.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1
                            )
                        }
                    }
                }

                // Scrollable Content Area for the selected category
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(12.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    when (selectedCategory) {
                        SettingsCategory.GW2_API -> {
                            Gw2ApiConfigSection(state = state, viewModel = viewModel)
                        }
                        SettingsCategory.DISPLAY -> {
                            DisplayConfigSection(state = state, viewModel = viewModel, context = context)
                        }
                        SettingsCategory.TOUCH_DECK -> {
                            TouchDeckConfigSection(state = state, viewModel = viewModel)
                        }
                        SettingsCategory.INPUT -> {
                            InputConfigSection(state = state, viewModel = viewModel, context = context)
                        }
                        SettingsCategory.APPEARANCE -> {
                            AppearanceConfigSection(state = state, viewModel = viewModel)
                        }
                        SettingsCategory.SYSTEM -> {
                            SystemConfigSection(
                                state = state,
                                viewModel = viewModel,
                                context = context,
                                onExitOverlay = onExitOverlay,
                                onSwitchToWindowMode = onSwitchToWindowMode
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Gw2ApiConfigSection(
    state: HubUiState,
    viewModel: MainViewModel
) {
    val clipboardManager = LocalClipboardManager.current
    var inputKey by remember(state.apiKey, state.isExampleData) {
        mutableStateOf(if (state.isExampleData) "" else state.apiKey)
    }
    var hideKey by remember { mutableStateOf(true) }

    ConfigCard(title = "🔑 Guild Wars 2 ArenaNet API Key") {
        Text(
            text = "Your API key links Guild Wars 2 account progression, active character inventories, wallet currencies, and Wizard's Vault Astral Acclaim directly to Tyria Hub.",
            color = Gw2Themes.TextMuted,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = inputKey,
            onValueChange = { inputKey = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("ArenaNet API Key (72 characters)") },
            visualTransformation = if (hideKey) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { hideKey = !hideKey }) {
                        Icon(
                            imageVector = if (hideKey) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle Visibility",
                            tint = Gw2Themes.TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            val clip = clipboardManager.getText()?.text
                            if (!clip.isNullOrBlank()) {
                                inputKey = clip.trim()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentPaste,
                            contentDescription = "Paste",
                            tint = state.theme.primaryAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = state.theme.primaryAccent,
                unfocusedBorderColor = Gw2Themes.BorderSubtle,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val key = inputKey.trim()
                    if (key.isNotBlank()) {
                        if (key.startsWith("EXAMPLE", ignoreCase = true) || key.contains("TEST", ignoreCase = true)) {
                            viewModel.prefs.apiKey = ""
                            viewModel.loadExampleData(notify = true)
                        } else {
                            viewModel.prefs.apiKey = key
                            viewModel.fetchAccountData(key)
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = state.theme.primaryAccent),
                shape = RoundedCornerShape(6.dp),
                enabled = !state.isApiLoading
            ) {
                Text(
                    text = if (state.isApiLoading) "Validating..." else "💾 Save & Test API Key",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            if (state.apiKey.isNotBlank() || state.isExampleData) {
                Button(
                    onClick = {
                        inputKey = ""
                        viewModel.prefs.apiKey = ""
                        viewModel.clearExampleData()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B151E)),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Clear",
                        tint = Color(0xFFFF5277),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Clear", color = Color(0xFFFF5277), fontSize = 11.sp)
                }
            }
        }

        if (state.apiErrorMessage != null && !state.isExampleData) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF33161A))
                    .border(1.dp, Color(0xFFFF4D6D), RoundedCornerShape(4.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = Color(0xFFFF4D6D),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = state.apiErrorMessage,
                    color = Color(0xFFFF859B),
                    fontSize = 11.sp
                )
            }
        }
    }

    // Example / Demo Data for Testing
    ConfigCard(title = "🧪 Example Data for Testing") {
        Text(
            text = "Test dual-screen layout, 9 level 80 profession characters, full Ascended/Legendary equipment, and live currency tickers immediately without needing an ArenaNet API key.",
            color = Gw2Themes.TextMuted,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.isExampleData) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF132838))
                    .border(1.dp, Gw2Themes.CyanAccent, RoundedCornerShape(6.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "✓ Example Data Active: ${state.account?.name ?: "CommanderTyria.7492"}",
                        color = Gw2Themes.CyanAccent,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "9 Characters • 1,485 Gold • 28,450 AP • All Expansions",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 10.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = { viewModel.loadExampleData(notify = true) },
                        border = androidx.compose.foundation.BorderStroke(1.dp, Gw2Themes.CyanAccent),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Gw2Themes.CyanAccent),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Reload", fontSize = 10.sp)
                    }

                    Button(
                        onClick = { viewModel.clearExampleData() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B151E)),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Clear", color = Color(0xFFFF5277), fontSize = 10.sp)
                    }
                }
            }
        } else {
            Button(
                onClick = { viewModel.loadExampleData(notify = true) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF132838)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Gw2Themes.CyanAccent),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "🧪 Load Example Data for Testing",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gw2Themes.CyanAccent
                )
            }
        }
    }

    // Token Status & Permissions Audit
    ConfigCard(title = "🛡️ API Key Permissions Audit") {
        if (state.tokenInfo != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Key Label: ${state.tokenInfo.name.ifBlank { "ArenaNet Token" }}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                if (state.account != null) {
                    Text(
                        text = "Account: ${state.account.name}",
                        color = Gw2Themes.GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val corePerms = listOf("account", "characters", "inventories", "wallet", "builds", "progression", "unlocks")
            val tokenPerms = state.tokenInfo.permissions.map { it.lowercase() }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                corePerms.forEach { perm ->
                    val hasPerm = tokenPerms.contains(perm)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (hasPerm) Color(0xFF143021) else Color(0xFF302012))
                            .border(
                                0.5.dp,
                                if (hasPerm) Gw2Themes.GreenAccent else Gw2Themes.GoldAccent,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (hasPerm) "✓" else "!",
                                color = if (hasPerm) Gw2Themes.GreenAccent else Gw2Themes.GoldAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = perm,
                                color = if (hasPerm) Color.White else Gw2Themes.TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        } else {
            Text(
                text = "No validated API key currently active. Enter and save your key above to view permissions audit.",
                color = Gw2Themes.TextMuted,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun DisplayConfigSection(
    state: HubUiState,
    viewModel: MainViewModel,
    context: Context
) {
    val allDisplays = remember { HandheldDisplayManager.getAllDisplays(context) }
    val displayInfos = remember { HandheldDisplayManager.getDisplayListInfo(context) }

    ConfigCard(title = "📱 AYN Thor Dual-Screen Target Display") {
        Text(
            text = "Choose which physical display the floating non-focusable overlay should render on. The AYN Thor bottom screen is 3.92\" 1080x1240.",
            color = Gw2Themes.TextMuted,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Display selection buttons
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            displayInfos.forEach { info ->
                val isSelected = state.targetOverlayDisplayId == info.displayId ||
                        (state.targetOverlayDisplayId == -1 && info.isBottomScreen)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) Color(0xFF162536) else Color(0xFF141722))
                        .border(
                            1.dp,
                            if (isSelected) Gw2Themes.CyanAccent else Gw2Themes.BorderSubtle,
                            RoundedCornerShape(6.dp)
                        )
                        .clickable {
                            viewModel.setTargetOverlayDisplayId(info.displayId)
                        }
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (info.isBottomScreen) "📱" else "🖥️",
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (info.isBottomScreen) "Bottom Screen (AYN Thor Recommended)" else "Display ID ${info.displayId} (Main Screen)",
                                    color = if (isSelected) Gw2Themes.CyanAccent else Color.White,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${info.width}x${info.height}px (${info.label})",
                                    color = Gw2Themes.TextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Gw2Themes.CyanAccent)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("ACTIVE", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    ConfigCard(title = "⚙️ Overlay Window Execution & Insets") {
        // Auto launch switch
        SettingToggleRow(
            title = "⚡ Auto-Launch Overlay",
            subtitle = "Automatically start overlay on bottom screen when app opens",
            checked = state.autoLaunchOverlay,
            onCheckedChange = { viewModel.setAutoLaunchOverlay(it) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Keep screen awake switch
        SettingToggleRow(
            title = "💡 Keep Screen Awake",
            subtitle = "Prevent bottom screen from timing out while gaming",
            checked = state.keepScreenAwake,
            onCheckedChange = { viewModel.setKeepScreenAwake(it) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Top Inset Padding
        Text(
            text = "Top Safe Area Inset: ${state.topInsetPaddingDp}dp (Avoids floating clocks/status bezels)",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(0, 8, 12, 16, 24).forEach { dpVal ->
                val isSel = state.topInsetPaddingDp == dpVal
                PillChoiceButton(
                    label = "${dpVal}dp",
                    isSelected = isSel,
                    onClick = { viewModel.setTopInsetPaddingDp(dpVal) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Bottom Inset Padding
        Text(
            text = "Bottom Safe Area Inset: ${state.bottomInsetPaddingDp}dp (Avoids Android gesture bar)",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(0, 6, 8, 12, 16).forEach { dpVal ->
                val isSel = state.bottomInsetPaddingDp == dpVal
                PillChoiceButton(
                    label = "${dpVal}dp",
                    isSelected = isSel,
                    onClick = { viewModel.setBottomInsetPaddingDp(dpVal) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Opacity
        Text(
            text = "Overlay Opacity: ${(state.overlayAlpha * 100).toInt()}%",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(1.0f to "100%", 0.9f to "90%", 0.8f to "80%", 0.7f to "70%").forEach { (alpha, label) ->
                val isSel = state.overlayAlpha == alpha
                PillChoiceButton(
                    label = label,
                    isSelected = isSel,
                    onClick = { viewModel.setOverlayAlpha(alpha) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Launch Overlay Button directly from settings
        Button(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        android.net.Uri.parse("package:" + context.packageName)
                    )
                    context.startActivity(intent)
                } else {
                    val allDisplays = HandheldDisplayManager.getAllDisplays(context)
                    val targetDisplay = if (state.targetOverlayDisplayId != -1) {
                        allDisplays.find { it.displayId == state.targetOverlayDisplayId }
                            ?: HandheldDisplayManager.findBottomDisplay(context)
                    } else {
                        HandheldDisplayManager.findBottomDisplay(context)
                    }

                    try {
                        val intent = Intent(context, TyriaOverlayService::class.java).apply {
                            putExtra(TyriaOverlayService.EXTRA_TARGET_DISPLAY_ID, targetDisplay.displayId)
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            context.startForegroundService(intent)
                        } else {
                            context.startService(intent)
                        }
                        (context as? Activity)?.moveTaskToBack(true)
                    } catch (e: Exception) {
                        android.util.Log.e("SettingsDialog", "Failed to start overlay service: ${e.message}", e)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF261D10)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Gw2Themes.GoldAccent),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = "🎯 Launch Persistent Overlay Window (No Focus Loss)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Gw2Themes.GoldAccent
            )
        }
    }
}

@Composable
private fun TouchDeckConfigSection(
    state: HubUiState,
    viewModel: MainViewModel
) {
    ConfigCard(title = "🎮 Touch Deck Ergonomics") {
        Text(
            text = "Configure button scales, default sub-mode layouts, and thumb grip ergonomics for the AYN Thor.",
            color = Gw2Themes.TextMuted,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Size Mode
        Text(
            text = "Button Size Preset: ${state.sizeMode}",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("XS" to "XS (Compact 40dp)", "MD" to "MD (Standard 52dp)", "LG" to "LG (Comfort 64dp)").forEach { (key, label) ->
                val isSel = state.sizeMode == key
                PillChoiceButton(
                    label = label,
                    isSelected = isSel,
                    onClick = { viewModel.setSizeMode(key) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Default Deck SubMode on Launch
        Text(
            text = "Default Touch Deck Mode on Startup:",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(
                DeckSubMode.THUMB_WINGS to "🎮 Thumb Wings",
                DeckSubMode.COMBAT_BAR to "⚔️ Combat Bar",
                DeckSubMode.MOUNTS_QOL to "🦅 Mounts & QoL"
            ).forEach { (mode, label) ->
                val isSel = state.defaultDeckSubMode == mode
                PillChoiceButton(
                    label = label,
                    isSelected = isSel,
                    onClick = { viewModel.setDefaultDeckSubMode(mode) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Left-Handed Mode Switch
        SettingToggleRow(
            title = "🔄 Left-Handed Mirror Mode",
            subtitle = "Swaps high-frequency skills to left thumb wing and utilities to right",
            checked = state.leftHandedMode,
            onCheckedChange = { viewModel.setLeftHandedMode(it) }
        )
    }

    // Active Build & Elite Specialization Configuration Card
    ConfigCard(title = "⚔️ Active Build & Elite Specialization") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Current: ${state.selectedBuild.eliteSpec} (${state.selectedBuild.profession})",
                    color = state.theme.primaryAccent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Mechanics: ${state.selectedBuild.mechanicsTitle} (${state.selectedBuild.mechanics.size} keys)",
                    color = Gw2Themes.TextMuted,
                    fontSize = 10.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF241D12))
                    .border(1.dp, Gw2Themes.GoldAccent, RoundedCornerShape(4.dp))
                    .clickable { viewModel.setShowBuildSelector(true) }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Browse All 12 Builds ▾",
                    color = Gw2Themes.GoldAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Quick Select Build Palette:",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Popular build presets grid
        val popularBuilds = PresetBuilds.all.take(8)
        popularBuilds.chunked(2).forEach { rowPair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                rowPair.forEach { build ->
                    val isSel = state.selectedBuild.id == build.id
                    val profTheme = Gw2Themes.forProfession(build.profession)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSel) profTheme.primaryAccent.copy(alpha = 0.25f) else Color(0xFF161924))
                            .border(
                                width = 1.dp,
                                color = if (isSel) profTheme.primaryAccent else Gw2Themes.BorderSubtle,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clickable { viewModel.selectBuild(build) }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⚔️ ${build.eliteSpec}",
                                color = if (isSel) Color.White else Gw2Themes.TextMuted,
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                            )
                            Text(
                                text = build.badgeCode,
                                color = profTheme.primaryAccent,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }

    // Steam Input & Hardware Split Guide Card
    ConfigCard(title = "🎮 Steam Input, Hardware & D-Pad Bindings") {
        Text(
            text = "Recommended physical control split for AYN Thor dual-screen handheld. Moment-to-moment combat is bound to physical triggers/sticks, and interface panels to the D-Pad.",
            color = Gw2Themes.TextMuted,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { viewModel.setShowControllerGuide(true) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2838)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Gw2Themes.CyanAccent),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = "📖 Open Full Hardware & D-Pad Mappings Guide",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Gw2Themes.CyanAccent
            )
        }
    }
}

@Composable
private fun InputConfigSection(
    state: HubUiState,
    viewModel: MainViewModel,
    context: Context
) {
    ConfigCard(title = "⚡ Input Injection & Feedback") {
        Text(
            text = "Choose how Tyria Hub dispatches game keys to Guild Wars 2 without stealing focus from the game screen.",
            color = Gw2Themes.TextMuted,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Input Method Selector
        Text(
            text = "Primary Input Method:",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(
                "IME" to "Native IME (Recommended)",
                "SHELL" to "Shell Keyevent (Root)",
                "DROIDDECK" to "DroidDeck Socket"
            ).forEach { (key, label) ->
                val isSel = state.inputMethodPreference == key
                PillChoiceButton(
                    label = label,
                    isSelected = isSel,
                    onClick = { viewModel.setInputMethodPreference(key) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Open IME system settings button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF172033))
                .border(1.dp, state.theme.primaryAccent, RoundedCornerShape(6.dp))
                .clickable {
                    val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⌨️", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Enable Tyria Hub in Android Keyboard Settings",
                            color = state.theme.primaryAccent,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (TyriaHubInputMethodService.isImeActive) "✓ Tyria Hub Input Method is active" else "Tap here to ensure 'Tyria Hub Touch Deck' is enabled in OS",
                            color = if (TyriaHubInputMethodService.isImeActive) Gw2Themes.GreenAccent else Gw2Themes.TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = "Open Settings",
                    tint = state.theme.primaryAccent,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Haptics Settings
        SettingToggleRow(
            title = "📳 Haptic Feedback (Vibration)",
            subtitle = "Tactile vibration when pressing combat slots and mounts",
            checked = state.isHapticsEnabled,
            onCheckedChange = { viewModel.toggleHaptics() }
        )

        if (state.isHapticsEnabled) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Vibration Intensity: ${state.hapticIntensity}",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("LIGHT", "MEDIUM", "HEAVY").forEach { intensity ->
                    val isSel = state.hapticIntensity == intensity
                    PillChoiceButton(
                        label = intensity,
                        isSelected = isSel,
                        onClick = { viewModel.setHapticIntensity(intensity) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Sound Toggle
        SettingToggleRow(
            title = "🔊 Audio Feedback",
            subtitle = "Sound clicks on button actuation",
            checked = state.isSoundEnabled,
            onCheckedChange = { viewModel.toggleSound() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Chat Macro Delay
        Text(
            text = "Chat Macro Delay: ${state.chatMacroDelayMs}ms (Enter -> Paste -> Enter pacing)",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(20L to "20ms (Fast)", 40L to "40ms (Standard)", 70L to "70ms (Safe)", 120L to "120ms (High Ping)").forEach { (ms, label) ->
                val isSel = state.chatMacroDelayMs == ms
                PillChoiceButton(
                    label = label,
                    isSelected = isSel,
                    onClick = { viewModel.setChatMacroDelayMs(ms) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Controller Guide button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF1E1A14))
                .border(1.dp, Gw2Themes.GoldAccent, RoundedCornerShape(6.dp))
                .clickable { viewModel.setShowControllerGuide(true) }
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🎮", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "View Steam Input & D-Pad Hardware Mapping",
                            color = Gw2Themes.GoldAccent,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Reference guide for physical buttons vs touch split",
                            color = Gw2Themes.TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }
                Text("OPEN ▾", color = Gw2Themes.GoldAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AppearanceConfigSection(
    state: HubUiState,
    viewModel: MainViewModel
) {
    ConfigCard(title = "🎨 Appearance & Visual Theme") {
        Text(
            text = "Customize UI colors, AMOLED battery saving, and high-contrast settings for outdoor visibility.",
            color = Gw2Themes.TextMuted,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Theme override
        Text(
            text = "Active Profession Palette:",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        val themes = listOf(
            "AUTO" to "Auto (Follow Active Build)",
            "Guardian" to "Guardian (Azure Blue)",
            "Ranger" to "Ranger (Emerald Green)",
            "Warrior" to "Warrior (Amber Orange)",
            "Revenant" to "Revenant (Mist Carmine)",
            "Thief" to "Thief (Shadow Maroon)",
            "Engineer" to "Engineer (Brass Bronze)",
            "Elementalist" to "Elementalist (Flame Red)",
            "Mesmer" to "Mesmer (Phantasm Purple)",
            "Necromancer" to "Necromancer (Poison Green)"
        )

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            themes.chunked(2).forEach { rowPair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    rowPair.forEach { (key, label) ->
                        val isSel = state.themeOverride.equals(key, ignoreCase = true)
                        PillChoiceButton(
                            label = label,
                            isSelected = isSel,
                            onClick = { viewModel.setThemeOverride(key) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowPair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Pure AMOLED Black
        SettingToggleRow(
            title = "🌑 Pure AMOLED Black (#000000)",
            subtitle = "Turns off pixels on OLED displays for maximum battery life",
            checked = state.amoledBlack,
            onCheckedChange = { viewModel.setAmoledBlack(it) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // High Contrast Borders
        SettingToggleRow(
            title = "☀️ High-Contrast Borders",
            subtitle = "Thick high-contrast borders for direct sunlight gaming",
            checked = state.highContrast,
            onCheckedChange = { viewModel.setHighContrast(it) }
        )
    }
}

@Composable
private fun SystemConfigSection(
    state: HubUiState,
    viewModel: MainViewModel,
    context: Context,
    onExitOverlay: (() -> Unit)?,
    onSwitchToWindowMode: (() -> Unit)?
) {
    ConfigCard(title = "🛠️ System Actions & Management") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Re-run setup wizard
            Button(
                onClick = {
                    viewModel.setShowSetupWizard(true)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2838)),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("🔄 Re-run Setup Wizard", fontSize = 11.sp, color = Gw2Themes.CyanAccent)
            }

            // Windowed Mode button
            if (onSwitchToWindowMode != null) {
                Button(
                    onClick = onSwitchToWindowMode,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D2235)),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("🪟 Windowed Mode", fontSize = 11.sp, color = Color(0xFF8DB2F5))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Exit overlay button
        if (onExitOverlay != null) {
            Button(
                onClick = onExitOverlay,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF421520)),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("✕ Exit Overlay / Close App", fontSize = 11.sp, color = Color(0xFFFF4D6D), fontWeight = FontWeight.Bold)
            }
        }
    }

    ConfigCard(title = "📊 Handheld & Hardware Diagnostics") {
        val allDisplays = HandheldDisplayManager.getAllDisplays(context)

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            DiagnosticRow("Device Model", "${Build.MANUFACTURER.uppercase()} ${Build.MODEL}")
            DiagnosticRow("Android Version", "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
            DiagnosticRow("Displays Detected", "${allDisplays.size} physical display(s)")
            DiagnosticRow("Overlay Service", if (TyriaOverlayService.isOverlayActive) "Active (TYPE_APPLICATION_OVERLAY)" else "Inactive")
            DiagnosticRow("Input Method", if (TyriaHubInputMethodService.isImeActive) "Active (IME Connection Live)" else "Standby")
            DiagnosticRow("Overlay Focus", "FLAG_NOT_FOCUSABLE (Zero Game Interruption)")
        }
    }
}

@Composable
private fun DiagnosticRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Gw2Themes.TextMuted, fontSize = 10.5.sp)
        Text(text = value, color = Color.White, fontSize = 10.5.sp, fontFamily = FontFamily.Monospace)
    }
}

@Composable
private fun ConfigCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF131622))
            .border(1.dp, Color(0xFF222638), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.White, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = Gw2Themes.TextMuted, fontSize = 10.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Gw2Themes.GreenAccent,
                uncheckedThumbColor = Gw2Themes.TextMuted,
                uncheckedTrackColor = Color(0xFF232738)
            )
        )
    }
}

@Composable
private fun PillChoiceButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) Color(0xFF1F3550) else Color(0xFF191D2C))
            .border(
                1.dp,
                if (isSelected) Gw2Themes.CyanAccent else Gw2Themes.BorderSubtle,
                RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else Gw2Themes.TextMuted,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
        )
    }
}
