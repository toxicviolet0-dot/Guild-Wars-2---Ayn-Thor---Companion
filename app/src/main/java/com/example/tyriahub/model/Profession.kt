package com.example.tyriahub.model

data class MechanicSkill(
    val slot: String,       // "F1", "F2", "F3", "F4", "F5"
    val name: String,       // e.g. "Spear", "Wings", "Shield"
    val description: String = "",
    val keybind: String
)

data class ProfessionBuild(
    val id: String,
    val profession: String,     // "Guardian", "Ranger", etc.
    val eliteSpec: String,       // "Dragonhunter", "Galeshot", "Untamed", etc.
    val badgeCode: String,      // "[F1F2F3]", "[F1-F5]"
    val mechanicsTitle: String,
    val mechanics: List<MechanicSkill>,
    // Full names
    val healName: String,
    val util1Name: String,
    val util2Name: String,
    val util3Name: String,
    val eliteName: String,
    // Short punchy names for compact handheld display
    val shortHeal: String = healName,
    val shortUtil1: String = util1Name,
    val shortUtil2: String = util2Name,
    val shortUtil3: String = util3Name,
    val shortElite: String = eliteName
)

object PresetBuilds {
    val Dragonhunter = ProfessionBuild(
        id = "guardian_dragonhunter",
        profession = "Guardian",
        eliteSpec = "Dragonhunter",
        badgeCode = "[F1F2F3]",
        mechanicsTitle = "Dragonhunter (Guardian) Mechanics",
        mechanics = listOf(
            MechanicSkill("F1", "Spear", "Justice", "F1"),
            MechanicSkill("F2", "Wings", "Resolve", "F2"),
            MechanicSkill("F3", "Shield", "Courage", "F3")
        ),
        healName = "Litany of Wrath",
        util1Name = "Procession of Blades",
        util2Name = "Test of Faith",
        util3Name = "Fragments of Faith",
        eliteName = "Dragon's Maw",
        shortHeal = "Litany",
        shortUtil1 = "Blades",
        shortUtil2 = "Test of Faith",
        shortUtil3 = "Fragments",
        shortElite = "Dragon's Maw"
    )

    val Galeshot = ProfessionBuild(
        id = "ranger_galeshot",
        profession = "Ranger",
        eliteSpec = "Galeshot",
        badgeCode = "[F1-F5]",
        mechanicsTitle = "Galeshot (Ranger) Mechanics",
        mechanics = listOf(
            MechanicSkill("F1", "Attack", "Pet Attack", "F1"),
            MechanicSkill("F2", "Burst", "Pet Burst", "F2"),
            MechanicSkill("F3", "Recall", "Pet Return", "F3"),
            MechanicSkill("F4", "Swap", "Swap Pet", "F4"),
            MechanicSkill("F5", "Aero", "Wind Toggle", "F5")
        ),
        healName = "\"We Heal As One!\"",
        util1Name = "Quickening Zephyr",
        util2Name = "Frost Trap",
        util3Name = "\"Sic 'Em!\"",
        eliteName = "\"Strength of the Pack!\"",
        shortHeal = "We Heal",
        shortUtil1 = "Zephyr",
        shortUtil2 = "Frost Trap",
        shortUtil3 = "Sic 'Em",
        shortElite = "Pack"
    )

    val Firebrand = ProfessionBuild(
        id = "guardian_firebrand",
        profession = "Guardian",
        eliteSpec = "Firebrand",
        badgeCode = "[F1-F3]",
        mechanicsTitle = "Firebrand Tome Mechanics",
        mechanics = listOf(
            MechanicSkill("F1", "Justice", "Damage Tome", "F1"),
            MechanicSkill("F2", "Resolve", "Healing Tome", "F2"),
            MechanicSkill("F3", "Courage", "Support Tome", "F3")
        ),
        healName = "Mantra of Solace",
        util1Name = "Mantra of Flame",
        util2Name = "Mantra of Lore",
        util3Name = "\"Stand Ground!\"",
        eliteName = "Mantra of Liberation",
        shortHeal = "Solace",
        shortUtil1 = "Flame",
        shortUtil2 = "Lore",
        shortUtil3 = "Stand Ground",
        shortElite = "Liberation"
    )

    val Elementalist = ProfessionBuild(
        id = "elementalist_core",
        profession = "Elementalist",
        eliteSpec = "Weaver",
        badgeCode = "[F1-F4]",
        mechanicsTitle = "Elemental Attunements",
        mechanics = listOf(
            MechanicSkill("F1", "Fire", "Fire Attunement", "F1"),
            MechanicSkill("F2", "Water", "Water Attunement", "F2"),
            MechanicSkill("F3", "Air", "Air Attunement", "F3"),
            MechanicSkill("F4", "Earth", "Earth Attunement", "F4")
        ),
        healName = "Glyph of Elemental Harmony",
        util1Name = "Primordial Stance",
        util2Name = "Arcane Blast",
        util3Name = "Twist of Fate",
        eliteName = "Weave Self",
        shortHeal = "Harmony",
        shortUtil1 = "Primordial",
        shortUtil2 = "Arcane Blast",
        shortUtil3 = "Twist of Fate",
        shortElite = "Weave Self"
    )

    val Mesmer = ProfessionBuild(
        id = "mesmer_virtuoso",
        profession = "Mesmer",
        eliteSpec = "Virtuoso",
        badgeCode = "[F1-F5]",
        mechanicsTitle = "Virtuoso Psionic Bladesongs",
        mechanics = listOf(
            MechanicSkill("F1", "Harmony", "Bladesong Damage", "F1"),
            MechanicSkill("F2", "Sorrow", "Bladesong Vuln", "F2"),
            MechanicSkill("F3", "Dissonance", "Bladesong Daze", "F3"),
            MechanicSkill("F4", "Distortion", "Bladesong Invuln", "F4"),
            MechanicSkill("F5", "Requiem", "Bladesong Rain", "F5")
        ),
        healName = "Twin Blade Restoration",
        util1Name = "Bladecall",
        util2Name = "Phantasmal Berserker",
        util3Name = "Well of Action",
        eliteName = "Thousand Cuts",
        shortHeal = "Twin Blade",
        shortUtil1 = "Bladecall",
        shortUtil2 = "Phantasm",
        shortUtil3 = "Well of Action",
        shortElite = "1000 Cuts"
    )

    val Warrior = ProfessionBuild(
        id = "warrior_berserker",
        profession = "Warrior",
        eliteSpec = "Berserker",
        badgeCode = "[F1F2]",
        mechanicsTitle = "Berserker Rage Mechanics",
        mechanics = listOf(
            MechanicSkill("F1", "Burst", "Primal Burst", "F1"),
            MechanicSkill("F2", "Berserk", "Enter Berserk Mode", "F2")
        ),
        healName = "Blood Reckoning",
        util1Name = "Outrage",
        util2Name = "\"For Great Justice!\"",
        util3Name = "Headbutt",
        eliteName = "Signet of Rage",
        shortHeal = "Reckoning",
        shortUtil1 = "Outrage",
        shortUtil2 = "Justice!",
        shortUtil3 = "Headbutt",
        shortElite = "Rage"
    )

    val Revenant = ProfessionBuild(
        id = "revenant_herald",
        profession = "Revenant",
        eliteSpec = "Herald",
        badgeCode = "[F1F2]",
        mechanicsTitle = "Herald Legendary Facets",
        mechanics = listOf(
            MechanicSkill("F1", "Swap", "Swap Stance", "F1"),
            MechanicSkill("F2", "Nature", "True Nature", "F2")
        ),
        healName = "Facet of Light",
        util1Name = "Facet of Darkness",
        util2Name = "Facet of Elements",
        util3Name = "Facet of Strength",
        eliteName = "Facet of Chaos",
        shortHeal = "Light",
        shortUtil1 = "Darkness",
        shortUtil2 = "Elements",
        shortUtil3 = "Strength",
        shortElite = "Chaos"
    )

    val Thief = ProfessionBuild(
        id = "thief_specter",
        profession = "Thief",
        eliteSpec = "Specter",
        badgeCode = "[F1F2]",
        mechanicsTitle = "Specter Shadow Shroud",
        mechanics = listOf(
            MechanicSkill("F1", "Siphon", "Target Ally/Foe", "F1"),
            MechanicSkill("F2", "Shroud", "Shadow Shroud", "F2")
        ),
        healName = "Well of Gloom",
        util1Name = "Well of Bounty",
        util2Name = "Well of Tears",
        util3Name = "Shadowstep",
        eliteName = "Shadowfall",
        shortHeal = "Gloom",
        shortUtil1 = "Bounty",
        shortUtil2 = "Tears",
        shortUtil3 = "Shadowstep",
        shortElite = "Shadowfall"
    )

    val Engineer = ProfessionBuild(
        id = "engineer_holosmith",
        profession = "Engineer",
        eliteSpec = "Holosmith",
        badgeCode = "[F1-F5]",
        mechanicsTitle = "Holosmith Photon Forge",
        mechanics = listOf(
            MechanicSkill("F1", "Tool 1", "Toolbelt 1", "F1"),
            MechanicSkill("F2", "Tool 2", "Toolbelt 2", "F2"),
            MechanicSkill("F3", "Tool 3", "Toolbelt 3", "F3"),
            MechanicSkill("F4", "Tool 4", "Toolbelt 4", "F4"),
            MechanicSkill("F5", "Forge", "Enter Forge", "F5")
        ),
        healName = "Coolant Blast",
        util1Name = "Laser Disk",
        util2Name = "Hard Light Shield",
        util3Name = "Photon Wall",
        eliteName = "Prime Light Beam",
        shortHeal = "Coolant",
        shortUtil1 = "Laser Disk",
        shortUtil2 = "Hard Light",
        shortUtil3 = "Photon Wall",
        shortElite = "Prime Beam"
    )

    val Necromancer = ProfessionBuild(
        id = "necro_reaper",
        profession = "Necromancer",
        eliteSpec = "Reaper",
        badgeCode = "[F1]",
        mechanicsTitle = "Reaper's Shroud",
        mechanics = listOf(
            MechanicSkill("F1", "Shroud", "Enter Reaper Shroud", "F1")
        ),
        healName = "\"Your Soul Is Mine!\"",
        util1Name = "\"You Have No Power!\"",
        util2Name = "\"Suffer!\"",
        util3Name = "Well of Suffering",
        eliteName = "\"Chilled to the Bone!\"",
        shortHeal = "Soul Mine",
        shortUtil1 = "No Power",
        shortUtil2 = "Suffer",
        shortUtil3 = "Suffering",
        shortElite = "Chilled"
    )

    val all = listOf(
        Dragonhunter,
        Galeshot,
        Firebrand,
        Elementalist,
        Mesmer,
        Warrior,
        Revenant,
        Thief,
        Engineer,
        Necromancer
    )
}
