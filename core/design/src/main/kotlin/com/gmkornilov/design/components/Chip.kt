package com.gmkornilov.design.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.theme.PostiumTheme
import compose.icons.TablerIcons
import compose.icons.tablericons.Checks

@Composable
fun Chip(
    text: String,
    modifier: Modifier = Modifier,
    onCLick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.primary)
            .clickable { onCLick() }
            .padding(horizontal = 12.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(top = 4.dp, bottom = 6.dp, end = 16.dp, start = 16.dp)
        )
    }
}

@Composable
fun SelectableChip(
    text: String,
    isSelected: Boolean,
    onSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val background by animateColorAsState(
        if (isSelected) MaterialTheme.colors.secondary else MaterialTheme.colors.primary
    )
    val textColor by animateColorAsState(
        if (isSelected) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onPrimary
    )


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .clickable { onSelected(!isSelected) }
            .padding(horizontal = 12.dp),
    ) {
        AnimatedVisibility(
            visible = isSelected,
            enter = expandHorizontally(),
            exit = shrinkHorizontally(),
        ) {
            Icon(
                TablerIcons.Checks,
                null,
                tint = textColor,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        val startPadding = if (isSelected) 8.dp else 16.dp

        Text(
            text = text,
            style = MaterialTheme.typography.h5,
            color = textColor,
            modifier = Modifier.padding(top = 4.dp, bottom = 6.dp, end = 16.dp, start = startPadding)
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun ChipPreview() {
    PostiumTheme {
        Row {
            SelectableChip("test", isSelected = true, {})

            Spacer(Modifier.size(4.dp))

            SelectableChip("test", isSelected = false, {})
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ChipPreviewDark() {
    PostiumTheme {
        Row {
            SelectableChip("test", isSelected = true, {})

            Spacer(Modifier.size(4.dp))

            SelectableChip("test", isSelected = false, {})
        }
    }
}