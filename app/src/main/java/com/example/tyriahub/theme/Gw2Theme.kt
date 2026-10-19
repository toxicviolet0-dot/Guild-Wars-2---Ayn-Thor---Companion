package com.example.tyriahub.theme

import androidx.compose.ui.graphics.Color

data class ProfessionTheme(
    val name: String,
    val primaryAccent: Color,
    val secondaryAccent: Color,
    val glowColor: Color,
    val surfaceColor: Color,
    val mechanicBadgeBg: Color,
    val mechanicBadgeBorder: Color,
    val textAccent: Color
)

object Gw2Themes {
    val Guardian = ProfessionTheme(
        name = "Guardian",
        primaryAccent = Color(0xFF199BD9),      // Guardian Aegis Blue
        secondaryAccent = Color(0xFF00B3FF),
        glowColor = Color(0x66199BD9),
        surfaceColor = Color(0xFF131F2A),
        mechanicBadgeBg = Color(0xFF0E384D),
        mechanicBadgeBorder = Color(0xFF199BD9),
        textAccent = Color(0xFF68D2FF)
    )

    val Ranger = ProfessionTheme(
        name = "Ranger",
        primaryAccent = Color(0xFF4EBA33),      // Verdurous Green
        secondaryAccent = Color(0xFF62D943),
        glowColor = Color(0x664EBA33),
        surfaceColor = Color(0xFF122216),
        mechanicBadgeBg = Color(0xFF163E1C),
        mechanicBadgeBorder = Color(0xFF4EBA33),
        textAccent = Color(0xFF88EA73)
    )

    val Warrior = ProfessionTheme(
        name = "Warrior",
        primaryAccent = Color(0xFFFF9900),      // Warlord Amber Orange
        secondaryAccent = Color(0xFFFFB733),
        glowColor = Color(0x66FF9900),
        surfaceColor = Color(0xFF26190C),
        mechanicBadgeBg = Color(0xFF4D2C00),
        mechanicBadgeBorder = Color(0xFFFF9900),
        textAccent = Color(0xFFFFD280)
    )

    val Revenant = ProfessionTheme(
        name = "Revenant",
        primaryAccent = Color(0xFFC83737),      // Mist Carmine Red
        secondaryAccent = Color(0xFFE84D4D),
        glowColor = Color(0x66C83737),
        surfaceColor = Color(0xFF261214),
        mechanicBadgeBg = Color(0xFF4A141A),
        mechanicBadgeBorder = Color(0xFFC83737),
        textAccent = Color(0xFFFFA1A1)
    )

    val Thief = ProfessionTheme(
        name = "Thief",
        primaryAccent = Color(0xFFC45163),      // Shadow Amethyst Maroon
        secondaryAccent = Color(0xFFE06B7F),
        glowColor = Color(0x66C45163),
        surfaceColor = Color(0xFF261318),
        mechanicBadgeBg = Color(0xFF4A1C25),
        mechanicBadgeBorder = Color(0xFFC45163),
        textAccent = Color(0xFFFF9EAF)
    )

    val Engineer = ProfessionTheme(
        name = "Engineer",
        primaryAccent = Color(0xFFC47A2C),      // Brass Cog Bronze
        secondaryAccent = Color(0xFFE69138),
        glowColor = Color(0x66C47A2C),
        surfaceColor = Color(0xFF24180E),
        mechanicBadgeBg = Color(0xFF472A0D),
        mechanicBadgeBorder = Color(0xFFC47A2C),
        textAccent = Color(0xFFFFBE7B)
    )

    val Elementalist = ProfessionTheme(
        name = "Elementalist",
        primaryAccent = Color(0xFFEC5A35),      // Prismatic Scarlet
        secondaryAccent = Color(0xFFFF7B54),
        glowColor = Color(0x66EC5A35),
        surfaceColor = Color(0xFF26140F),
        mechanicBadgeBg = Color(0xFF4D1A0D),
        mechanicBadgeBorder = Color(0xFFEC5A35),
        textAccent = Color(0xFFFFAA91)
    )

    val Mesmer = ProfessionTheme(
        name = "Mesmer",
        primaryAccent = Color(0xFFA442C2),      // Phantasmagoric Royal Purple
        secondaryAccent = Color(0xFFBF5FE0),
        glowColor = Color(0x66A442C2),
        surfaceColor = Color(0xFF221128),
        mechanicBadgeBg = Color(0xFF421550),
        mechanicBadgeBorder = Color(0xFFA442C2),
        textAccent = Color(0xFFE29BFA)
    )

    val Necromancer = ProfessionTheme(
        name = "Necromancer",
        primaryAccent = Color(0xFF4C9950),      // Fel Lichen Green
        secondaryAccent = Color(0xFF6BB870),
        glowColor = Color(0x664C9950),
        surfaceColor = Color(0xFF132115),
        mechanicBadgeBg = Color(0xFF1D3B20),
        mechanicBadgeBorder = Color(0xFF4C9950),
        textAccent = Color(0xFF90D994)
    )

    fun forProfession(profession: String): ProfessionTheme {
        return when (profession.lowercase()) {
            "guardian" -> Guardian
            "ranger" -> Ranger
            "warrior" -> Warrior
            "revenant" -> Revenant
            "thief" -> Thief
            "engineer" -> Engineer
            "elementalist" -> Elementalist
            "mesmer" -> Mesmer
            "necromancer" -> Necromancer
            else -> Guardian
        }
    }

    // Shared Palette Tokens
    val DeepBackground = Color(0xFF090A0E)
    val CardBackground = Color(0xFF11131A)
    val BorderSubtle = Color(0xFF252936)
    val TextMuted = Color(0xFF8A93A6)
    val TextBright = Color(0xFFF1F5F9)
    val GoldAccent = Color(0xFFE5B869)
    val CyanAccent = Color(0xFF4DEEEA)
    val GreenAccent = Color(0xFF4EBA33)
    val DangerRed = Color(0xFFFF4D6D)
    val WaypointYellow = Color(0xFFFFC857)
    val DodgeRed = Color(0xFFB32644)
    val SwapCyan = Color(0xFF136E8F)
    val TargetPurple = Color(0xFF2D2353)
    val SkyscaleBrown = Color(0xFF4A3420)
}
