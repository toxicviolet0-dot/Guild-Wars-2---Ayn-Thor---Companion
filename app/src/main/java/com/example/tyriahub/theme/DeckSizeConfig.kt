package com.example.tyriahub.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Centralized sizing configuration linking the XS / MD / LG mode to all interface elements:
 * - Button heights, widths, and paddings
 * - Typography (title, label, hint, badge font sizes)
 * - Icon and emoji scaling
 * - Layout gaps, row heights, and container padding
 * - Grid densities (such as inventory and mounts)
 */
data class DeckSizeConfig(
    val mode: String, // "XS", "MD", "LG"
    
    // Global scales
    val fontScale: Float,
    val iconScale: Float,
    val spacingScale: Float,

    // Touch Buttons (Thumb Wings, Combat Bar, TouchDeck)
    val touchButtonHeight: Dp,
    val touchButtonTitleSp: TextUnit,
    val touchButtonSubSp: TextUnit,
    val touchButtonKeySp: TextUnit,
    val touchButtonPaddingH: Dp,
    val touchButtonPaddingV: Dp,
    val touchButtonCornerRadius: Dp,

    // F-Keys (Profession Mechanics)
    val fKeyHeight: Dp,
    val fKeyTitleSp: TextUnit,
    val fKeySubSp: TextUnit,

    // Combat Bar & Thumb Wings Spacing
    val combatBarSlotSize: Dp,
    val combatBarGap: Dp,
    val wingsGap: Dp,
    val wingsRowHeightMultiplier: Float,

    // Mounts & QoL
    val mountBtnHeight: Dp,
    val mountIconSp: TextUnit,
    val mountTitleSp: TextUnit,
    val mountSubSp: TextUnit,
    val mountGridSpacing: Dp,

    // Inventory
    val inventoryColumns: Int,
    val inventorySlotIconSp: TextUnit,
    val inventoryBadgeSp: TextUnit,
    val inventorySlotPadding: Dp,
    val inventorySpacing: Dp,
    val inventoryHeaderSp: TextUnit,
    val inventorySearchHeight: Dp,

    // Top Navigation & Live Ticker
    val navHeight: Dp,
    val navTabPaddingH: Dp,
    val navTabPaddingV: Dp,
    val navTabFontSp: TextUnit,
    val navIconSize: Dp,
    val tickerHeight: Dp,
    val tickerFontSp: TextUnit,
    val tickerBtnPaddingH: Dp,

    // Cards (Boss HUD, Vault, Characters)
    val cardPadding: Dp,
    val cardSpacing: Dp,
    val titleSp: TextUnit,
    val bodySp: TextUnit,
    val captionSp: TextUnit,
    val iconSize: Dp,
    val waypointBtnHeight: Dp
) {
    companion object {
        val XS = DeckSizeConfig(
            mode = "XS",
            fontScale = 0.82f,
            iconScale = 0.80f,
            spacingScale = 0.75f,

            touchButtonHeight = 36.dp,
            touchButtonTitleSp = 9.5.sp,
            touchButtonSubSp = 7.5.sp,
            touchButtonKeySp = 7.sp,
            touchButtonPaddingH = 2.dp,
            touchButtonPaddingV = 1.dp,
            touchButtonCornerRadius = 4.dp,

            fKeyHeight = 26.dp,
            fKeyTitleSp = 9.sp,
            fKeySubSp = 7.5.sp,

            combatBarSlotSize = 42.dp,
            combatBarGap = 2.dp,
            wingsGap = 2.5.dp,
            wingsRowHeightMultiplier = 0.82f,

            mountBtnHeight = 42.dp,
            mountIconSp = 18.sp,
            mountTitleSp = 9.5.sp,
            mountSubSp = 7.5.sp,
            mountGridSpacing = 3.dp,

            inventoryColumns = 10,
            inventorySlotIconSp = 11.5.sp,
            inventoryBadgeSp = 6.5.sp,
            inventorySlotPadding = 1.dp,
            inventorySpacing = 2.dp,
            inventoryHeaderSp = 10.5.sp,
            inventorySearchHeight = 32.dp,

            navHeight = 32.dp,
            navTabPaddingH = 6.dp,
            navTabPaddingV = 3.dp,
            navTabFontSp = 9.5.sp,
            navIconSize = 13.dp,
            tickerHeight = 24.dp,
            tickerFontSp = 9.5.sp,
            tickerBtnPaddingH = 5.dp,

            cardPadding = 5.dp,
            cardSpacing = 4.dp,
            titleSp = 11.5.sp,
            bodySp = 10.sp,
            captionSp = 8.5.sp,
            iconSize = 22.dp,
            waypointBtnHeight = 24.dp
        )

        val MD = DeckSizeConfig(
            mode = "MD",
            fontScale = 1.0f,
            iconScale = 1.0f,
            spacingScale = 1.0f,

            touchButtonHeight = 48.dp,
            touchButtonTitleSp = 11.5.sp,
            touchButtonSubSp = 8.5.sp,
            touchButtonKeySp = 8.5.sp,
            touchButtonPaddingH = 4.dp,
            touchButtonPaddingV = 2.dp,
            touchButtonCornerRadius = 6.dp,

            fKeyHeight = 32.dp,
            fKeyTitleSp = 10.5.sp,
            fKeySubSp = 8.5.sp,

            combatBarSlotSize = 54.dp,
            combatBarGap = 4.dp,
            wingsGap = 4.dp,
            wingsRowHeightMultiplier = 1.0f,

            mountBtnHeight = 54.dp,
            mountIconSp = 22.sp,
            mountTitleSp = 11.sp,
            mountSubSp = 8.5.sp,
            mountGridSpacing = 4.dp,

            inventoryColumns = 8,
            inventorySlotIconSp = 13.5.sp,
            inventoryBadgeSp = 7.5.sp,
            inventorySlotPadding = 1.5.dp,
            inventorySpacing = 3.dp,
            inventoryHeaderSp = 12.sp,
            inventorySearchHeight = 38.dp,

            navHeight = 38.dp,
            navTabPaddingH = 9.dp,
            navTabPaddingV = 5.dp,
            navTabFontSp = 11.sp,
            navIconSize = 15.dp,
            tickerHeight = 28.dp,
            tickerFontSp = 10.5.sp,
            tickerBtnPaddingH = 8.dp,

            cardPadding = 8.dp,
            cardSpacing = 6.dp,
            titleSp = 13.sp,
            bodySp = 11.sp,
            captionSp = 9.5.sp,
            iconSize = 28.dp,
            waypointBtnHeight = 28.dp
        )

        val LG = DeckSizeConfig(
            mode = "LG",
            fontScale = 1.20f,
            iconScale = 1.22f,
            spacingScale = 1.25f,

            touchButtonHeight = 62.dp,
            touchButtonTitleSp = 13.5.sp,
            touchButtonSubSp = 10.sp,
            touchButtonKeySp = 10.sp,
            touchButtonPaddingH = 6.dp,
            touchButtonPaddingV = 3.dp,
            touchButtonCornerRadius = 8.dp,

            fKeyHeight = 40.dp,
            fKeyTitleSp = 12.sp,
            fKeySubSp = 10.sp,

            combatBarSlotSize = 66.dp,
            combatBarGap = 6.dp,
            wingsGap = 6.dp,
            wingsRowHeightMultiplier = 1.20f,

            mountBtnHeight = 66.dp,
            mountIconSp = 28.sp,
            mountTitleSp = 13.sp,
            mountSubSp = 10.sp,
            mountGridSpacing = 6.dp,

            inventoryColumns = 6,
            inventorySlotIconSp = 16.5.sp,
            inventoryBadgeSp = 9.sp,
            inventorySlotPadding = 2.dp,
            inventorySpacing = 4.5.dp,
            inventoryHeaderSp = 14.sp,
            inventorySearchHeight = 44.dp,

            navHeight = 46.dp,
            navTabPaddingH = 12.dp,
            navTabPaddingV = 7.dp,
            navTabFontSp = 12.5.sp,
            navIconSize = 18.dp,
            tickerHeight = 34.dp,
            tickerFontSp = 12.sp,
            tickerBtnPaddingH = 10.dp,

            cardPadding = 12.dp,
            cardSpacing = 8.dp,
            titleSp = 15.sp,
            bodySp = 12.5.sp,
            captionSp = 10.5.sp,
            iconSize = 36.dp,
            waypointBtnHeight = 32.dp
        )

        fun forMode(mode: String): DeckSizeConfig = when (mode.uppercase()) {
            "XS" -> XS
            "LG" -> LG
            else -> MD
        }
    }
}

val LocalDeckSizeConfig = compositionLocalOf { DeckSizeConfig.MD }
