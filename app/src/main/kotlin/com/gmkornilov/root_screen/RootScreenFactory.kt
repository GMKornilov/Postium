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
import com.gmkornilov.postpage.brick_navigation.PostPageScreenFactory
import com.gmkornilov.userpage.brick_navigation.UserPageScreenFactory
import com.google.firebase.firestore.FirebaseFirestore
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
        OuterDeps,
        Dependency {
        override val authInteractor: AuthInteractor

        val router: TreeRouter
    }

    interface OuterDeps {
        val firebaseFirestore: FirebaseFirestore
    }

    @dagger.Component(
        dependencies = [Deps::class],
        modules = [RootModule::class],
    )
    @RootScope
    interface Component :
        AuthorizationFlowScreenFactory.Deps,
        MainpageScreenFactory.Deps,
        PostPageScreenFactory.Deps,
        UserPageScreenFactory.Deps {
        val rootViewModel: RootViewModel

        override val authorizationFlowScreenFactory: AuthorizationFlowScreenFactory

        override val postPageScreenFactory: PostPageScreenFactory

        override val userPageScreenFactory: UserPageScreenFactory
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

        @Binds
        @RootScope
        fun bindPostpageDeps(component: Component): PostPageScreenFactory.Deps

        @Binds
        @RootScope
        fun bindUserpageDeps(component: Component): UserPageScreenFactory.Deps

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