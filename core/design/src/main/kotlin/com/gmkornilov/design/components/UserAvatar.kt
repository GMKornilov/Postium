package com.gmkornilov.design.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.google.accompanist.placeholder.material.placeholder

@Composable
fun UserAvatar(
    avatarUrl: String,
    modifier: Modifier = Modifier,
) {
    SubcomposeAsyncImage(
        model = avatarUrl,
        loading = {
            Box(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .width(IntrinsicSize.Max)
                    .placeholder(visible = true)
            )
        },
        contentDescription = null,
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
    )
}