package com.gmkornilov.design.modifiers

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.google.accompanist.insets.LocalWindowInsets

val LocalNavigationBarHeight = compositionLocalOf { 56.dp }

fun Modifier.imePaddingWithBottomBar(): Modifier = composed {
    val inset = LocalWindowInsets.current.ime
    val bottomPadding = with(LocalDensity.current) {
        max(inset.bottom.toDp() - LocalNavigationBarHeight.current, 0.dp)
    }
    this.padding(bottom = bottomPadding)
}