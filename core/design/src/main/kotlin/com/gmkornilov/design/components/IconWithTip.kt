package com.gmkornilov.design.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.theme.PostiumTheme

@Composable
fun IconWithTip(
    imageVector: ImageVector,
    text: String,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
    Box {
        Icon(imageVector = imageVector, contentDescription = contentDescription, tint = tint)

        Text(
            text,
            style = MaterialTheme.typography.caption,
            color = tint,
            modifier = Modifier.align(Alignment.BottomEnd).padding(start = 24.dp, top = 12.dp)
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun IconWithTipPreview() {
    PostiumTheme {
        Row(Modifier.background(MaterialTheme.colors.surface)) {
            IconWithTip(imageVector = Icons.Filled.ThumbUp, text = "1")

            Spacer(Modifier.size(4.dp))

            IconWithTip(imageVector = Icons.Filled.ThumbDown, text = "1")
        }
    }
}