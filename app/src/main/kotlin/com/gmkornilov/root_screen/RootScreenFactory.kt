package com.gmkornilov.root_screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
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
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.commentpage.brick_navigation.PostCommentPageFactory
import com.gmkornilov.commentpage.view.CommentpageListener
import com.gmkornilov.mainpage.brick_navigation.MainpageScreenFactory
import com.gmkornilov.mainpage.mainpage.MainPageListener
import com.gmkornilov.postcreatepage.brick_navigation.PostCreatePageScreenFactory
import com.gmkornilov.postpage.brick_navigation.PostPageScreenFactory
import com.gmkornilov.postpage.view.PostpageListener
import com.gmkornilov.user.repository.UserAvatarRepository
import com.gmkornilov.userpage.brick_navigation.UserPageScreenFactory
import com.gmkornilov.userpage.view.UserPageListener
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Scope

private const val key = "root_screen"

@OptIn(ExperimentalAnimationApi::class)
class RootScreenFactory @Inject constructor(
    override val dependency: Deps,
) : DependencyProvider<RootScreenFactory.Deps> {

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

                LaunchedEffect(viewModel) {
                    viewModel.onMenuItemClicked(index)
                }

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
        UserPageScreenFactory.Deps,
        PostCreatePageScreenFactory.Deps,
        PostCommentPageFactory.Deps {
        val rootViewModel: RootViewModel

        val authorizationFlowScreenFactory: AuthorizationFlowScreenFactory

        val postPageScreenFactory: PostPageScreenFactory

        val userPageScreenFactory: UserPageScreenFactory

        override val userAvatarRepository: UserAvatarRepository

        val postCreatePageScreenFactory: PostCreatePageScreenFactory
    }

    @Scope
    annotation class RootScope

    @Module(includes = [DepsModule::class, ListenersModule::class])
    interface RootModule {
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

    @Module
    interface ListenersModule {
        @RootScope
        @Binds
        fun bindUserPageListener(rootViewModel: RootViewModel): UserPageListener

        @RootScope
        @Binds
        fun bindMainPageListener(rootViewModel: RootViewModel): MainPageListener

        @RootScope
        @Binds
        fun postpageListener(rootViewModel: RootViewModel): PostpageListener

        @RootScope
        @Binds
        fun commentpageListener(rootViewModel: RootViewModel): CommentpageListener
    }

    @Module
    interface DepsModule {
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

        @Binds
        @RootScope
        fun bindCreatePostDeps(component: Component): PostCreatePageScreenFactory.Deps

        @Binds
        @RootScope
        fun bindCommentDeps(component: Component): PostCommentPageFactory.Deps
    }
}