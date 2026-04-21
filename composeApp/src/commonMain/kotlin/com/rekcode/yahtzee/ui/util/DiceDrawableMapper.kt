
package com.rekcode.yahtzee.ui.util

import org.jetbrains.compose.resources.DrawableResource
import com.rekcode.yahtzee.generated.resources.Res
import com.rekcode.yahtzee.generated.resources.dice_1
import com.rekcode.yahtzee.generated.resources.dice_2
import com.rekcode.yahtzee.generated.resources.dice_3
import com.rekcode.yahtzee.generated.resources.dice_4
import com.rekcode.yahtzee.generated.resources.dice_5
import com.rekcode.yahtzee.generated.resources.dice_6
import com.rekcode.yahtzee.generated.resources.dice_blank

/**
 * Utility object responsible for mapping dice face values to drawable resources.
 *
 * This class contains no game logic and serves only as a UI-layer translation
 * between engine-provided dice values and visual representations.
 *
 * This implementation uses Compose Multiplatform resource accessors instead
 * of Android-specific R.drawable references.
 */
object DiceDrawableMapper {

    /**
     * Returns the drawable resource corresponding to a dice face value.
     *
     * @param value The dice face value (expected range: 1–6). Any value outside
     * this range will return the blank dice drawable.
     *
     * @return The DrawableResource representing the given dice value.
     */
    fun getDrawableForValue(value: Int?): DrawableResource {
        return when (value) {
            1 -> Res.drawable.dice_1
            2 -> Res.drawable.dice_2
            3 -> Res.drawable.dice_3
            4 -> Res.drawable.dice_4
            5 -> Res.drawable.dice_5
            6 -> Res.drawable.dice_6
            else -> Res.drawable.dice_blank
        }
    }
}