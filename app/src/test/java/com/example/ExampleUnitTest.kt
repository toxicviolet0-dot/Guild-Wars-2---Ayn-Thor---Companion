package com.example

import com.example.tyriahub.model.ExampleData
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun exampleData_hasAccountAndValidCurrencies() {
    val account = ExampleData.account
    assertEquals("CommanderTyria.7492", account.name)
    assertEquals(100, account.fractalLevel)
    assertTrue("Wallet coins should be over 1000 gold", ExampleData.walletCoins > 10000000)
    assertTrue("Karma should be positive", ExampleData.karma > 0)
    assertTrue("Laurels should be positive", ExampleData.laurels > 0)
    assertTrue("Astral Acclaim should be positive", ExampleData.astralAcclaim > 0)
  }

  @Test
  fun exampleData_hasAllNineProfessionsLevel80() {
    val chars = ExampleData.characters
    assertEquals(9, chars.size)
    
    val professions = chars.map { it.profession }.toSet()
    val expected = setOf(
      "Guardian", "Ranger", "Engineer", "Necromancer",
      "Thief", "Mesmer", "Warrior", "Elementalist", "Revenant"
    )
    assertEquals(expected, professions)
    assertTrue("All test characters should be level 80", chars.all { it.level == 80 })
    assertTrue("All test characters should have equipped gear", chars.all { it.equipment.isNotEmpty() })
  }

  @Test
  fun exampleData_charactersHaveBagsAndInventoryItems() {
    val chars = ExampleData.characters
    assertTrue("All characters should have inventory bags", chars.all { it.bags.isNotEmpty() })
    val firstChar = chars.first()
    assertEquals(5, firstChar.bags.size)
    val totalSlots = firstChar.bags.sumOf { it?.size ?: 0 }
    assertEquals(100, totalSlots)
    val filledItems = firstChar.bags.flatMap { it?.inventory ?: emptyList() }.filterNotNull()
    assertTrue("Character should have items in bags", filledItems.isNotEmpty())
    assertTrue("Character should have some free empty slots", filledItems.size < totalSlots)
  }

  @Test
  fun gw2ItemCatalog_resolvesItemMetadataProperly() {
    val resolved = com.example.tyriahub.model.Gw2ItemCatalog.resolve(
      com.example.tyriahub.model.InventoryItem(id = 19721, count = 250)
    )
    assertNotNull(resolved)
    assertEquals("Glob of Ectoplasm", resolved?.name)
    assertEquals("Rare", resolved?.rarity)
    assertEquals("Material", resolved?.type)
  }
}

