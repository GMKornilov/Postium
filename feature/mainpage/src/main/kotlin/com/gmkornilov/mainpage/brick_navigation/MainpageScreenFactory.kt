package com.gmkornilov.mainpage.brick_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
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
    private val mainpageScreen = BaseScreen(
        key = "Home",
        onCreate = { _, _ ->
           val component = DaggerMainpageScreenFactory_Component.builder()
               .deps(dependency)
               .build()
           component.mainPageViewModel
        },
        content = {
            val viewModel = it.get<MainPageViewModel>()
            Mainpage(viewModel, Modifier.fillMaxSize())
        }
    )

    interface Deps : Dependency {

    }

    @dagger.Component(
        dependencies = [Deps::class],
        modules = [Module::class]
    )
    internal interface Component {
        val mainPageViewModel: MainPageViewModel
    }

    @dagger.Module
    internal interface Module {

    }

    fun build(): Screen<*> {
        return mainpageScreen
    }
}