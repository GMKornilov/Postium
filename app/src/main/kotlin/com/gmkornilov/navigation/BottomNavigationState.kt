package com.gmkornilov.navigation

import com.alphicc.brick.TreeRouter

data class BottomNavigationState(
    val selectedIndex: Int,
    val selectedRouter: TreeRouter
)
