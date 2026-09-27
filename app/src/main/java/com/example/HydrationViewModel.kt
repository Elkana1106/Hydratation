package com.example

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HydrationRecord(
  val id: Long = System.currentTimeMillis(),
  val amountMl: Int,
  val timeFormatted: String
)

data class HydrationUiState(
  val currentIntakeMl: Int = 0,
  val targetMl: Int = 2000,
  val history: List<HydrationRecord> = emptyList(),
  val showResetConfirmation: Boolean = false,
  val celebrationTrigger: Int = 0
) {
  val progress: Float
    get() = if (targetMl > 0) (currentIntakeMl.toFloat() / targetMl.toFloat()).coerceIn(0f, 1f) else 0f

  val percentage: Int
    get() = ((currentIntakeMl.toFloat() / targetMl.toFloat()) * 100).toInt()

  val remainingMl: Int
    get() = (targetMl - currentIntakeMl).coerceAtLeast(0)

  val isGoalReached: Boolean
    get() = currentIntakeMl >= targetMl

  val glassesCount: Int
    get() = currentIntakeMl / 250
}

class HydrationViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(HydrationUiState())
  val uiState: StateFlow<HydrationUiState> = _uiState.asStateFlow()

  private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

  fun addWater(amountMl: Int = 250) {
    _uiState.update { currentState ->
      val newTotal = currentState.currentIntakeMl + amountMl
      val newRecord = HydrationRecord(
        amountMl = amountMl,
        timeFormatted = timeFormat.format(Date())
      )
      val justReachedGoal = currentState.currentIntakeMl < currentState.targetMl && newTotal >= currentState.targetMl

      currentState.copy(
        currentIntakeMl = newTotal,
        history = listOf(newRecord) + currentState.history,
        celebrationTrigger = if (justReachedGoal) currentState.celebrationTrigger + 1 else currentState.celebrationTrigger
      )
    }
  }

  fun requestReset() {
    _uiState.update { it.copy(showResetConfirmation = true) }
  }

  fun dismissResetConfirmation() {
    _uiState.update { it.copy(showResetConfirmation = false) }
  }

  fun confirmReset() {
    _uiState.update {
      it.copy(
        currentIntakeMl = 0,
        history = emptyList(),
        showResetConfirmation = false
      )
    }
  }

  fun undoLastEntry() {
    _uiState.update { currentState ->
      if (currentState.history.isEmpty()) return@update currentState
      val removed = currentState.history.first()
      val newTotal = (currentState.currentIntakeMl - removed.amountMl).coerceAtLeast(0)
      currentState.copy(
        currentIntakeMl = newTotal,
        history = currentState.history.drop(1)
      )
    }
  }
}
