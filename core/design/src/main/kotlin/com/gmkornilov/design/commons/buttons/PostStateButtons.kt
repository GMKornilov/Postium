package com.gmkornilov.design.commons.buttons

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.theme.DarkBurgundy
import com.gmkornilov.design.theme.Green
import com.gmkornilov.design.theme.PostiumTheme

@Composable
fun LikeButton(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    IconToggleButton(
        checked = isChecked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
    ) {
        Icon(
            imageVector = if (isChecked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
            contentDescription = null,
            tint = if (isChecked) Green else MaterialTheme.colors.onSurface,
        )
    }
}

@Composable
fun DislikeButton(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    IconToggleButton(
        checked = isChecked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
    ) {
        Icon(
            imageVector = if (isChecked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
            contentDescription = null,
            tint = if (isChecked) Color.Red else MaterialTheme.colors.onSurface,
        )
    }
}

@Composable
fun BookmarkButton(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    IconToggleButton(
        checked = isChecked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
    ) {
        Icon(
            imageVector = if (isChecked) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkAdd,
            contentDescription = null,
            tint = if (isChecked) DarkBurgundy else MaterialTheme.colors.onSurface,
        )
    }
}

@Preview(
    name = "Buttons light",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun SuccessPreviewLight() {
    ButtonPreview()
}

@Preview(
    name = "Buttons dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun SuccessPreviewDark() {
    ButtonPreview()
}

@Composable
private fun ButtonPreview() {
    PostiumTheme {
        Column(modifier = Modifier.background(MaterialTheme.colors.surface)) {
            Row {
                val modifier = Modifier.padding(4.dp)
                LikeButton(isChecked = true, onCheckedChange = {}, modifier = modifier)
                DislikeButton(isChecked = true, onCheckedChange = {}, modifier = modifier)
                BookmarkButton(isChecked = true, onCheckedChange = {}, modifier = modifier)
            }
            Row {
                val modifier = Modifier.padding(4.dp)
                LikeButton(isChecked = false, onCheckedChange = {}, modifier = modifier)
                DislikeButton(isChecked = false, onCheckedChange = {}, modifier = modifier)
                BookmarkButton(isChecked = false, onCheckedChange = {}, modifier = modifier)
            }
        }
    }
}