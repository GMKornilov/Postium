package com.gmkornilov.navigation

import androidx.compose.runtime.Composable

interface BottomNavigationItem {
    @Composable
    fun IconComposable()

    val title: String

    val route: String
}