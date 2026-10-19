package com.example.tyriahub.model

import java.util.Calendar
import java.util.TimeZone

enum class BossCategory {
    ALL,
    CORE,
    EXPANSION
}

data class WorldBossDefinition(
    val id: String,
    val name: String,
    val zone: String,
    val waypointCode: String,
    val category: BossCategory,
    // Daily UTC schedule in minutes from 00:00 UTC (0 .. 1439)
    val scheduleMinutesUtc: List<Int>,
    val durationMinutes: Int = 15
)

data class ActiveBossStatus(
    val definition: WorldBossDefinition,
    val isLive: Boolean,
    val minutesUntil: Int,
    val minutesLeft: Int,
    val nextSpawnUtc: String
)

data class TyrianTimeStatus(
    val phaseName: String,      // "Day", "Dusk", "Night", "Dawn"
    val minutesRemainingInPhase: Int,
    val nextResetHours: Int,
    val nextResetMinutes: Int
)

object WorldBossRepository {
    val bosses = listOf(
        WorldBossDefinition(
            id = "fire_elemental",
            name = "Fire Elemental",
            zone = "Metrica Province",
            waypointCode = "[&BEcAAAA=]",
            category = BossCategory.CORE,
            scheduleMinutesUtc = listOf(45, 165, 285, 405, 525, 645, 765, 885, 1005, 1125, 1245, 1365)
        ),
        WorldBossDefinition(
            id = "shadow_behemoth",
            name = "Shadow Behemoth",
            zone = "Queensdale",
            waypointCode = "[&BPcAAAA=]",
            category = BossCategory.CORE,
            scheduleMinutesUtc = listOf(105, 225, 345, 465, 585, 705, 825, 945, 1065, 1185, 1305, 1425)
        ),
        WorldBossDefinition(
            id = "tequatl",
            name = "Tequatl the Sunless",
            zone = "Sparkfly Fen",
            waypointCode = "[&BNABAAA=]",
            category = BossCategory.CORE,
            scheduleMinutesUtc = listOf(0, 180, 420, 690, 960, 1140),
            durationMinutes = 20
        ),
        WorldBossDefinition(
            id = "shatterer",
            name = "The Shatterer",
            zone = "Blazeridge Steppes",
            waypointCode = "[&BE4DAAA=]",
            category = BossCategory.CORE,
            scheduleMinutesUtc = listOf(60, 240, 420, 600, 780, 960, 1140, 1320)
        ),
        WorldBossDefinition(
            id = "claw_of_jormag",
            name = "Claw of Jormag",
            zone = "Frostgorge Sound",
            waypointCode = "[&BHgCAAA=]",
            category = BossCategory.CORE,
            scheduleMinutesUtc = listOf(150, 330, 510, 690, 870, 1050, 1230, 1410)
        ),
        WorldBossDefinition(
            id = "megadestroyer",
            name = "Megadestroyer",
            zone = "Mount Maelstrom",
            waypointCode = "[&BM0CAAA=]",
            category = BossCategory.CORE,
            scheduleMinutesUtc = listOf(30, 150, 270, 390, 510, 630, 750, 870, 990, 1110, 1230, 1350)
        ),
        WorldBossDefinition(
            id = "triple_trouble",
            name = "Triple Trouble",
            zone = "Bloodtide Coast",
            waypointCode = "[&BKoBAAA=]",
            category = BossCategory.CORE,
            scheduleMinutesUtc = listOf(60, 240, 480, 750, 1020, 1200),
            durationMinutes = 25
        ),
        WorldBossDefinition(
            id = "karka_queen",
            name = "Karka Queen",
            zone = "Southsun Cove",
            waypointCode = "[&BNUGAAA=]",
            category = BossCategory.CORE,
            scheduleMinutesUtc = listOf(120, 360, 630, 900, 1080, 1380)
        ),
        WorldBossDefinition(
            id = "golem_mark_ii",
            name = "Inquest Golem Mark II",
            zone = "Mount Maelstrom",
            waypointCode = "[&BNQCAAA=]",
            category = BossCategory.CORE,
            scheduleMinutesUtc = listOf(0, 120, 240, 360, 480, 600, 720, 840, 960, 1080, 1200, 1320)
        ),
        WorldBossDefinition(
            id = "svanir_shaman",
            name = "Svanir Shaman Chief",
            zone = "Wayfarer Foothills",
            waypointCode = "[&BMIDAAA=]",
            category = BossCategory.CORE,
            scheduleMinutesUtc = listOf(15, 135, 255, 375, 495, 615, 735, 855, 975, 1095, 1215, 1335)
        ),
        WorldBossDefinition(
            id = "octovine",
            name = "Battle in Tarir (Octovine)",
            zone = "Auric Basin (Heart of Thorns)",
            waypointCode = "[&BAIIAAA=]",
            category = BossCategory.EXPANSION,
            scheduleMinutesUtc = listOf(0, 120, 240, 360, 480, 600, 720, 840, 960, 1080, 1200, 1320),
            durationMinutes = 20
        ),
        WorldBossDefinition(
            id = "chak_gerent",
            name = "Chak Gerent",
            zone = "Tangled Depths (Heart of Thorns)",
            waypointCode = "[&BPUHAAA=]",
            category = BossCategory.EXPANSION,
            scheduleMinutesUtc = listOf(30, 150, 270, 390, 510, 630, 750, 870, 990, 1110, 1230, 1350),
            durationMinutes = 20
        ),
        WorldBossDefinition(
            id = "choya_pinata",
            name = "Casino Blitz & Choya Pinata",
            zone = "Crystal Oasis (Path of Fire)",
            waypointCode = "[&BLsKAAA=]",
            category = BossCategory.EXPANSION,
            scheduleMinutesUtc = listOf(20, 140, 260, 380, 500, 620, 740, 860, 980, 1100, 1220, 1340),
            durationMinutes = 20
        ),
        WorldBossDefinition(
            id = "dragons_end",
            name = "Battle for the Jade Sea",
            zone = "Dragon's End (End of Dragons)",
            waypointCode = "[&BKcNAAA=]",
            category = BossCategory.EXPANSION,
            scheduleMinutesUtc = listOf(0, 120, 240, 360, 480, 600, 720, 840, 960, 1080, 1200, 1320),
            durationMinutes = 30
        )
    )

    fun calculateCurrentEvents(currentUtcMillis: Long = System.currentTimeMillis()): List<ActiveBossStatus> {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = currentUtcMillis
        }
        val currentMinutesUtc = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)

        return bosses.map { def ->
            var isLive = false
            var minutesUntil = Int.MAX_VALUE
            var minutesLeft = 0
            var nextSpawnMinute = -1

            for (spawnMin in def.scheduleMinutesUtc) {
                val endMin = spawnMin + def.durationMinutes
                if (currentMinutesUtc in spawnMin until endMin) {
                    isLive = true
                    minutesLeft = endMin - currentMinutesUtc
                    minutesUntil = 0
                    nextSpawnMinute = spawnMin
                    break
                }
            }

            if (!isLive) {
                // Find next upcoming spawn
                val futureSpawns = def.scheduleMinutesUtc.filter { it > currentMinutesUtc }
                if (futureSpawns.isNotEmpty()) {
                    nextSpawnMinute = futureSpawns.first()
                    minutesUntil = nextSpawnMinute - currentMinutesUtc
                } else {
                    // First spawn tomorrow
                    nextSpawnMinute = def.scheduleMinutesUtc.first()
                    minutesUntil = (1440 - currentMinutesUtc) + nextSpawnMinute
                }
            }

            val hours = nextSpawnMinute / 60
            val mins = nextSpawnMinute % 60
            val spawnTimeStr = String.format("%02d:%02d UTC", hours, mins)

            ActiveBossStatus(
                definition = def,
                isLive = isLive,
                minutesUntil = minutesUntil,
                minutesLeft = minutesLeft,
                nextSpawnUtc = spawnTimeStr
            )
        }.sortedWith(
            compareBy<ActiveBossStatus> { !it.isLive }
                .thenBy { it.minutesUntil }
        )
    }

    fun calculateTyrianTime(currentUtcMillis: Long = System.currentTimeMillis()): TyrianTimeStatus {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = currentUtcMillis
        }
        val currentMinutesUtc = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
        val cycleMinute = currentMinutesUtc % 120

        val (phase, remaining) = when {
            cycleMinute < 25 -> "Day" to (25 - cycleMinute)
            cycleMinute < 30 -> "Dusk" to (30 - cycleMinute)
            cycleMinute < 75 -> "Night" to (75 - cycleMinute)
            cycleMinute < 80 -> "Dawn" to (80 - cycleMinute)
            else -> "Day" to (120 - cycleMinute + 25)
        }

        val minutesUntilReset = 1440 - currentMinutesUtc
        val resetHours = minutesUntilReset / 60
        val resetMinutes = minutesUntilReset % 60

        return TyrianTimeStatus(
            phaseName = phase,
            minutesRemainingInPhase = remaining,
            nextResetHours = resetHours,
            nextResetMinutes = resetMinutes
        )
    }
}
