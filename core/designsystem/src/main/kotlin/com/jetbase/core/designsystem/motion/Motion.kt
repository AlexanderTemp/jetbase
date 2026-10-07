package com.jetbase.core.designsystem.motion

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

/** Easing curves and durations of the Material 3 motion system. */
object JetBaseMotion {
    const val DURATION_MEDIUM = 400
    const val DURATION_SHORT = 200

    val EmphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
    val EmphasizedAccelerate = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
    val Emphasized = CubicBezierEasing(0.2f, 0f, 0f, 1f)

    /** Fraction of the width the screen slides in the "shared axis X" pattern. */
    const val SLIDE_FRACTION = 0.10f

    /** Moment (fraction of the duration) when the outgoing screen has already faded out. */
    private const val FADE_THRESHOLD = 0.35f

    /**
     * Material "Shared Axis X" pattern: the new screen slides and fades in while the old one leaves
     * in the same direction. It communicates "moving forward/back in a hierarchy".
     */
    fun sharedAxisXIn(
        initialOffsetX: (fullWidth: Int) -> Int,
        durationMillis: Int = DURATION_MEDIUM,
    ): EnterTransition {
        val outgoing = (durationMillis * FADE_THRESHOLD).toInt()
        return slideInHorizontally(
            animationSpec = tween(durationMillis, easing = Emphasized),
            initialOffsetX = initialOffsetX,
        ) +
            fadeIn(
                animationSpec =
                    tween(
                        durationMillis = durationMillis - outgoing,
                        delayMillis = outgoing,
                        easing = LinearOutSlowInEasing,
                    )
            )
    }

    fun sharedAxisXOut(
        targetOffsetX: (fullWidth: Int) -> Int,
        durationMillis: Int = DURATION_MEDIUM,
    ): ExitTransition =
        slideOutHorizontally(
            animationSpec = tween(durationMillis, easing = Emphasized),
            targetOffsetX = targetOffsetX,
        ) +
            fadeOut(
                animationSpec =
                    tween(
                        durationMillis = (durationMillis * FADE_THRESHOLD).toInt(),
                        easing = FastOutLinearInEasing,
                    )
            )
}
