
package com.rekcode.yahtzee.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.pow

/**
 * Centralized base measurement constants for the application.
 *
 * Responsibilities:
 * - Define canonical measurement values for the UI.
 * - Provide a single source of truth for viewport-scaled dimensions.
 * - Prevent hardcoded dp and sp values in composables.
 *
 * Architectural Notes:
 * - Base values represent the canonical design system.
 * - Runtime UI sizing must use [UiDimens] produced by [rememberUiDimens].
 * - Scaling is driven by viewport height to preserve vertical layout fidelity.
 */
object Dimens {

    /**
     * Canonical root screen padding.
     */
    val ScreenPadding = 20.dp

    /**
     * Canonical spacing between major UI sections.
     */
    val SectionSpacing = 2.dp

    /**
     * Canonical spacing between vertically stacked interactive controls.
     */
    val ItemStackSpacing = 24.dp

    /**
     * Canonical large vertical spacing value.
     */
    val VerticalSpacingLarge = 24.dp

    /**
     * Canonical medium vertical spacing value.
     */
    val VerticalSpacingMedium = 16.dp

    /**
     * Canonical splash image size.
     */
    val SplashImageSize = 200.dp

    /**
     * Canonical button height.
     */
    val ButtonHeight = 56.dp

    /**
     * Canonical button corner radius.
     */
    val ButtonCornerRadius = 8.dp

    /**
     * Canonical outlined button border width.
     */
    val ButtonBorderWidth = 2.dp

    /**
     * Canonical setup-screen edge padding for decorative dice.
     */
    val SetupDiceEdgePadding = 8.dp

    /**
     * Canonical setup-screen bottom padding for decorative dice.
     */
    val SetupDiceBottomPadding = 120.dp

    /**
     * Canonical width of the player stepper count display.
     */
    val StepperCountWidth = 48.dp

    /**
     * Canonical stepper button size.
     */
    val StepperButtonSize = 40.dp

    /**
     * Canonical stepper corner radius.
     */
    val StepperCornerRadius = 12.dp

    /**
     * Canonical stepper internal padding.
     */
    val StepperContainerPadding = 12.dp

    /**
     * Canonical vertical spacing between dice rows.
     */
    val DiceSpacing = 5.dp

    /**
     * Canonical held-die corner radius.
     */
    val DiceHeldCornerRadius = 12.dp

    /**
     * Canonical horizontal spacing between dice in a row.
     */
    val DiceHorizontalSpacing = 8.dp

    /**
     * Canonical die size.
     */
    val DiceSize = 140.dp

    /**
     * Canonical die vertical offset.
     */
    val DiceVerticalOffset = 48.dp

    /**
     * Canonical dice bounce offset.
     */
    val DiceBounceOffset = (-24).dp

    /**
     * Canonical score value column width.
     */
    val ScoreValueWidth = 48.dp

    /**
     * Canonical spacing between score rows.
     */
    val ScoreRowSpacing = 1.dp

    /**
     * Canonical spacing below the player header.
     */
    val PlayerHeaderBottomSpacing = 4.dp

    /**
     * Canonical score text size.
     */
    val ScoreTextSize = 20.sp

    /**
     * Canonical player header text size.
     */
    val PlayerHeaderTextSize = 26.sp

    /**
     * Canonical button label text size.
     */
    val ButtonLabelFontSize = 20.sp

    /**
     * Canonical Yahtzee celebration letter spacing.
     */
    val YahtzeeCelebrationLetterSpacing = 6.sp
}

/**
 * Viewport-scaled measurement set used by all UI composables.
 *
 * @property scaleFactor Height-derived scale factor relative to the canonical design viewport.
 * @property screenPadding Scaled root screen padding.
 * @property sectionSpacing Scaled spacing between major UI sections.
 * @property itemStackSpacing Scaled spacing between vertically stacked controls.
 * @property verticalSpacingLarge Scaled large vertical spacing.
 * @property verticalSpacingMedium Scaled medium vertical spacing.
 * @property splashImageSize Scaled splash image size.
 * @property buttonHeight Scaled button height.
 * @property buttonCornerRadius Scaled button corner radius.
 * @property buttonBorderWidth Scaled outlined button border width.
 * @property setupDiceEdgePadding Scaled setup-screen edge padding for decorative dice.
 * @property setupDiceBottomPadding Scaled setup-screen bottom padding for decorative dice.
 * @property stepperCountWidth Scaled width of the player stepper count display.
 * @property stepperButtonSize Scaled stepper button size.
 * @property stepperCornerRadius Scaled stepper corner radius.
 * @property stepperContainerPadding Scaled stepper internal padding.
 * @property diceSpacing Scaled spacing between dice rows.
 * @property diceHeldCornerRadius Scaled held-die corner radius.
 * @property diceHorizontalSpacing Scaled spacing between dice in a row.
 * @property diceSize Scaled die size.
 * @property diceVerticalOffset Scaled die vertical offset.
 * @property diceBounceOffset Scaled dice bounce offset.
 * @property scoreValueWidth Scaled score value column width.
 * @property scoreRowSpacing Scaled spacing between score rows.
 * @property playerHeaderBottomSpacing Scaled spacing below the player header.
 * @property scoreTextSize Scaled score text size.
 * @property playerHeaderTextSize Scaled player header text size.
 * @property buttonLabelFontSize Scaled button label text size.
 * @property yahtzeeCelebrationLetterSpacing Scaled Yahtzee celebration letter spacing.
 */
@Immutable
data class UiDimens(
    val scaleFactor: Float,
    val screenPadding: Dp,
    val sectionSpacing: Dp,
    val itemStackSpacing: Dp,
    val verticalSpacingLarge: Dp,
    val verticalSpacingMedium: Dp,
    val splashImageSize: Dp,
    val buttonHeight: Dp,
    val buttonCornerRadius: Dp,
    val buttonBorderWidth: Dp,
    val setupDiceEdgePadding: Dp,
    val setupDiceBottomPadding: Dp,
    val stepperCountWidth: Dp,
    val stepperButtonSize: Dp,
    val stepperCornerRadius: Dp,
    val stepperContainerPadding: Dp,
    val diceSpacing: Dp,
    val diceHeldCornerRadius: Dp,
    val diceHorizontalSpacing: Dp,
    val diceSize: Dp,
    val diceVerticalOffset: Dp,
    val diceBounceOffset: Dp,
    val scoreValueWidth: Dp,
    val scoreRowSpacing: Dp,
    val playerHeaderBottomSpacing: Dp,
    val scoreTextSize: TextUnit,
    val playerHeaderTextSize: TextUnit,
    val buttonLabelFontSize: TextUnit,
    val yahtzeeCelebrationLetterSpacing: TextUnit
)

/**
 * Creates and remembers a viewport-scaled measurement set.
 *
 * Scaling is derived only from viewport height and the canonical design
 * viewport height declared in [UiConstants.DesignViewportHeight].
 *
 * @param viewportHeight Current resolved screen viewport height.
 * @return Viewport-scaled UI measurements.
 */
@Composable
fun rememberUiDimens(
    viewportHeight: Dp
): UiDimens {
    val scaleFactor = viewportHeight.value / UiConstants.DesignViewportHeight
    val scoreRowSpacingFactor =
        ((viewportHeight.value - 800f) / 200f).coerceAtLeast(0f)
    val textScaleFactor = scaleFactor * 1.05f

    return remember(viewportHeight) {
        UiDimens(
            scaleFactor = scaleFactor,
            screenPadding = Dimens.ScreenPadding * scaleFactor,
            sectionSpacing = Dimens.SectionSpacing * scaleFactor,
            itemStackSpacing = Dimens.ItemStackSpacing * scaleFactor,
            verticalSpacingLarge = Dimens.VerticalSpacingLarge * scaleFactor,
            verticalSpacingMedium = Dimens.VerticalSpacingMedium * scaleFactor,
            splashImageSize = Dimens.SplashImageSize * scaleFactor,
            buttonHeight = Dimens.ButtonHeight * scaleFactor,
            buttonCornerRadius = Dimens.ButtonCornerRadius * scaleFactor,
            buttonBorderWidth = Dimens.ButtonBorderWidth * scaleFactor,
            setupDiceEdgePadding = Dimens.SetupDiceEdgePadding * scaleFactor,
            setupDiceBottomPadding = Dimens.SetupDiceBottomPadding * scaleFactor,
            stepperCountWidth = Dimens.StepperCountWidth * scaleFactor,
            stepperButtonSize = Dimens.StepperButtonSize * scaleFactor,
            stepperCornerRadius = Dimens.StepperCornerRadius * scaleFactor,
            stepperContainerPadding = Dimens.StepperContainerPadding * scaleFactor,
            diceSpacing = Dimens.DiceSpacing * scaleFactor,
            diceHeldCornerRadius = Dimens.DiceHeldCornerRadius * scaleFactor,
            diceHorizontalSpacing = Dimens.DiceHorizontalSpacing * scaleFactor,
            diceSize = Dimens.DiceSize * scaleFactor,
            diceVerticalOffset = Dimens.DiceVerticalOffset * scaleFactor,
            diceBounceOffset = Dimens.DiceBounceOffset * scaleFactor,
            scoreValueWidth = Dimens.ScoreValueWidth * scaleFactor,
            scoreRowSpacing = Dimens.ScoreRowSpacing,
            playerHeaderBottomSpacing = Dimens.PlayerHeaderBottomSpacing * scaleFactor,
            scoreTextSize = Dimens.ScoreTextSize * textScaleFactor,
            playerHeaderTextSize = Dimens.PlayerHeaderTextSize * textScaleFactor,
            buttonLabelFontSize = Dimens.ButtonLabelFontSize * textScaleFactor,
            yahtzeeCelebrationLetterSpacing =
                Dimens.YahtzeeCelebrationLetterSpacing * textScaleFactor
        )
    }
}