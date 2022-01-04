package com.gmkornilov.navigation.bottom_navigation_items

import androidx.compose.runtime.Composable
import com.gmkornilov.navigation.BottomNavigationItem
import javax.inject.Inject

class HomeBottomNavigationItem @Inject constructor(

): BottomNavigationItem {
    @Composable
    override fun IconComposable() {

    }

    override val title: String = "test"

    override val route: String = "test"
}