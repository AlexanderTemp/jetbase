package com.jetbase.core.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.navigation3.scene.Scene
import com.jetbase.core.designsystem.motion.JetBaseMotion

/**
 * Material "Shared Axis X" transitions for NavDisplay (the idea Seal applies to every screen).
 *
 * In Navigation 3 transitions are configured ONCE on NavDisplay instead of on every destination.
 */
object NavTransitions {
    private const val OFFSET = JetBaseMotion.SLIDE_FRACTION

    fun <T : Any> forward(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
        JetBaseMotion.sharedAxisXIn(initialOffsetX = { (it * OFFSET).toInt() }) togetherWith
            JetBaseMotion.sharedAxisXOut(targetOffsetX = { -(it * OFFSET).toInt() })
    }

    fun <T : Any> pop(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
        JetBaseMotion.sharedAxisXIn(initialOffsetX = { -(it * OFFSET).toInt() }) togetherWith
            JetBaseMotion.sharedAxisXOut(targetOffsetX = { (it * OFFSET).toInt() })
    }

    /** Predictive back (Android 14+): the leaving screen shrinks while following the gesture. */
    fun <T : Any> predictivePop():
        AnimatedContentTransitionScope<Scene<T>>.(Int) -> ContentTransform = {
        JetBaseMotion.sharedAxisXIn(initialOffsetX = { -(it * OFFSET).toInt() }) togetherWith
            (JetBaseMotion.sharedAxisXOut(targetOffsetX = { (it * OFFSET).toInt() }) +
                scaleOut(
                    animationSpec = tween(easing = JetBaseMotion.EmphasizedAccelerate),
                    targetScale = 0.9f,
                ))
    }
}
