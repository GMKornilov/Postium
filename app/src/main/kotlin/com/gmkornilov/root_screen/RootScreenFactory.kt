package com.gmkornilov.root_screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorization.feature_flow.AuthorizationFlowScreenFactory
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
    override val dependency: Deps,
) : NavigationScreenProvider<RootScreenFactory.Deps> {

    val screenKey = key

    fun build(): Screen<RootViewModel> {
        return BaseScreen(
            key = key,
            onCreate = { _, _ ->
                val component = DaggerRootScreenFactory_Component.builder()
                    .deps(dependency)
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

    interface Deps :
        AuthorizationFlowScreenFactory.Deps,
        MainpageScreenFactory.Deps,
        Dependency {
        val authInteractor: AuthInteractor

        val router: TreeRouter
    }

    @RootScope
    @dagger.Component(
        dependencies = [Deps::class],
        modules = [RootModule::class],
    )
    interface Component : AuthorizationFlowScreenFactory.Deps, MainpageScreenFactory.Deps {
        val rootViewModel: RootViewModel
    }

    @Scope
    annotation class RootScope

    @Module
    interface RootModule {

        @Binds
        @RootScope
        fun bindAuthorizationDeps(component: Component): AuthorizationFlowScreenFactory.Deps

        @Binds
        @RootScope
        fun bindMainpageDeps(component: Component): MainpageScreenFactory.Deps

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