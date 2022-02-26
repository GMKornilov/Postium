package com.gmkornilov.mainpage.brick_navigation

import com.alphicc.brick.Screen
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import com.gmkornilov.mainpage.mainpage.MainPageViewModel
import com.gmkornilov.mainpage.mainpage.Mainpage
import javax.inject.Inject

class MainpageScreenFactory @Inject constructor(
    override val dependency: Deps,
): NavigationScreenProvider<MainpageScreenFactory.Deps> {
    private val mainpageScreen = BaseScreen<MainPageViewModel>(
        key = "Home",
        content = {
            Mainpage()
        }
    )

    interface Deps : Dependency {

    }

    fun build(): Screen<MainPageViewModel> {
        return mainpageScreen
    }
}