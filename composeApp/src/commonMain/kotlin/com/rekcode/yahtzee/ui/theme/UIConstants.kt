
package com.rekcode.yahtzee.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

/**
 * Centralized non-measurement UI constants for the application.
 *
 * Responsibilities:
 * - Provide a single source of truth for reusable UI values
 * - Ensure consistency across layouts and components
 * - Prevent hardcoded ("magic") values in composables
 *
 * Covers:
 * - Color values used for UI support (previews, alternating rows)
 * - Layout weight and fraction constants (Float)
 * - Alpha constants (Float)
 * - Animation duration constants (Long)
 * - Ratio constants (Float)
 * - String display constants
 * - Game setup bounds (Int)
 * - Typography weight constants
 *
 * This object does NOT:
 * - Contain business logic
 * - Manage state
 * - Reference game engine data
 * - Hold dimensional or measurement values (see Dimens)
 */
object UiConstants {

    // ------------------------------------------------------------------------
    // Colors (UI Support)
    // ------------------------------------------------------------------------

    /**
     * Neutral background color used for component previews in Android Studio.
     */
    val PreviewBackgroundColor = Color(0xFF2E2E2E)

    /**
     * Background color for even-indexed score rows.
     * Fully transparent — inherits the surface beneath.
     */
    val ScoreRowEvenBackground = Color(0x00000000)

    /**
     * Background color for odd-indexed score rows.
     * Subtle white tint to create visual row separation on dark surfaces.
     */
    val ScoreRowOddBackground = Color(0x1AFFFFFF)

    // ------------------------------------------------------------------------
    // Layout - Game Screen Section Weights
    // ------------------------------------------------------------------------

    /**
     * Number of dice used in the game.
     */
    const val DiceCount = 5

    /**
     * Vertical layout weight assigned to the dice display section.
     */
    const val DiceSectionWeight = 0.33f

    /**
     * Vertical layout weight assigned to the score sheet section.
     */
    const val ScoreSectionWeight = 0.60f

    /**
     * Vertical layout weight assigned to the action button section.
     */
    const val ActionSectionWeight = 0.10f

    // ------------------------------------------------------------------------
    // Layout - Canonical App Aspect Ratio
    // ------------------------------------------------------------------------

    /**
     * Canonical application aspect ratio width component.
     *
     * Defines the horizontal component of the fixed application rendering ratio.
     * Used to maintain consistent UI proportions across all platforms.
     *
     * Ratio defined as width : height.
     */
    const val AppAspectRatioWidth = 4.5f

    /**
     * Canonical application aspect ratio height component.
     *
     * Defines the vertical component of the fixed application rendering ratio.
     * See [AppAspectRatioWidth] for full ratio definition.
     */
    const val AppAspectRatioHeight = 10f

    // ------------------------------------------------------------------------
    // Layout - Canonical Design Viewport
    // ------------------------------------------------------------------------

    /**
     * Canonical design viewport width for the application.
     *
     * This value defines the fixed logical design-space width used as the
     * internal sizing reference for all shared UI layout and scaling.
     *
     * The application layout is designed from this baseline rather than from
     * raw platform window size.
     */
    const val DesignViewportWidth = 720f

    /**
     * Canonical design viewport height for the application.
     *
     * This value defines the fixed logical design-space height used as the
     * internal sizing reference for all shared UI layout and scaling.
     *
     * Together with [DesignViewportWidth], this establishes the fixed internal
     * viewport used for cross-platform scaling behavior.
     */
    const val DesignViewportHeight = 1280f

    // ------------------------------------------------------------------------
    // Layout - Button Weights and Fractions
    // ------------------------------------------------------------------------

    /**
     * Equal weight applied to each action button within a shared row.
     */
    const val ActionButtonWeight = 1f

    /**
     * Width fraction applied to primary buttons on setup screens.
     */
    const val ButtonWidthFraction = 0.6f

    /**
     * Weight used for row spacer alignment within scored rows.
     */
    const val RowSpacerWeight = 1f

    // ------------------------------------------------------------------------
    // Buttons - State Alpha
    // ------------------------------------------------------------------------

    /**
     * Alpha applied to the border of an enabled action button.
     */
    const val ButtonBorderEnabledAlpha = 0.5f

    /**
     * Alpha applied to the border of a disabled action button.
     */
    const val ButtonBorderDisabledAlpha = 0.3f

    // ------------------------------------------------------------------------
    // Score Rows - State Alpha
    // ------------------------------------------------------------------------

    /**
     * Alpha applied to locked score row text and value to signal
     * the category is no longer selectable by the current player.
     */
    const val LockedRowAlpha = 0.4f

    // ------------------------------------------------------------------------
// Dice - Animation & Interaction
// ------------------------------------------------------------------------

    /**
     * Minimum scale applied during dice bounce animation.
     */
    const val DiceBounceScaleMin = 1.0f

    /**
     * Maximum scale applied during dice bounce animation.
     */
    const val DiceBounceScaleMax = 1.08f

    /**
     * Duration of one bounce animation cycle in milliseconds.
     */
    const val DiceBounceDurationMs = 400

    /**
     * Scale applied when the die is pressed.
     */
    const val DicePressScale = 0.92f

    /**
     * Duration of one full dice rotation cycle in milliseconds.
     */
    const val DiceRotationDurationMs = 700

    /**
     * Total degrees rotated during one animation cycle.
     */
    const val DiceRotationDegrees = 360f

    /**
     * Duration in milliseconds for the dice rolling phase.
     *
     * This represents the logical roll duration used by the game flow,
     * not the visual animation cycle timing.
     *
     * Must remain separate from animation timing to support:
     * - Deterministic game flow
     * - Future multiplayer synchronization
     */
    const val DiceRollDurationMs = 600L

    // ------------------------------------------------------------------------
    // Celebration
    // ------------------------------------------------------------------------

    /**
     * Duration in milliseconds for the Yahtzee celebration overlay.
     */
    const val YahtzeeCelebrationDurationMs = 2000L

    /**
     * Alpha value applied to the full-screen overlay during the Yahtzee celebration.
     *
     * This value controls the dimming intensity of the background while the
     * celebration message is displayed. The overlay remains translucent so that
     * the underlying game state is still visible to the player.
     */
    const val YahtzeeCelebrationOverlayAlpha: Float = 0.85f

    // ------------------------------------------------------------------------
    // Game Logic Support - Score Sheet
    // ------------------------------------------------------------------------

    /**
     * Display name used to identify the boundary between the upper and lower
     * score sections within the score sheet.
     *
     * This value is intentionally aligned with the display name provided by the
     * Yahtzee game engine API for the "Three of a Kind" category.
     *
     * Architectural Contract:
     * - This constant must remain synchronized with the engine's category
     *   display names.
     * - It is used by the UI layer to determine where to split the score sheet
     *   into upper and lower sections.
     *
     * Any change to engine-provided display names must be reflected here to
     * maintain correct UI behavior.
     */
    const val ScoreSectionLowerBoundaryName = "Three of a Kind"

    // ------------------------------------------------------------------------
    // Setup Screen - Player Stepper Bounds
    // ------------------------------------------------------------------------

    /**
     * Minimum selectable player count on the setup screen.
     */
    const val MinPlayerCount = 1

    /**
     * Maximum selectable player count on the setup screen.
     */
    const val MaxPlayerCount = 4

    /**
     * Default player count shown when the setup screen first loads.
     */
    const val DefaultPlayerCount = 1

    /**
     * Alpha applied to the stepper button border to soften contrast
     * against the table surface while remaining visible.
     */
    const val StepperButtonBorderAlpha = 0.6f

    // ------------------------------------------------------------------------
    // Typography - Weight
    // ------------------------------------------------------------------------

    /**
     * Font weight applied to button and stepper control labels.
     * Bold weight ensures readability against the table_rail background.
     */
    val ButtonLabelFontWeight = FontWeight.Bold
}