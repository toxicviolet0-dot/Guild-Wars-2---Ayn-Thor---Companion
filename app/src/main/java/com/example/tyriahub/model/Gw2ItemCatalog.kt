package com.example.tyriahub.model

import androidx.compose.ui.graphics.Color

/**
 * Catalog and resolver for Guild Wars 2 inventory items, resolving IDs to
 * authentic names, rarities, categories, chat codes, and descriptions.
 */
object Gw2ItemCatalog {

    data class ItemMeta(
        val name: String,
        val rarity: String,
        val type: String,
        val icon: String,
        val chatCode: String,
        val description: String = "",
        val vendorValue: Long = 0
    )

    val rarityColors = mapOf(
        "Junk" to Color(0xFFAAAAAA),
        "Basic" to Color(0xFFE5E7EB),
        "Fine" to Color(0xFF62A8EA),
        "Masterwork" to Color(0xFF38B249),
        "Rare" to Color(0xFFECC94B),
        "Exotic" to Color(0xFFFFA000),
        "Ascended" to Color(0xFFFF4081),
        "Legendary" to Color(0xFFBA68C8)
    )

    fun colorForRarity(rarity: String?): Color {
        return rarityColors[rarity] ?: Color(0xFF9E9E9E)
    }

    private val catalog = mapOf(
        // Legendaries
        30699 to ItemMeta("Sunrise", "Legendary", "Weapon", "⚔️", "[&AgG7dAIA]", "Legendary Greatsword with dynamic prismatic dawn effects.", 50000),
        30704 to ItemMeta("Twilight", "Legendary", "Weapon", "⚔️", "[&AgGAdgIA]", "Legendary Greatsword containing the night sky and cosmos.", 50000),
        30703 to ItemMeta("Eternity", "Legendary", "Weapon", "⚔️", "[&AgF/dgIA]", "The fused embodiment of sunrise and twilight.", 100000),
        30684 to ItemMeta("Bolt", "Legendary", "Weapon", "⚡", "[&AgGcdAIA]", "Legendary Sword crackling with pure electric plasma.", 50000),
        30689 to ItemMeta("The Bifrost", "Legendary", "Weapon", "🌈", "[&AgGhdAIA]", "Legendary Staff of rainbow radiance and light.", 50000),
        30687 to ItemMeta("Kudzu", "Legendary", "Weapon", "🏹", "[&AgGfdAIA]", "Legendary Longbow blooming with Tyrian flora.", 50000),
        30691 to ItemMeta("Incinerator", "Legendary", "Weapon", "🔥", "[&AgGjdAIA]", "Legendary Dagger ignited with dragonfire.", 50000),
        30695 to ItemMeta("The Flameseeker Prophecies", "Legendary", "Weapon", "🛡️", "[&AgGndAIA]", "Legendary Shield inscribed with historic prophecies.", 50000),
        76158 to ItemMeta("Nevermore", "Legendary", "Weapon", "🦅", "[&AgE+MQEA]", "Spirit of the Raven resting upon a wooden branch.", 50000),
        72713 to ItemMeta("Astralaria", "Legendary", "Weapon", "🌌", "[&AgEpLwEA]", "Astrolabe axe reflecting constellations of the Mists.", 50000),
        79895 to ItemMeta("Ad Infinitum", "Legendary", "Armor", "🪽", "[&AgH/LAEA]", "Fractal glider and backpiece vibrating with unstable flux.", 40000),
        91234 to ItemMeta("Coalescence", "Legendary", "Trinket", "💍", "[&AgFaXAEA]", "Raid ring infused with the coalesced essence of Hall of Chains.", 30000),
        88770 to ItemMeta("Vision", "Legendary", "Trinket", "🔮", "[&AgFCWwEA]", "Legendary accessory granting clairvoyant sight into Tyria.", 30000),
        81908 to ItemMeta("Aurora", "Legendary", "Trinket", "✨", "[&AgEYQAEA]", "Legendary accessory pulsing with Living World Magic.", 30000),

        // Ascended Gear & Chests
        80101 to ItemMeta("Zojja's Greatsword", "Ascended", "Weapon", "⚔️", "[&AgEVVgAA]", "Berserker stats (+Power, +Precision, +Ferocity).", 10000),
        80102 to ItemMeta("Tequatl's Hoard", "Ascended", "Container", "📦", "[&AgEUWQAA]", "Contains your choice of ascended Sunless weapons.", 20000),
        80103 to ItemMeta("Ascended Armor Chest", "Ascended", "Container", "🛡️", "[&AgFkWgAA]", "Selectable ascended armor piece with custom stats.", 25000),
        80104 to ItemMeta("Vision Crystal", "Ascended", "Material", "💎", "[&AgFlWgAA]", "Infused crystal needed to craft Ascended armaments.", 5000),
        80105 to ItemMeta("Blood Infused Band", "Ascended", "Trinket", "💍", "[&AgFmVgAA]", "Bloodstone infused ring with selectable attributes.", 4950),

        // High Value Currencies & Materials
        19721 to ItemMeta("Glob of Ectoplasm", "Rare", "Material", "✨", "[&AgFxEwAA]", "Salvaged from rare equipment. Essential crafting resource.", 150),
        19976 to ItemMeta("Mystic Coin", "Rare", "Material", "🪙", "[&AgGIEwAA]", "Used in the Mystic Forge to craft legendary items.", 22000),
        19684 to ItemMeta("Mystic Clover", "Rare", "Material", "🍀", "[&AgEcEwAA]", "Crucial catalyst for forge transmutations and legendary gifts.", 15000),
        68063 to ItemMeta("Amalgamated Gemstone", "Rare", "Material", "💎", "[&AgE/QwAA]", "Fused crystal of diverse gemstones used in elite weapons.", 12500),
        24277 to ItemMeta("Vial of Powerful Blood", "Fine", "Material", "🩸", "[&AgHVEwAA]", "Tier 6 Trophy. Required for berserker crafting.", 450),
        24295 to ItemMeta("Powerful Venom Sac", "Fine", "Material", "🧪", "[&AgHnEwAA]", "Tier 6 Trophy. Concentrated poison.", 320),
        24358 to ItemMeta("Ancient Bone", "Fine", "Material", "🦴", "[&AgH+EwAA]", "Tier 6 Trophy. Fossilized bone of ancient behemoths.", 290),
        19700 to ItemMeta("Mithril Ingot", "Basic", "Material", "🧱", "[&AgEUEwAA]", "Refined mithril metal for weaponry and heavy armor.", 50),
        19722 to ItemMeta("Elder Wood Plank", "Basic", "Material", "🪵", "[&AgFyEwAA]", "Refined elder wood for staves, bows, and shields.", 80),
        19748 to ItemMeta("Silk Scrap", "Basic", "Material", "🧵", "[&AgFEEwAA]", "Soft woven threads refined into gossamer fabrics.", 30),

        // Consumables & Buffs
        12452 to ItemMeta("Plate of Beef Rendang", "Exotic", "Consumable", "🍲", "[&AgFEPAAA]", "+100 Condition Damage, +70 Expertise, +10% Karma.", 850),
        12423 to ItemMeta("Sweet & Spicy Butternut Soup", "Masterwork", "Consumable", "🥣", "[&AgEfPAAA]", "+100 Power, +70 Ferocity, +10% Experience.", 450),
        43360 to ItemMeta("Fried Golden Dumpling", "Masterwork", "Consumable", "🥟", "[&AgFoqQAA]", "+33% Boon Duration on Dodge, +100 Concentration.", 380),
        67528 to ItemMeta("Toxic Focusing Crystal", "Masterwork", "Consumable", "🔮", "[&AgFgCQEA]", "+100 Expertise, +10% Condition Damage.", 520),
        91805 to ItemMeta("Feast of Delectable Cake", "Ascended", "Consumable", "🎂", "[&AgH1ZgEA]", "Places birthday feast for all nearby allies with +All Stats.", 12000),

        // Utility, Gizmos & Salvage
        89410 to ItemMeta("Copper-Fed Salvage-o-Matic", "Rare", "Utility", "⚙️", "[&AgEyVgEA]", "Unlimited salvage tool that consumes 3 copper per use.", 30000),
        89411 to ItemMeta("Silver-Fed Salvage-o-Matic", "Rare", "Utility", "🪙", "[&AgEzVgEA]", "Consumes 60 copper to salvage with high rare yield.", 45000),
        23001 to ItemMeta("Mystic Salvage Kit", "Rare", "Utility", "🛠️", "[&AgH5VAAA]", "250 charges of master-tier salvage efficiency.", 2500),
        88272 to ItemMeta("Mistlock Sanctuary Passkey", "Exotic", "Utility", "🚪", "[&AgGQWQEA]", "Instant teleport pass to the cosmic fractal lounge in the Mists.", 50000),
        78873 to ItemMeta("White Mantle Portal Device", "Rare", "Utility", "🌀", "[&AgHhLgEA]", "Creates a localized spatial gateway between two points.", 20000),
        79899 to ItemMeta("Prototype Position Rewinder", "Rare", "Utility", "⏳", "[&AgEjLwEA]", "Marks a temporal waypoint and rewinds your location.", 5000),

        // Wizard's Vault, Bags & Containers
        10012 to ItemMeta("Wizard's Vault Astral Bag", "Rare", "Container", "✨", "[&AgGkZgEA]", "Contains Astral Acclaim tradeable rewards and coin.", 10000),
        10013 to ItemMeta("Heavy Crafting Bag", "Masterwork", "Container", "🎒", "[&AgGlZgEA]", "Contains 3 random Tier 6 crafting materials.", 2500),
        8941 to ItemMeta("20 Slot Safe Box", "Masterwork", "Bag", "🧰", "[&AgHNCAAA]", "Items placed here will not be sold to vendors or sorted.", 5000),
        9535 to ItemMeta("20 Slot Invisible Bag", "Masterwork", "Bag", "🎒", "[&AgHfCQAA]", "Items here are invisible to trading post and vendors.", 5000),
        8932 to ItemMeta("20 Slot Gossamer Bag", "Masterwork", "Bag", "🎒", "[&AgHECAAA]", "Spacious 20-slot cloth expansion bag.", 4500),

        // Junk & Trophies
        24363 to ItemMeta("Cracked Spikes", "Junk", "Junk", "🦴", "[&AgHjEwAA]", "Shattered chitin spikes. Sell to a vendor.", 150),
        24364 to ItemMeta("Broken Claws", "Junk", "Junk", "🐾", "[&AgHkEwAA]", "Worn and useless beast claws. Sell to vendor.", 85),
        24365 to ItemMeta("Shiny Bauble", "Junk", "Junk", "🪙", "[&AgHlEwAA]", "A shiny trinket salvaged from dredge minecarts. 30 silver.", 3000),
        24366 to ItemMeta("Engorged Spider Eye", "Junk", "Junk", "👁️", "[&AgHmEwAA]", "Oozing gland trophy suitable only for vendor copper.", 110)
    )

    /**
     * Enriches an inventory item using known catalog metadata if its fields are null or empty.
     */
    fun resolve(item: InventoryItem?): InventoryItem? {
        if (item == null) return null
        val meta = catalog[item.id]
        return if (meta != null) {
            item.copy(
                name = item.name ?: meta.name,
                rarity = item.rarity ?: meta.rarity,
                type = item.type ?: meta.type,
                icon = item.icon ?: meta.icon,
                chatCode = item.chatCode ?: meta.chatCode,
                description = if (item.description.isNullOrBlank()) meta.description else item.description,
                vendorValue = item.vendorValue ?: meta.vendorValue
            )
        } else {
            // Default fallback for unknown item ID
            val inferredType = when {
                item.upgrades.isNotEmpty() -> "Equipment"
                item.charges != null -> "Consumable"
                else -> "Item"
            }
            item.copy(
                name = item.name ?: "GW2 Item #${item.id}",
                rarity = item.rarity ?: "Basic",
                type = item.type ?: inferredType,
                icon = item.icon ?: "📦",
                chatCode = item.chatCode ?: "[&Ag${item.id}AAAA==]",
                description = item.description ?: "Item ID: ${item.id} (Tap to ping in game chat)"
            )
        }
    }
}
