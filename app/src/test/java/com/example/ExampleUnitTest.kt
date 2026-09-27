package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {
  private lateinit var viewModel: HydrationViewModel

  @Before
  fun setUp() {
    viewModel = HydrationViewModel()
  }

  @Test
  fun `initial state is 0ml and target is 2000ml`() {
    val state = viewModel.uiState.value
    assertEquals(0, state.currentIntakeMl)
    assertEquals(2000, state.targetMl)
    assertEquals(0f, state.progress, 0.001f)
    assertFalse(state.isGoalReached)
    assertEquals(2000, state.remainingMl)
  }

  @Test
  fun `addWater increments intake by 250ml`() {
    viewModel.addWater(250)
    val state = viewModel.uiState.value
    assertEquals(250, state.currentIntakeMl)
    assertEquals(1, state.glassesCount)
    assertEquals(1750, state.remainingMl)
    assertEquals(0.125f, state.progress, 0.001f)
    assertEquals(1, state.history.size)
  }

  @Test
  fun `reaching 2000ml marks goal as reached`() {
    repeat(8) {
      viewModel.addWater(250)
    }
    val state = viewModel.uiState.value
    assertEquals(2000, state.currentIntakeMl)
    assertEquals(8, state.glassesCount)
    assertEquals(0, state.remainingMl)
    assertEquals(1.0f, state.progress, 0.001f)
    assertTrue(state.isGoalReached)
  }

  @Test
  fun `confirmReset clears current intake and history`() {
    viewModel.addWater(250)
    viewModel.addWater(250)
    assertEquals(500, viewModel.uiState.value.currentIntakeMl)

    viewModel.confirmReset()
    val state = viewModel.uiState.value
    assertEquals(0, state.currentIntakeMl)
    assertEquals(0, state.history.size)
    assertFalse(state.showResetConfirmation)
  }

  @Test
  fun `undoLastEntry removes the latest addition`() {
    viewModel.addWater(250)
    viewModel.addWater(250)
    assertEquals(500, viewModel.uiState.value.currentIntakeMl)

    viewModel.undoLastEntry()
    assertEquals(250, viewModel.uiState.value.currentIntakeMl)
    assertEquals(1, viewModel.uiState.value.history.size)
  }
}

