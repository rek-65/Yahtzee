
package com.rekcode.yahtzee.ui.util

import com.rekcode.yahtzee.api.ScoreCategory
import com.rekcode.yahtzee.generated.resources.Res
import org.jetbrains.compose.resources.StringResource
import com.rekcode.yahtzee.generated.resources.score_ones
import com.rekcode.yahtzee.generated.resources.score_twos
import com.rekcode.yahtzee.generated.resources.score_threes
import com.rekcode.yahtzee.generated.resources.score_fours
import com.rekcode.yahtzee.generated.resources.score_fives
import com.rekcode.yahtzee.generated.resources.score_sixes
import com.rekcode.yahtzee.generated.resources.score_three_of_a_kind
import com.rekcode.yahtzee.generated.resources.score_four_of_a_kind
import com.rekcode.yahtzee.generated.resources.score_full_house
import com.rekcode.yahtzee.generated.resources.score_small_straight
import com.rekcode.yahtzee.generated.resources.score_large_straight
import com.rekcode.yahtzee.generated.resources.score_yahtzee
import com.rekcode.yahtzee.generated.resources.score_chance

/**
 * Maps ScoreCategory values to displayable string resources.
 *
 * This function provides a UI-layer translation from engine-provided
 * score categories to localized display strings.
 *
 * No game logic is performed here.
 */
fun ScoreCategory.toDisplayStringRes(): StringResource {
    return when (this) {
        ScoreCategory.ONES -> Res.string.score_ones
        ScoreCategory.TWOS -> Res.string.score_twos
        ScoreCategory.THREES -> Res.string.score_threes
        ScoreCategory.FOURS -> Res.string.score_fours
        ScoreCategory.FIVES -> Res.string.score_fives
        ScoreCategory.SIXES -> Res.string.score_sixes

        ScoreCategory.THREE_OF_A_KIND -> Res.string.score_three_of_a_kind
        ScoreCategory.FOUR_OF_A_KIND -> Res.string.score_four_of_a_kind
        ScoreCategory.FULL_HOUSE -> Res.string.score_full_house
        ScoreCategory.SMALL_STRAIGHT -> Res.string.score_small_straight
        ScoreCategory.LARGE_STRAIGHT -> Res.string.score_large_straight
        ScoreCategory.YAHTZEE -> Res.string.score_yahtzee
        ScoreCategory.CHANCE -> Res.string.score_chance
    }
}