package com.gmkornilov.mainpage.brick_navigation

import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import com.gmkornilov.mainpage.mainpage.MainPageViewModel
import com.gmkornilov.mainpage.mainpage.Mainpage
import javax.inject.Inject

private val mainpageScreen = BaseScreen<MainPageViewModel>(
    key = "Home",
    content = {
        Mainpage()
    }
)

class MainpageScreen @Inject constructor(): NavigationScreenProvider<MainPageViewModel> {
    override val screen = mainpageScreen
}