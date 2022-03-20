package com.gmkornilov.design.data

import androidx.compose.ui.unit.Dp

enum class CornerType(val topStart: Int = 0, val topEnd: Int = 0, val bottomStart: Int = 0, val bottomEnd: Int = 0) {
    NONE,
    ALL(topStart = 1, topEnd = 1, bottomStart = 1, bottomEnd = 1),
    TOP(topStart = 1, topEnd = 1),
    BOTTOM(bottomStart = 1, bottomEnd = 1);

    fun toTopStart(dp: Dp) = dp * topStart

    fun toTopEnd(dp: Dp) = dp * topEnd

    fun toBottomStart(dp: Dp) = dp * bottomStart

    fun toBottomEnd(dp: Dp) = dp * bottomEnd
}