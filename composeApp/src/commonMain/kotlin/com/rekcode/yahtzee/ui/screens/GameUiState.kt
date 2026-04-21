
package com.rekcode.yahtzee.ui.screens

import com.rekcode.yahtzee.api.ScoreSheetItem

/**
 * Represents the complete UI state for the Game screen.
 *
 * This data class is immutable and contains all values required for rendering
 * the game interface. It acts as a pure UI model and does not contain any
 * business or game logic.
 *
 * @param currentPlayerIndex Index of the currently active player
 * @param scoreSheet Score sheet displayed for the active player
 * @param diceValues Current face values of all dice
 * @param heldStates Held state for each die
 * @param isRolling Indicates whether dice are currently in a rolling state
 * @param isRollEnabled Indicates whether the roll action is currently allowed
 * @param previewSheet Optional preview score sheet based on current dice values
 * @param upperBonusValue Current upper section bonus value
 * @param showGameOverDialog Indicates whether the game over dialog should be displayed
 * @param finalScores Final scores for all players when the game is complete
 * @param winnerIndex Index of the winning player when the game is complete
 * @param showYahtzeeCelebration Indicates whether the Yahtzee celebration should be displayed
 */
data class GameUiState(
    val currentPlayerIndex: Int,
    val scoreSheet: List<ScoreSheetItem>,
    val diceValues: List<Int>,
    val heldStates: List<Boolean>,
    val isRolling: Boolean,
    val isRollEnabled: Boolean,
    val previewSheet: List<ScoreSheetItem>?,
    val upperBonusValue: Int,
    val showGameOverDialog: Boolean,
    val finalScores: List<Int>?,
    val winnerIndex: Int?,
    val showYahtzeeCelebration: Boolean
)