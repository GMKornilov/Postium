package com.gmkornilov.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.*
import com.gmkornilov.design.R

@Composable
fun ErrorStateContainer(
    errorMessage: String,
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error_animation))

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(16.dp),
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = errorMessage,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.onSurface,
        )
    }
}

@Composable
fun EmptyStateContainer(
    emptyStateMessage: String,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.not_found))

    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value = MaterialTheme.colors.onSurface.toArgb(),
            keyPath = arrayOf("**"),
        )
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(16.dp),
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            dynamicProperties = dynamicProperties,
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = emptyStateMessage,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.onSurface,
        )
    }
}