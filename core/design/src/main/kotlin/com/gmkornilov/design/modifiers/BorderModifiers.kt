package com.gmkornilov.design.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color, cornerRadius: Dp): Modifier {
    return this.drawWithContent {
        drawContent()

        val corner = cornerRadius.value * density

        val width = strokeWidth.value * density
        val y = size.height - width / 2

        drawLine(
            color,
            Offset(corner, y),
            Offset(size.width - corner, y),
            width
        )

        val bottomLeftRect = Rect(
            Offset(corner, y - corner),
            corner
        )
        val bottomRightRect = Rect(
            Offset(size.width - corner, y - corner),
            corner
        )

        val bottomLeftPath = Path().apply {
            addArc(
                bottomLeftRect,
                90f,
                90f,
            )
        }
        val bottomRightPath = Path().apply {
            addArc(
                bottomRightRect,
                0f,
                90f,
            )
        }
        drawPath(bottomLeftPath, color, style = Stroke(width = width))
        drawPath(bottomRightPath, color, style = Stroke(width = width))
    }
}

fun Modifier.topBorder(strokeWidth: Dp, color: Color, cornerRadius: Dp): Modifier {
    return this.drawWithContent {
        drawContent()

        val corner = cornerRadius.value * density

        val width = strokeWidth.value * density
        val y = width / 2

        drawLine(
            color,
            Offset(corner, y),
            Offset(size.width - corner, y),
            width
        )

        val topLeftRect = Rect(
            Offset(corner, y + corner),
            corner
        )
        val topRightRect = Rect(
            Offset(size.width - corner, y + corner),
            corner
        )

        val bottomLeftPath = Path().apply {
            addArc(
                topLeftRect,
                -90f,
                -90f,
            )
        }
        val bottomRightPath = Path().apply {
            addArc(
                topRightRect,
                -90f,
                90f,
            )
        }
        drawPath(bottomLeftPath, color, style = Stroke(width = width))
        drawPath(bottomRightPath, color, style = Stroke(width = width))
    }
}