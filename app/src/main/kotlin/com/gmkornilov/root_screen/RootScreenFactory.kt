package com.gmkornilov.root_screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorization.brick_navigation.AuthorizationFlowScreenFactory
import com.gmkornilov.bottom_navigation_items.BottomNavigationItem
import com.gmkornilov.bottom_navigation_items.HomeBottomNavigationItem
import com.gmkornilov.bottom_navigation_items.ProfileBottomNavigationItem
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import com.gmkornilov.mainpage.brick_navigation.MainpageScreenFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Scope

private const val key = "Root screen"

@OptIn(ExperimentalAnimationApi::class)
class RootScreenFactory @Inject constructor(
    override val dependency: RootScreenDeps,
) : NavigationScreenProvider<RootScreenFactory.RootScreenDeps> {

    val screenKey = key

    fun build(): Screen<RootViewModel> {
        return BaseScreen(
            key = key,
            onCreate = { _, _ ->
                val component = DaggerRootScreenFactory_Component.builder()
                    .rootScreenDeps(dependency)
                    .build()
                return@BaseScreen component.rootViewModel
            },
            content = {
                val viewModel = it.get<RootViewModel>()

                val state by viewModel.container.stateFlow.collectAsState()

                val router = state.selectedRouter
                val index = state.selectedIndex

                BottomMenuScreen(
                    index,
                    router,
                    viewModel::onMenuItemClicked,
                    viewModel.bottomNavigationItems,
                )
            }
        )
    }

    interface RootScreenDeps :
        AuthorizationFlowScreenFactory.AuthorizationDeps,
        MainpageScreenFactory.MainPageDependency,
        Dependency {
        override val router: TreeRouter

        val authInteractor: AuthInteractor
    }

    @RootScope
    @dagger.Component(
        dependencies = [RootScreenDeps::class],
        modules = [RootModule::class],
    )
    interface Component : AuthorizationFlowScreenFactory.AuthorizationDeps {
        val rootViewModel: RootViewModel
    }

    @Scope
    annotation class RootScope

    @Module
    interface RootModule {

        @Binds
        @RootScope
        fun bindAuthorizationDeps(component: Component): AuthorizationFlowScreenFactory.AuthorizationDeps

        companion object {
            @Provides
            @RootScope
            fun bottomNavigationItems(
                homeBottomNavigationItem: HomeBottomNavigationItem,
                profileBottomNavigationItem: ProfileBottomNavigationItem
            ): List<BottomNavigationItem> {
                return listOf(homeBottomNavigationItem, profileBottomNavigationItem)
            }
        }
    }
}