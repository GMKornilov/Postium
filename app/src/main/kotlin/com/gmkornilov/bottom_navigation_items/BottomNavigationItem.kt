package com.gmkornilov.bottom_navigation_items

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.alphicc.brick.TreeRouter

interface BottomNavigationItem {
    @Composable
    fun IconComposable()

    @Composable
    fun TitleComposable()

    val router: TreeRouter

    fun onSelected() {}
}