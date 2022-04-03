package com.gmkornilov.root_screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorization.feature_flow.AuthorizationFlowScreenFactory
import com.gmkornilov.bottom_navigation_items.*
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.commentpage.brick_navigation.PostCommentPageFactory
import com.gmkornilov.commentpage.view.CommentpageListener
import com.gmkornilov.mainpage.brick_navigation.MainpageScreenFactory
import com.gmkornilov.mainpage.mainpage.MainPageListener
import com.gmkornilov.post_categories.categories_list.CategoriesListScreenFactory
import com.gmkornilov.post_categories.categories_list.view.CategoriesListener
import com.gmkornilov.post_categories.categories_posts.CategoriesPostsScreenFactory
import com.gmkornilov.post_categories.categories_posts.view.CategoryPostsListener
import com.gmkornilov.postcreatepage.brick_navigation.PostCreatePageScreenFactory
import com.gmkornilov.postpage.brick_navigation.PostPageScreenFactory
import com.gmkornilov.postpage.view.PostpageListener
import com.gmkornilov.user.repository.UserAvatarRepository
import com.gmkornilov.user_playlists.playlist_create.PlaylistCreateScreenFactory
import com.gmkornilov.user_playlists.playlist_list.PlaylistListScreenFactory
import com.gmkornilov.user_playlists.playlist_list.view.PlaylistListListener
import com.gmkornilov.userpage.brick_navigation.UserPageScreenFactory
import com.gmkornilov.userpage.view.UserPageListener
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Scope

const val ROOT_KEY = "root_screen"

@OptIn(ExperimentalAnimationApi::class)
class RootScreenFactory @Inject constructor(
    override val dependency: Deps,
) : DependencyProvider<RootScreenFactory.Deps> {
    fun build(): Screen<*> {
        return BaseScreen(
            key = ROOT_KEY,
            onCreate = { _, _ ->
                val component = DaggerRootScreenFactory_Component.builder()
                    .deps(dependency)
                    .build()
                Timber.e("new on create in root")
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
        PostCommentPageFactory.Deps,
        CategoriesListScreenFactory.Deps,
        CategoriesPostsScreenFactory.Deps,
        PlaylistListScreenFactory.Deps,
        PlaylistCreateScreenFactory.Deps {
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
                categoriesBottomNavigationItem: CategoriesBottomNavigationItem,
                profileBottomNavigationItem: ProfileBottomNavigationItem,
                playlistsBottomNavigationItem: PlaylistsBottomNavigationItem,
            ): List<BottomNavigationItem> {
                return listOf(
                    homeBottomNavigationItem,
                    categoriesBottomNavigationItem,
                    playlistsBottomNavigationItem,
                    profileBottomNavigationItem
                )
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

        @RootScope
        @Binds
        fun categoryListener(rootViewModel: RootViewModel): CategoriesListener

        @RootScope
        @Binds
        fun categoryPostsListener(rootViewModel: RootViewModel): CategoryPostsListener

        @RootScope
        @Binds
        fun playlistListPostsListener(rootViewModel: RootViewModel): PlaylistListListener
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

        @Binds
        @RootScope
        fun bindCategories(component: Component): CategoriesListScreenFactory.Deps

        @Binds
        @RootScope
        fun bindCategoryPosts(component: Component): CategoriesPostsScreenFactory.Deps

        @Binds
        @RootScope
        fun bindPlaylistList(component: Component): PlaylistListScreenFactory.Deps

        @Binds
        @RootScope
        fun bindPlaylistCreate(component: Component): PlaylistCreateScreenFactory.Deps
    }
}