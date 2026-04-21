
package com.rekcode.yahtzee.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.rekcode.yahtzee.api.ScoreCategory
import com.rekcode.yahtzee.api.ScoreSheetItem
import com.rekcode.yahtzee.generated.AppColors
import com.rekcode.yahtzee.generated.resources.Res
import com.rekcode.yahtzee.generated.resources.action_exit
import com.rekcode.yahtzee.generated.resources.action_play_again
import com.rekcode.yahtzee.generated.resources.action_roll
import com.rekcode.yahtzee.generated.resources.celebration_yahtzee
import com.rekcode.yahtzee.generated.resources.dialog_game_over_score_line
import com.rekcode.yahtzee.generated.resources.dialog_game_over_title
import com.rekcode.yahtzee.generated.resources.dialog_game_over_winner
import com.rekcode.yahtzee.generated.resources.label_player
import com.rekcode.yahtzee.generated.resources.label_yahtzee_bonus
import com.rekcode.yahtzee.generated.resources.section_lower
import com.rekcode.yahtzee.generated.resources.section_upper
import com.rekcode.yahtzee.generated.resources.section_upper_bonus
import com.rekcode.yahtzee.ui.components.DiceView
import com.rekcode.yahtzee.ui.theme.Dimens
import com.rekcode.yahtzee.ui.theme.UiConstants
import com.rekcode.yahtzee.ui.theme.UiDimens
import com.rekcode.yahtzee.ui.theme.rememberUiDimens
import com.rekcode.yahtzee.ui.util.toDisplayStringRes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/**
 * Composable representing the primary game screen layout using
 * deterministic vertical partitioning and real score data binding.
 *
 * @param coordinator Coordinator that owns Game UI state and flow transitions.
 * @param onExitRequested Callback invoked when the user requests to leave the game screen.
 * @param onPlayAgainRequested Callback invoked when the user chooses to start a new game
 *                             with the same player count after game over.
 */
@Composable
fun GameScreen(
    coordinator: GameCoordinator,
    onExitRequested: () -> Unit,
    onPlayAgainRequested: () -> Unit
) {
    val uiState = coordinator.uiState

    val (upperSection, lowerSection) = remember(uiState.scoreSheet) {
        val splitIndex = uiState.scoreSheet.indexOfFirst {
            it.displayName.equals(
                UiConstants.ScoreSectionLowerBoundaryName,
                ignoreCase = true
            )
        }

        if (splitIndex != -1) {
            Pair(
                uiState.scoreSheet.subList(0, splitIndex),
                uiState.scoreSheet.subList(splitIndex, uiState.scoreSheet.size)
            )
        } else {
            Pair(uiState.scoreSheet, emptyList())
        }
    }

    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val uiDimens = rememberUiDimens(maxHeight)

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.weight(UiConstants.DiceSectionWeight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                DiceSection(
                    diceValues = uiState.diceValues,
                    heldStates = uiState.heldStates,
                    isRolling = uiState.isRolling,
                    onDieClick = { index ->
                        coordinator.onDieClicked(index)
                    },
                    uiDimens = uiDimens
                )
            }

            Column(
                modifier = Modifier.weight(UiConstants.ScoreSectionWeight)
            ) {
                ScoreSection(
                    currentPlayerIndex = uiState.currentPlayerIndex,
                    upperSection = upperSection,
                    lowerSection = lowerSection,
                    previewSheet = uiState.previewSheet,
                    upperBonusValue = uiState.upperBonusValue,
                    onScoreSelected = { category ->
                        coordinator.onScoreSelected(category)
                    },
                    uiDimens = uiDimens
                )
            }

            Column(
                modifier = Modifier.weight(UiConstants.ActionSectionWeight)
            ) {
                ActionSection(
                    isRollEnabled = uiState.isRollEnabled,
                    onRollRequested = {
                        coroutineScope.launch {
                            coordinator.beginRoll()
                            delay(UiConstants.DiceRollDurationMs)
                            coordinator.onRollCompleted()

                            val isYahtzee = coordinator.uiState.showYahtzeeCelebration
                            if (isYahtzee) {
                                delay(UiConstants.YahtzeeCelebrationDurationMs)
                                coordinator.dismissYahtzeeCelebration()
                            }
                        }
                    },
                    onExitRequested = onExitRequested,
                    uiDimens = uiDimens
                )
            }

            if (uiState.showGameOverDialog) {
                AlertDialog(
                    onDismissRequest = {},
                    confirmButton = {
                        OutlinedButton(
                            onClick = {
                                coordinator.dismissGameOver()
                                onPlayAgainRequested()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = AppColors.table_rail,
                                contentColor = AppColors.text_on_table
                            )
                        ) {
                            Text(
                                text = stringResource(Res.string.action_play_again),
                                fontWeight = UiConstants.ButtonLabelFontWeight,
                                fontSize = uiDimens.buttonLabelFontSize
                            )
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = onExitRequested,
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = AppColors.table_rail,
                                contentColor = AppColors.text_on_table
                            )
                        ) {
                            Text(
                                text = stringResource(Res.string.action_exit),
                                fontWeight = UiConstants.ButtonLabelFontWeight,
                                fontSize = uiDimens.buttonLabelFontSize
                            )
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(Res.string.dialog_game_over_title),
                            fontWeight = UiConstants.ButtonLabelFontWeight,
                            fontSize = uiDimens.playerHeaderTextSize,
                            color = AppColors.text_on_table
                        )
                    },
                    text = {
                        val scores = uiState.finalScores ?: emptyList()
                        val winner = uiState.winnerIndex ?: 0
                        val scoreLines = scores
                            .mapIndexed { index, score ->
                                val bonus = coordinator.getAdditionalYahtzeeBonus(index)

                                val baseLine = stringResource(
                                    Res.string.dialog_game_over_score_line,
                                    index + 1,
                                    score
                                )

                                if (bonus > 0) {
                                    val bonusLine = stringResource(
                                        Res.string.label_yahtzee_bonus,
                                        bonus
                                    )
                                    "$baseLine ($bonusLine)"
                                } else {
                                    baseLine
                                }
                            }
                            .joinToString(separator = "\n")

                        val winnerLine = stringResource(
                            Res.string.dialog_game_over_winner,
                            winner + 1
                        )

                        Text(
                            text = "$scoreLines\n\n$winnerLine",
                            fontSize = uiDimens.scoreTextSize,
                            color = AppColors.text_on_table
                        )
                    },
                    containerColor = AppColors.table_felt
                )
            }

            if (uiState.showYahtzeeCelebration) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            AppColors.table_felt.copy(alpha = UiConstants.YahtzeeCelebrationOverlayAlpha)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.celebration_yahtzee),
                        fontSize = uiDimens.playerHeaderTextSize,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.text_section_header,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            letterSpacing = uiDimens.yahtzeeCelebrationLetterSpacing
                        )
                    )
                }
            }
        }
    }
}

/**
 * Renders the dice area of the game screen.
 *
 * This composable remains placement-only.
 * Dice rendering and dice interaction behavior remain owned by [DiceView].
 *
 * @param diceValues Current face values of all dice.
 * @param heldStates Current held state of each die.
 * @param isRolling Whether dice animation is currently active.
 * @param onDieClick Callback when a die is tapped.
 * @param uiDimens Viewport-scaled measurement set for layout placement.
 */
@Composable
fun DiceSection(
    diceValues: List<Int>,
    heldStates: List<Boolean>,
    isRolling: Boolean,
    onDieClick: (Int) -> Unit,
    uiDimens: UiDimens
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(uiDimens.sectionSpacing))

        Column(
            verticalArrangement = Arrangement.spacedBy(uiDimens.diceSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    space = uiDimens.diceHorizontalSpacing,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.Top
            ) {
                DiceSlot(slotSize = uiDimens.diceSize) {
                    DiceView(
                        value = diceValues[0],
                        isHeld = heldStates[0],
                        isRolling = isRolling,
                        onClick = { onDieClick(0) }
                    )
                }

                DiceSlot(slotSize = uiDimens.diceSize) {
                    DiceView(
                        value = diceValues[1],
                        isHeld = heldStates[1],
                        isRolling = isRolling,
                        onClick = { onDieClick(1) }
                    )
                }

                DiceSlot(slotSize = uiDimens.diceSize) {
                    DiceView(
                        value = diceValues[2],
                        isHeld = heldStates[2],
                        isRolling = isRolling,
                        onClick = { onDieClick(2) }
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    space = uiDimens.diceHorizontalSpacing,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.Top
            ) {
                DiceSlot(slotSize = uiDimens.diceSize) {
                    DiceView(
                        value = diceValues[3],
                        isHeld = heldStates[3],
                        isRolling = isRolling,
                        onClick = { onDieClick(3) }
                    )
                }

                DiceSlot(slotSize = uiDimens.diceSize) {
                    DiceView(
                        value = diceValues[4],
                        isHeld = heldStates[4],
                        isRolling = isRolling,
                        onClick = { onDieClick(4) }
                    )
                }
            }
        }
    }
}

/**
 * Bounded placement slot for a die.
 *
 * This composable only defines the space allocated to the die.
 * The die itself retains full ownership of its rendering and behavior.
 *
 * @param slotSize Allocated square size for the die slot.
 * @param content Die content rendered inside the slot.
 */
@Composable
private fun DiceSlot(
    slotSize: Dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.size(slotSize),
        contentAlignment = Alignment.TopCenter
    ) {
        content()
    }
}

/**
 * Renders the full score section including upper section, bonus, and lower section.
 *
 * @param currentPlayerIndex Active player index.
 * @param upperSection Upper score entries.
 * @param lowerSection Lower score entries.
 * @param previewSheet Optional preview score sheet.
 * @param upperBonusValue Current upper section bonus value.
 * @param onScoreSelected Callback when a score category is selected.
 * @param uiDimens Viewport-scaled measurement set for layout placement.
 * @param modifier Modifier applied to the root score section container.
 */
@Composable
fun ScoreSection(
    currentPlayerIndex: Int,
    upperSection: List<ScoreSheetItem>,
    lowerSection: List<ScoreSheetItem>,
    previewSheet: List<ScoreSheetItem>?,
    upperBonusValue: Int,
    onScoreSelected: (ScoreCategory) -> Unit,
    uiDimens: UiDimens,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = uiDimens.screenPadding),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(
                Res.string.label_player,
                currentPlayerIndex + 1
            ),
            fontSize = uiDimens.playerHeaderTextSize,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = uiDimens.playerHeaderBottomSpacing),
            textAlign = TextAlign.Center,
            color = AppColors.text_on_table
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(uiDimens.scoreRowSpacing)
        ) {
            Text(
                text = stringResource(Res.string.section_upper),
                fontSize = uiDimens.scoreTextSize,
                color = AppColors.text_section_header
            )

            ScoreRowList(
                entries = upperSection,
                previewSheet = previewSheet,
                onScoreSelected = onScoreSelected,
                uiDimens = uiDimens
            )

            ScoreRow(
                label = stringResource(Res.string.section_upper_bonus),
                value = upperBonusValue.toString(),
                backgroundColor = UiConstants.ScoreRowEvenBackground,
                isEmphasized = true,
                uiDimens = uiDimens
            )

            Spacer(modifier = Modifier.height(uiDimens.sectionSpacing))

            if (lowerSection.isNotEmpty()) {
                Text(
                    text = stringResource(Res.string.section_lower),
                    fontSize = uiDimens.scoreTextSize,
                    color = AppColors.text_section_header
                )

                ScoreRowList(
                    entries = lowerSection,
                    previewSheet = previewSheet,
                    onScoreSelected = onScoreSelected,
                    uiDimens = uiDimens
                )
            }
        }
    }
}

/**
 * Renders a list of score rows for a given section.
 *
 * @param entries List of score sheet items to render.
 * @param previewSheet Optional preview score sheet.
 * @param onScoreSelected Callback invoked when a score category is selected.
 * @param uiDimens Viewport-scaled measurement set for layout placement.
 */
@Composable
fun ScoreRowList(
    entries: List<ScoreSheetItem>,
    previewSheet: List<ScoreSheetItem>?,
    onScoreSelected: (ScoreCategory) -> Unit,
    uiDimens: UiDimens
) {
    entries.forEachIndexed { index, entry ->
        ScoreRow(
            label = stringResource(entry.category.toDisplayStringRes()),
            value = resolveScoreDisplayValue(entry, previewSheet),
            backgroundColor = if (index % 2 == 0) {
                UiConstants.ScoreRowEvenBackground
            } else {
                UiConstants.ScoreRowOddBackground
            },
            isClickable = !entry.isLocked,
            isLocked = entry.isLocked,
            onClick = {
                if (!entry.isLocked) {
                    onScoreSelected(entry.category)
                }
            },
            uiDimens = uiDimens
        )
    }
}

/**
 * Stateless UI component representing a single row in the score sheet.
 *
 * @param label Display name for the score category.
 * @param value String representation of the score value.
 * @param backgroundColor Background color for the row.
 * @param isEmphasized Controls bold and highlighted text styling.
 * @param isClickable Whether the row responds to tap interactions.
 * @param isLocked Whether the row represents a locked score category.
 * @param onClick Lambda invoked when the row is clicked.
 * @param uiDimens Viewport-scaled measurement set for layout placement.
 */
@Composable
fun ScoreRow(
    label: String,
    value: String,
    backgroundColor: Color,
    uiDimens: UiDimens,
    isEmphasized: Boolean = false,
    isClickable: Boolean = false,
    isLocked: Boolean = false,
    onClick: () -> Unit = {}
) {
    val resolvedTextColor = when {
        isLocked -> AppColors.text_on_table.copy(alpha = UiConstants.LockedRowAlpha)
        isEmphasized -> AppColors.text_section_header
        else -> AppColors.text_on_table
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .then(
                if (isClickable) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
            .padding(horizontal = uiDimens.screenPadding)
    ) {
        Text(
            text = label,
            fontSize = uiDimens.scoreTextSize,
            fontWeight = if (isEmphasized) FontWeight.Bold else FontWeight.Normal,
            color = resolvedTextColor
        )
        Spacer(modifier = Modifier.weight(UiConstants.RowSpacerWeight))
        Text(
            text = value,
            fontSize = uiDimens.scoreTextSize,
            fontWeight = if (isEmphasized) FontWeight.Bold else FontWeight.Normal,
            color = resolvedTextColor,
            modifier = Modifier.width(uiDimens.scoreValueWidth),
            textAlign = TextAlign.End
        )
    }
}

/**
 * Resolves the display value for a score row based on lock state and preview data.
 *
 * @param entry The score sheet item representing the category.
 * @param previewSheet Optional preview score sheet.
 * @return String representation of the score for UI display.
 */
private fun resolveScoreDisplayValue(
    entry: ScoreSheetItem,
    previewSheet: List<ScoreSheetItem>?
): String {
    return when {
        entry.isLocked -> entry.score?.toString() ?: "-"
        previewSheet != null -> previewSheet
            .find { it.category == entry.category }
            ?.score?.toString() ?: "-"
        else -> "-"
    }
}

/**
 * Renders the primary action area for the game screen.
 *
 * @param isRollEnabled Whether the roll button is currently enabled.
 * @param onRollRequested Callback invoked when the roll action is requested.
 * @param onExitRequested Callback invoked when the exit action is requested.
 * @param uiDimens Viewport-scaled measurement set for layout placement.
 * @param modifier Modifier applied to the root container of the action section.
 */
@Composable
fun ActionSection(
    isRollEnabled: Boolean,
    onRollRequested: () -> Unit,
    onExitRequested: () -> Unit,
    uiDimens: UiDimens,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            horizontal = uiDimens.screenPadding,
            vertical = uiDimens.screenPadding
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(UiConstants.ButtonWidthFraction),
                horizontalArrangement = Arrangement.spacedBy(uiDimens.screenPadding)
            ) {
                OutlinedButton(
                    onClick = onRollRequested,
                    enabled = isRollEnabled,
                    modifier = Modifier.weight(UiConstants.ActionButtonWeight),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = AppColors.table_rail,
                        contentColor = AppColors.text_on_table,
                        disabledContainerColor = AppColors.button_disabled_background,
                        disabledContentColor = AppColors.text_disabled
                    )
                ) {
                    Text(
                        text = stringResource(Res.string.action_roll),
                        fontWeight = UiConstants.ButtonLabelFontWeight,
                        fontSize = uiDimens.buttonLabelFontSize
                    )
                }

                OutlinedButton(
                    onClick = onExitRequested,
                    modifier = Modifier.weight(UiConstants.ActionButtonWeight),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = AppColors.table_rail,
                        contentColor = AppColors.text_on_table
                    )
                ) {
                    Text(
                        text = stringResource(Res.string.action_exit),
                        fontWeight = UiConstants.ButtonLabelFontWeight,
                        fontSize = uiDimens.buttonLabelFontSize
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(uiDimens.sectionSpacing))
    }
}