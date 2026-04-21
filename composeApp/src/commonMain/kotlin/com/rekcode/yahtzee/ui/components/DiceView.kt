package com.rekcode.yahtzee.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.rekcode.yahtzee.generated.AppColors
import com.rekcode.yahtzee.generated.resources.Res
import com.rekcode.yahtzee.generated.resources.dice_1
import com.rekcode.yahtzee.generated.resources.dice_2
import com.rekcode.yahtzee.generated.resources.dice_3
import com.rekcode.yahtzee.generated.resources.dice_4
import com.rekcode.yahtzee.generated.resources.dice_5
import com.rekcode.yahtzee.generated.resources.dice_6
import com.rekcode.yahtzee.generated.resources.dice_blank
import com.rekcode.yahtzee.ui.theme.Dimens
import com.rekcode.yahtzee.ui.theme.UiConstants
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Displays a single die with support for:
 * - Rolling animation (bounce + rotation)
 * - Held state visualization
 * - Press interaction feedback
 *
 * This composable is stateless and fully driven by input parameters.
 * All visual resource ownership for the die remains internal to this component.
 *
 * @param value Current dice value (1–6). Invalid values render a blank die.
 * @param isHeld Whether the die is currently held (locked).
 * @param isRolling Whether the die is actively rolling.
 * @param onClick Callback invoked when the die is tapped.
 */
@Composable
fun DiceView(
    value: Int,
    isHeld: Boolean,
    isRolling: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val shouldAnimate = isRolling && !isHeld

    val transition = rememberInfiniteTransition(label = "dice_transition")

    val animatedScale = transition.animateFloat(
        initialValue = UiConstants.DiceBounceScaleMin,
        targetValue = UiConstants.DiceBounceScaleMax,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = UiConstants.DiceBounceDurationMs,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_anim"
    )

    val animatedOffsetY = transition.animateFloat(
        initialValue = 0f,
        targetValue = -Dimens.DiceBounceOffset.value,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = UiConstants.DiceBounceDurationMs,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset_anim"
    )

    val animatedRotation = transition.animateFloat(
        initialValue = 0f,
        targetValue = UiConstants.DiceRotationDegrees,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = UiConstants.DiceRotationDurationMs,
                easing = LinearEasing
            )
        ),
        label = "rotation_anim"
    )

    val scale = when {
        shouldAnimate -> animatedScale.value
        isPressed -> UiConstants.DicePressScale
        else -> UiConstants.DiceBounceScaleMin
    }

    val offsetY = if (shouldAnimate) {
        animatedOffsetY.value.dp
    } else {
        0.dp
    }

    val rotation = if (shouldAnimate) animatedRotation.value else 0f

    val backgroundColor = if (isHeld) {
        AppColors.dice_held_background
    } else {
        Color.Transparent
    }

    val shape = RoundedCornerShape(Dimens.DiceHeldCornerRadius)
    val drawable = getDieDrawable(value)

    Box(
        modifier = Modifier
            .offset(y = Dimens.DiceVerticalOffset + offsetY)
            .size(Dimens.DiceSize)
            .scale(scale)
            .graphicsLayer { rotationZ = rotation }
            .background(color = backgroundColor, shape = shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = "Die showing $value",
            modifier = Modifier.size(Dimens.DiceSize)
        )
    }
}

/**
 * Resolves the drawable resource for the provided die value.
 *
 * @param value Current dice value.
 * @return The drawable resource representing the die face.
 */
private fun getDieDrawable(value: Int): DrawableResource {
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

