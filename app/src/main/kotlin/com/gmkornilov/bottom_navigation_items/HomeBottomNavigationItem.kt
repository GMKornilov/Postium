package com.gmkornilov.bottom_navigation_items

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alphicc.brick.TreeRouter
import com.gmkornilov.mainpage.brick_navigation.MainpageScreenFactory
import com.gmkornilov.postium.R
import com.gmkornilov.root_screen.RootScreenFactory
import javax.inject.Inject

class HomeBottomNavigationItem @Inject constructor(
    rootRouter: TreeRouter,
    bottomNavigationScreenFactory: RootScreenFactory,
    mainpageScreenFactory: MainpageScreenFactory,
): BottomNavigationItem {
    @Composable
    override fun IconComposable() {
        Icon(imageVector = Icons.Filled.Home, contentDescription = "home")
    }

    @Composable
    override fun TitleComposable() {
        Text(stringResource(R.string.home_tab))
    }

    override val router: TreeRouter = rootRouter.branch(bottomNavigationScreenFactory.screenKey).apply {
        newRootScreen(mainpageScreenFactory.build())
    }
}