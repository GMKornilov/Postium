package com.gmkornilov.mainpage.brick_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorization.feature_flow.AuthorizationFlowScreenFactory
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import com.gmkornilov.mainpage.mainpage.MainPageViewModel
import com.gmkornilov.mainpage.mainpage.Mainpage
import com.gmkornilov.post_bookmarks.PostBookmarkRepository
import com.gmkornilov.post_likes.PostLikeRepository
import com.gmkornilov.postpage.brick_navigation.PostPageScreenFactory
import com.gmkornilov.source.FirebasePostSource
import com.gmkornilov.userpage.brick_navigation.UserPageScreenFactory
import dagger.BindsInstance
import javax.inject.Inject
import javax.inject.Scope

class MainpageScreenFactory @Inject constructor(
    override val dependency: Deps,
) : NavigationScreenProvider<MainpageScreenFactory.Deps> {
    private lateinit var router: TreeRouter

    private val mainpageScreen = BaseScreen(
        key = "Home",
        onCreate = { _, _ ->
            val component = DaggerMainpageScreenFactory_Component.builder()
                .deps(dependency)
                .router(router)
                .build()
            component.mainPageViewModel
        },
        content = {
            val viewModel = it.get<MainPageViewModel>()
            Mainpage(viewModel, Modifier.fillMaxSize())
        }
    )

    interface Deps : Dependency {
        val firebasePostSource: FirebasePostSource

        val postLikeRepository: PostLikeRepository

        val postBookmarkRepository: PostBookmarkRepository

        val authInteractor: AuthInteractor

        val authorizationFlowScreenFactory: AuthorizationFlowScreenFactory

        val postPageScreenFactory: PostPageScreenFactory

        val userPageScreenFactory: UserPageScreenFactory
    }

    @Scope
    annotation class MainpageScope

    @dagger.Component(
        dependencies = [Deps::class],
        modules = [Module::class]
    )
    @MainpageScope
    internal interface Component {
        val mainPageViewModel: MainPageViewModel

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun router(router: TreeRouter): Builder

            fun deps(deps: Deps): Builder

            fun build(): Component
        }
    }

    @dagger.Module
    internal interface Module {

    }

    fun build(router: TreeRouter): Screen<*> {
        this.router = router
        return mainpageScreen
    }
}