package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Tyria Hub", appName)
  }

  @Test
  fun `test main activity launch`() {
    val controller = org.robolectric.Robolectric.buildActivity(MainActivity::class.java)
    val activity = controller.setup().get()
    org.junit.Assert.assertNotNull(activity)
  }

  @Test
  fun `test overlay service creation`() {
    val serviceController = org.robolectric.Robolectric.buildService(com.example.tyriahub.overlay.TyriaOverlayService::class.java)
    val service = serviceController.create().get()
    org.junit.Assert.assertNotNull(service)
  }

  @Test
  fun `test main view model load example data`() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = com.example.tyriahub.ui.MainViewModel(context)
    viewModel.loadExampleData()
    val state = viewModel.uiState.value
    org.junit.Assert.assertTrue(state.characterList.isNotEmpty())
    org.junit.Assert.assertEquals("Valerius Dawnseeker", state.selectedCharacter?.name)
    org.junit.Assert.assertTrue(state.selectedCharacter?.bags?.isNotEmpty() == true)

    viewModel.selectTab(com.example.tyriahub.ui.HubTab.INVENTORY)
    val updatedState = viewModel.uiState.value
    org.junit.Assert.assertEquals(com.example.tyriahub.ui.HubTab.INVENTORY, updatedState.currentTab)
    org.junit.Assert.assertEquals(5, updatedState.selectedCharacter?.bags?.size)
  }

  @Test
  fun `test size mode scaling config`() {
    val xsConfig = com.example.tyriahub.theme.DeckSizeConfig.forMode("XS")
    val mdConfig = com.example.tyriahub.theme.DeckSizeConfig.forMode("MD")
    val lgConfig = com.example.tyriahub.theme.DeckSizeConfig.forMode("LG")

    org.junit.Assert.assertTrue(xsConfig.touchButtonHeight < mdConfig.touchButtonHeight)
    org.junit.Assert.assertTrue(mdConfig.touchButtonHeight < lgConfig.touchButtonHeight)
    org.junit.Assert.assertTrue(xsConfig.inventoryColumns > lgConfig.inventoryColumns)
    org.junit.Assert.assertTrue(xsConfig.navHeight < lgConfig.navHeight)
  }

  @Test
  fun `test fetchAccountData with blank key does not crash and keeps state`() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = com.example.tyriahub.ui.MainViewModel(context)
    viewModel.fetchAccountData("   ")
    val state = viewModel.uiState.value
    org.junit.Assert.assertFalse(state.isApiLoading)
  }

  @Test
  fun `test example data does not have invalid api key error`() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = com.example.tyriahub.ui.MainViewModel(context)
    viewModel.loadExampleData(notify = false)
    val state = viewModel.uiState.value
    org.junit.Assert.assertTrue(state.isExampleData)
    org.junit.Assert.assertNull(state.apiErrorMessage)
    org.junit.Assert.assertFalse(state.isApiLoading)

    // Also verify passing an EXAMPLE testing key doesn't hit network or cause 401 error
    viewModel.fetchAccountData("EXAMPLE-GW2-API-KEY-TESTING")
    val stateAfter = viewModel.uiState.value
    org.junit.Assert.assertTrue(stateAfter.isExampleData)
    org.junit.Assert.assertNull(stateAfter.apiErrorMessage)
    org.junit.Assert.assertFalse(stateAfter.isApiLoading)
  }
}


