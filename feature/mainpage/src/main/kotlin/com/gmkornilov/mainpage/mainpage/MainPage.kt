package com.gmkornilov.mainpage.mainpage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Mainpage() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("this is main page", modifier = Modifier.align(Alignment.Center))
    }
}