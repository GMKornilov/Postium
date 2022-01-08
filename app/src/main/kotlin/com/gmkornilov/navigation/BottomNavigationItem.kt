package com.gmkornilov.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

interface BottomNavigationItem {
    @Composable
    fun IconComposable()

    fun isInTab(route: String): Boolean

    val title: String

    val route: String
}