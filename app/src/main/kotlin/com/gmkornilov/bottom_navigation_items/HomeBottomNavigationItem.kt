package com.gmkornilov.bottom_navigation_items

import android.content.Context
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import com.alphicc.brick.TreeRouter
import com.gmkornilov.mainpage.brick_navigation.MainpageScreen
import com.gmkornilov.navigation.BottomNavigationScreen
import com.gmkornilov.postium.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HomeBottomNavigationItem @Inject constructor(
    parentRouter: TreeRouter,
    @ApplicationContext context: Context,
    bottomNavigationScreen: BottomNavigationScreen,
    mainpageScreen: MainpageScreen,
): BottomNavigationItem {
    @Composable
    override fun IconComposable() {
        Icon(imageVector = Icons.Filled.Home, contentDescription = "home")
    }

    override val title: String = context.getString(R.string.home_tab)

    override val router: TreeRouter = parentRouter.branch(bottomNavigationScreen.screen.key).apply {
        addScreen(mainpageScreen.screen)
    }
}