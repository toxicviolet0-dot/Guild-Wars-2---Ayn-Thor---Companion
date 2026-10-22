package com.example.tyriahub.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenInfoResponse(
    val id: String = "",
    val name: String = "",
    val permissions: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class AccountResponse(
    val id: String = "",
    val name: String = "",
    val age: Long = 0,
    val world: Int = 0,
    val guilds: List<String> = emptyList(),
    @Json(name = "created") val created: String = "",
    @Json(name = "access") val access: List<String> = emptyList(),
    @Json(name = "commander") val commander: Boolean = false,
    @Json(name = "fractal_level") val fractalLevel: Int = 1,
    @Json(name = "daily_ap") val dailyAp: Int = 0,
    @Json(name = "monthly_ap") val monthlyAp: Int = 0,
    @Json(name = "wvw_rank") val wvwRank: Int = 0
)

@JsonClass(generateAdapter = true)
data class WalletItemResponse(
    val id: Int,
    val value: Long
)

@JsonClass(generateAdapter = true)
data class CharacterEquipmentItem(
    val id: Int = 0,
    val slot: String = "",
    val rarity: String? = null,
    val skin: Int? = null,
    val charges: Int? = null
)

@JsonClass(generateAdapter = true)
data class InventoryItem(
    val id: Int = 0,
    val count: Int = 1,
    val charges: Int? = null,
    val skin: Int? = null,
    val upgrades: List<Int> = emptyList(),
    val infusions: List<Int> = emptyList(),
    val binding: String? = null,
    @Json(name = "bound_to") val boundTo: String? = null,
    val name: String? = null,
    val rarity: String? = null,
    val type: String? = null,
    val description: String? = null,
    val icon: String? = null,
    @Json(name = "chat_code") val chatCode: String? = null,
    @Json(name = "vendor_value") val vendorValue: Long? = null
)

@JsonClass(generateAdapter = true)
data class CharacterBag(
    val id: Int = 0,
    val size: Int = 20,
    val inventory: List<InventoryItem?> = emptyList()
)

@JsonClass(generateAdapter = true)
data class CharacterResponse(
    val name: String = "",
    val race: String = "",
    val gender: String = "",
    val profession: String = "",
    val level: Int = 80,
    val deaths: Int = 0,
    val equipment: List<CharacterEquipmentItem> = emptyList(),
    val bags: List<CharacterBag?> = emptyList()
)

@JsonClass(generateAdapter = true)
data class ItemPriceDetails(
    @Json(name = "unit_price") val unitPrice: Long = 0,
    val quantity: Long = 0
)

@JsonClass(generateAdapter = true)
data class CommercePriceResponse(
    val id: Int,
    val whitelisted: Boolean = true,
    val buys: ItemPriceDetails = ItemPriceDetails(),
    val sells: ItemPriceDetails = ItemPriceDetails()
)

data class FormattedCurrency(
    val gold: Long,
    val silver: Long,
    val copper: Long
) {
    companion object {
        fun fromCoins(coins: Long): FormattedCurrency {
            val gold = coins / 10000
            val silver = (coins % 10000) / 100
            val copper = coins % 100
            return FormattedCurrency(gold, silver, copper)
        }
    }
}
