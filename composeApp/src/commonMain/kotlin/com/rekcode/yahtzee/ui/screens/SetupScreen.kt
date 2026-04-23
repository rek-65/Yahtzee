
package com.rekcode.yahtzee.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.rekcode.yahtzee.generated.AppColors
import com.rekcode.yahtzee.ui.components.DiceView
import com.rekcode.yahtzee.ui.theme.UiConstants
import com.rekcode.yahtzee.ui.theme.UiDimens
import com.rekcode.yahtzee.ui.theme.rememberUiDimens
import com.rekcode.yahtzee.generated.resources.Res
import com.rekcode.yahtzee.generated.resources.action_decrement
import com.rekcode.yahtzee.generated.resources.action_exit
import com.rekcode.yahtzee.generated.resources.action_increment
import com.rekcode.yahtzee.generated.resources.action_start_game
import org.jetbrains.compose.resources.stringResource

/**
 * Displays the setup screen for selecting player count and starting the game.
 *
 * Provides controls for adjusting the number of players and initiating or exiting the game.
 * This composable emits events only and does not manage navigation logic.
 *
 * @param onStartGame Callback invoked with the selected player count when starting the game.
 * @param onExitRequested Callback invoked when the user requests to exit the application.
 * @param instructionsButtonLabel Label displayed on the instructions button.
 * @param onInstructionsRequested Callback invoked when the user requests to view instructions.
 * */
@Composable
fun SetupScreen(
    onStartGame: (playerCount: Int) -> Unit,
    onExitRequested: () -> Unit,
    instructionsButtonLabel: String,
    onInstructionsRequested: () -> Unit
) {
    var playerCount by remember { mutableIntStateOf(UiConstants.DefaultPlayerCount) }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val uiDimens = rememberUiDimens(maxHeight)

        Box(modifier = Modifier.fillMaxSize()) {
            DecorativeDie(
                value = 1,
                uiDimens = uiDimens,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(
                        start = uiDimens.setupDiceEdgePadding,
                        top = uiDimens.setupDiceEdgePadding
                    )
            )

            DecorativeDie(
                value = 2,
                uiDimens = uiDimens,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        end = uiDimens.setupDiceEdgePadding,
                        top = uiDimens.setupDiceEdgePadding
                    )
            )

            DecorativeDie(
                value = 3,
                uiDimens = uiDimens,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(
                        start = uiDimens.setupDiceEdgePadding,
                        bottom = uiDimens.setupDiceBottomPadding + uiDimens.screenPadding
                    )
            )

            DecorativeDie(
                value = 4,
                uiDimens = uiDimens,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = uiDimens.setupDiceEdgePadding,
                        bottom = uiDimens.setupDiceBottomPadding + uiDimens.screenPadding
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(uiDimens.screenPadding),
                verticalArrangement = Arrangement.spacedBy(
                    space = uiDimens.itemStackSpacing,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PlayerStepper(
                    playerCount = playerCount,
                    onDecrement = {
                        if (playerCount > UiConstants.MinPlayerCount) {
                            playerCount--
                        }
                    },
                    onIncrement = {
                        if (playerCount < UiConstants.MaxPlayerCount) {
                            playerCount++
                        }
                    },
                    uiDimens = uiDimens
                )

                Button(
                    onClick = { onStartGame(playerCount) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.table_rail,
                        contentColor = AppColors.text_on_table
                    )
                ) {
                    Text(
                        text = stringResource(Res.string.action_start_game),
                        fontWeight = UiConstants.ButtonLabelFontWeight,
                        fontSize = uiDimens.buttonLabelFontSize
                    )
                }

                Button(
                    onClick = onInstructionsRequested,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.table_rail,
                        contentColor = AppColors.text_on_table
                    )
                ) {
                    Text(
                        text = instructionsButtonLabel,
                        fontWeight = UiConstants.ButtonLabelFontWeight,
                        fontSize = uiDimens.buttonLabelFontSize
                    )
                }

                Button(
                    onClick = onExitRequested,
                    colors = ButtonDefaults.buttonColors(
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
    }
}

/**
 * Displays a stepper control for selecting player count.
 *
 * @param playerCount Current player count.
 * @param onDecrement Callback for decrement action.
 * @param onIncrement Callback for increment action.
 * @param uiDimens Viewport-scaled measurement set for layout placement.
 */
@Composable
private fun PlayerStepper(
    playerCount: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    uiDimens: UiDimens
) {
    val textOnTable = AppColors.text_on_table
    val borderColor = textOnTable.copy(alpha = UiConstants.StepperButtonBorderAlpha)
    val containerBackground = AppColors.table_rail

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(containerBackground, RoundedCornerShape(uiDimens.stepperCornerRadius))
            .padding(uiDimens.stepperContainerPadding)
    ) {
        StepperButton(
            label = stringResource(Res.string.action_decrement),
            onClick = onDecrement,
            enabled = playerCount > UiConstants.MinPlayerCount,
            borderColor = borderColor,
            contentColor = textOnTable,
            uiDimens = uiDimens
        )

        Text(
            text = playerCount.toString(),
            modifier = Modifier.size(
                width = uiDimens.stepperCountWidth,
                height = uiDimens.buttonHeight
            ),
            textAlign = TextAlign.Center,
            color = textOnTable,
            fontWeight = UiConstants.ButtonLabelFontWeight,
            fontSize = uiDimens.buttonLabelFontSize
        )

        StepperButton(
            label = stringResource(Res.string.action_increment),
            onClick = onIncrement,
            enabled = playerCount < UiConstants.MaxPlayerCount,
            borderColor = borderColor,
            contentColor = textOnTable,
            uiDimens = uiDimens
        )
    }
}

/**
 * Displays a circular stepper button.
 *
 * @param label Button label text.
 * @param onClick Callback invoked when the button is pressed.
 * @param enabled Whether the button is enabled.
 * @param borderColor Border color for the button.
 * @param contentColor Content color for the button.
 * @param uiDimens Viewport-scaled measurement set for layout placement.
 */
@Composable
private fun StepperButton(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean,
    borderColor: Color,
    contentColor: Color,
    uiDimens: UiDimens
) {
    val alpha = if (enabled) 1f else UiConstants.ButtonBorderDisabledAlpha

    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .size(uiDimens.buttonHeight)
            .border(
                width = uiDimens.buttonBorderWidth,
                color = borderColor.copy(alpha = alpha),
                shape = CircleShape
            )
    ) {
        Text(
            text = label,
            color = contentColor.copy(alpha = alpha),
            fontWeight = UiConstants.ButtonLabelFontWeight,
            fontSize = uiDimens.buttonLabelFontSize
        )
    }
}

/**
 * Displays a decorative die in a corner.
 *
 * This composable defines only the bounded placement slot for the decorative die.
 * Die rendering behavior remains owned by [DiceView].
 *
 * @param value The face value to display on the die.
 * @param uiDimens Viewport-scaled measurement set for layout placement.
 * @param modifier Modifier carrying alignment and padding from the parent scope.
 */
@Composable
private fun DecorativeDie(
    value: Int,
    uiDimens: UiDimens,
    modifier: Modifier
) {
    Box(
        modifier = modifier.size(uiDimens.diceSize),
        contentAlignment = Alignment.TopCenter
    ) {
        DiceView(
            value = value,
            isHeld = false,
            isRolling = false,
            onClick = {}
        )
    }
}