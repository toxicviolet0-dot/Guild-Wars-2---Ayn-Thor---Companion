package com.example.tyriahub.model

/**
 * Rich example/mock data for testing Guild Wars 2 Account, Characters, Wallet,
 * and Equipment inspection on dual-screen devices without requiring an active ArenaNet API key.
 */
object ExampleData {

    val tokenInfo = TokenInfoResponse(
        id = "TEST-APIKEY-PERMISSIONS-ALL",
        name = "Tyria Hub Testing Key (All Scopes)",
        permissions = listOf("account", "characters", "inventories", "wallet", "unlocks", "builds", "pvp", "wvw", "progression")
    )

    val account = AccountResponse(
        id = "TEST-ACCOUNT-TYRIA-7492",
        name = "CommanderTyria.7492",
        age = 4210980L,
        world = 1008, // Jade Quarry
        guilds = listOf("Order of Whispers", "Vigil Vanguard", "Durmand Priory"),
        created = "2012-08-28T00:00:00Z",
        access = listOf(
            "GuildWars2",
            "HeartOfThorns",
            "PathOfFire",
            "EndOfDragons",
            "SecretsOfTheObscure",
            "JanthirWilds"
        ),
        commander = true,
        fractalLevel = 100,
        dailyAp = 14250,
        monthlyAp = 4800,
        wvwRank = 845
    )

    const val walletCoins: Long = 14856240L // 1,485 Gold, 62 Silver, 40 Copper
    const val karma: Long = 3485210L
    const val laurels: Long = 175L
    const val gems: Long = 1600L
    const val astralAcclaim: Long = 840L

    private fun createGearSet(weapon1: String, weapon2: String?, offhand: String?, armorWeight: String): List<CharacterEquipmentItem> {
        val list = mutableListOf<CharacterEquipmentItem>()
        list.add(CharacterEquipmentItem(id = 80101, slot = "Helm", rarity = "Ascended"))
        list.add(CharacterEquipmentItem(id = 80102, slot = "Shoulders", rarity = "Ascended"))
        list.add(CharacterEquipmentItem(id = 80103, slot = "Coat", rarity = "Ascended"))
        list.add(CharacterEquipmentItem(id = 80104, slot = "Gloves", rarity = "Ascended"))
        list.add(CharacterEquipmentItem(id = 80105, slot = "Leggings", rarity = "Ascended"))
        list.add(CharacterEquipmentItem(id = 80106, slot = "Boots", rarity = "Ascended"))
        list.add(CharacterEquipmentItem(id = 90101, slot = "WeaponA1", rarity = "Legendary"))
        if (offhand != null) {
            list.add(CharacterEquipmentItem(id = 90102, slot = "WeaponA2", rarity = "Ascended"))
        }
        if (weapon2 != null) {
            list.add(CharacterEquipmentItem(id = 90103, slot = "WeaponB1", rarity = "Legendary"))
        }
        list.add(CharacterEquipmentItem(id = 70101, slot = "Backpack", rarity = "Ascended"))
        list.add(CharacterEquipmentItem(id = 70102, slot = "Accessory1", rarity = "Ascended"))
        list.add(CharacterEquipmentItem(id = 70103, slot = "Accessory2", rarity = "Ascended"))
        list.add(CharacterEquipmentItem(id = 70104, slot = "Amulet", rarity = "Ascended"))
        list.add(CharacterEquipmentItem(id = 70105, slot = "Ring1", rarity = "Ascended"))
        list.add(CharacterEquipmentItem(id = 70106, slot = "Ring2", rarity = "Ascended"))
        return list
    }

    private fun createCharacterBags(profession: String): List<CharacterBag> {
        // Bag 1: 20-Slot Starter Backpack - Utilities, keys, portal devices, food
        val bag1Items = mutableListOf<InventoryItem?>()
        bag1Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 89410, count = 1, binding = "Account"))) // Copper-Fed
        bag1Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 88272, count = 1, binding = "Account"))) // Mistlock Passkey
        bag1Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 78873, count = 1, binding = "Account"))) // White Mantle Portal Device
        bag1Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 79899, count = 1, binding = "Account"))) // Prototype Position Rewinder
        bag1Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 12452, count = 35))) // Plate of Beef Rendang
        bag1Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 12423, count = 28))) // Butternut Soup
        bag1Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 43360, count = 50))) // Fried Golden Dumpling
        bag1Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 67528, count = 42))) // Toxic Focusing Crystal
        bag1Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 91805, count = 5, binding = "Account"))) // Feast of Delectable Cake
        bag1Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 10012, count = 3, binding = "Account"))) // Wizard's Vault Astral Bag
        // Rest of Bag 1 empty slots
        while (bag1Items.size < 20) { bag1Items.add(null) }

        // Bag 2: 20-Slot Safe Box - Legendary & Ascended spare gear & weapons
        val bag2Items = mutableListOf<InventoryItem?>()
        when (profession) {
            "Guardian" -> {
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 30699, count = 1, binding = "Account"))) // Sunrise
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 30695, count = 1, binding = "Account"))) // Flameseeker Prophecies
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 80101, count = 1, binding = "Account"))) // Zojja's Greatsword
            }
            "Warrior" -> {
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 30704, count = 1, binding = "Account"))) // Twilight
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 30684, count = 1, binding = "Account"))) // Bolt
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 80103, count = 2, binding = "Account"))) // Ascended Armor Chest
            }
            "Ranger" -> {
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 30687, count = 1, binding = "Account"))) // Kudzu
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 72713, count = 1, binding = "Account"))) // Astralaria
            }
            "Necromancer" -> {
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 76158, count = 1, binding = "Account"))) // Nevermore
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 80102, count = 1, binding = "Account"))) // Tequatl's Hoard
            }
            "Thief" -> {
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 30691, count = 1, binding = "Account"))) // Incinerator
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 30684, count = 1, binding = "Account"))) // Bolt
            }
            "Elementalist" -> {
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 30689, count = 1, binding = "Account"))) // The Bifrost
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 30691, count = 1, binding = "Account"))) // Incinerator
            }
            "Mesmer" -> {
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 30684, count = 1, binding = "Account"))) // Bolt
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 88770, count = 1, binding = "Account"))) // Vision
            }
            "Revenant" -> {
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 30703, count = 1, binding = "Account"))) // Eternity
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 91234, count = 1, binding = "Account"))) // Coalescence
            }
            else -> {
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 30684, count = 1, binding = "Account"))) // Bolt
                bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 80101, count = 1, binding = "Account"))) // Zojja's
            }
        }
        bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 79895, count = 1, binding = "Account"))) // Ad Infinitum
        bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 81908, count = 1, binding = "Account"))) // Aurora
        bag2Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 80105, count = 1, binding = "Account"))) // Blood Infused Band
        while (bag2Items.size < 20) { bag2Items.add(null) }

        // Bag 3: 20-Slot Gossamer Bag - Crafting materials & high value tokens
        val bag3Items = mutableListOf<InventoryItem?>()
        bag3Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 19721, count = 250))) // Ectoplasm x250
        bag3Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 19721, count = 142))) // Ectoplasm x142
        bag3Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 19976, count = 250))) // Mystic Coin x250
        bag3Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 19684, count = 77, binding = "Account"))) // Mystic Clover x77
        bag3Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 68063, count = 120))) // Amalgamated Gemstone x120
        bag3Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 24277, count = 250))) // Powerful Blood x250
        bag3Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 24295, count = 180))) // Powerful Venom Sac x180
        bag3Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 24358, count = 210))) // Ancient Bone x210
        bag3Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 19700, count = 250))) // Mithril Ingot x250
        bag3Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 19722, count = 250))) // Elder Wood Plank x250
        bag3Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 80104, count = 15, binding = "Account"))) // Vision Crystal x15
        while (bag3Items.size < 20) { bag3Items.add(null) }

        // Bag 4: 20-Slot Invisible Bag - Containers & Salvage Kits
        val bag4Items = mutableListOf<InventoryItem?>()
        bag4Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 23001, count = 1, charges = 250))) // Mystic Salvage Kit
        bag4Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 89411, count = 1, binding = "Account"))) // Silver-Fed Salvage-o-Matic
        bag4Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 10013, count = 12, binding = "Account"))) // Heavy Crafting Bag x12
        bag4Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 19748, count = 160))) // Silk Scrap x160
        while (bag4Items.size < 20) { bag4Items.add(null) }

        // Bag 5: 20-Slot Olmakhan Leather Pack - Vendor Junk & Trophies
        val bag5Items = mutableListOf<InventoryItem?>()
        bag5Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 24363, count = 18))) // Cracked Spikes x18
        bag5Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 24364, count = 34))) // Broken Claws x34
        bag5Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 24365, count = 9))) // Shiny Bauble x9 (27 silver!)
        bag5Items.add(Gw2ItemCatalog.resolve(InventoryItem(id = 24366, count = 14))) // Engorged Spider Eye x14
        while (bag5Items.size < 20) { bag5Items.add(null) }

        return listOf(
            CharacterBag(id = 8932, size = 20, inventory = bag1Items),
            CharacterBag(id = 8941, size = 20, inventory = bag2Items),
            CharacterBag(id = 8932, size = 20, inventory = bag3Items),
            CharacterBag(id = 9535, size = 20, inventory = bag4Items),
            CharacterBag(id = 8932, size = 20, inventory = bag5Items)
        )
    }

    val characters: List<CharacterResponse> = listOf(
        CharacterResponse(
            name = "Valerius Dawnseeker",
            race = "Human",
            gender = "Male",
            profession = "Guardian",
            level = 80,
            deaths = 42,
            equipment = createGearSet("Greatsword", "Scepter", "Shield", "Heavy"),
            bags = createCharacterBags("Guardian")
        ),
        CharacterResponse(
            name = "Sylvia Bramblethorn",
            race = "Sylvari",
            gender = "Female",
            profession = "Ranger",
            level = 80,
            deaths = 28,
            equipment = createGearSet("Longbow", "Axe", "Dagger", "Medium"),
            bags = createCharacterBags("Ranger")
        ),
        CharacterResponse(
            name = "Ignis Cogwheel",
            race = "Asura",
            gender = "Male",
            profession = "Engineer",
            level = 80,
            deaths = 35,
            equipment = createGearSet("Rifle", "Pistol", "Pistol", "Medium"),
            bags = createCharacterBags("Engineer")
        ),
        CharacterResponse(
            name = "Morrigan Voidcaller",
            race = "Norn",
            gender = "Female",
            profession = "Necromancer",
            level = 80,
            deaths = 19,
            equipment = createGearSet("Greatsword", "Staff", null, "Light"),
            bags = createCharacterBags("Necromancer")
        ),
        CharacterResponse(
            name = "Lyra Shadowstep",
            race = "Human",
            gender = "Female",
            profession = "Thief",
            level = 80,
            deaths = 51,
            equipment = createGearSet("Dagger", "Shortbow", "Dagger", "Medium"),
            bags = createCharacterBags("Thief")
        ),
        CharacterResponse(
            name = "Aurelius Chronos",
            race = "Sylvari",
            gender = "Male",
            profession = "Mesmer",
            level = 80,
            deaths = 22,
            equipment = createGearSet("Dagger", "Greatsword", "Sword", "Light"),
            bags = createCharacterBags("Mesmer")
        ),
        CharacterResponse(
            name = "Kaelen Stonebreaker",
            race = "Charr",
            gender = "Male",
            profession = "Warrior",
            level = 80,
            deaths = 63,
            equipment = createGearSet("Greatsword", "Axe", "Axe", "Heavy"),
            bags = createCharacterBags("Warrior")
        ),
        CharacterResponse(
            name = "Vesper Stormcaller",
            race = "Human",
            gender = "Female",
            profession = "Elementalist",
            level = 80,
            deaths = 78,
            equipment = createGearSet("Sword", "Staff", "Dagger", "Light"),
            bags = createCharacterBags("Elementalist")
        ),
        CharacterResponse(
            name = "Radek Mistsoul",
            race = "Charr",
            gender = "Male",
            profession = "Revenant",
            level = 80,
            deaths = 31,
            equipment = createGearSet("Greatsword", "Sword", "Sword", "Heavy"),
            bags = createCharacterBags("Revenant")
        )
    )
}
