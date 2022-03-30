package com.gmkornilov.userpage.brick_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorization.feature_flow.AuthorizationFlowScreenFactory
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import com.gmkornilov.post.repository.PostRepository
import com.gmkornilov.postcreatepage.brick_navigation.PostCreatePageScreenFactory
import com.gmkornilov.user.repository.UserRepository
import com.gmkornilov.userpage.view.UserPage
import com.gmkornilov.userpage.view.UserPageViewModel
import dagger.Binds
import dagger.BindsInstance
import javax.inject.Inject

private const val USER_PAGE_SCREEN_KEY = "user page"

class UserPageScreenFactory @Inject constructor(
    override val dependency: Deps
): NavigationScreenProvider<UserPageScreenFactory.Deps> {
    private lateinit var router: TreeRouter

    private val screen = BaseScreen(
        key = USER_PAGE_SCREEN_KEY,
        onCreate = { _, arg ->
            val userPageArgument = arg.get<UserPageArgument>()
            val component = DaggerUserPageScreenFactory_Component.builder()
                .deps(dependency)
                .router(router)
                .argument(userPageArgument)
                .build()

            component.viewModel
        }
    ) {
        val viewModel = it.get<UserPageViewModel>()

        UserPage(viewModel = viewModel, modifier = Modifier.fillMaxSize())
    }

    fun build(router: TreeRouter): Screen<*> {
        this.router = router
        return screen
    }

    interface Deps : Dependency {
        val authInteractor: AuthInteractor
        val authorizationFlowScreenFactory: AuthorizationFlowScreenFactory
        val postCreatePageScreenFactory: PostCreatePageScreenFactory
        val postRepository: PostRepository
        val userRepository: UserRepository
    }

    @dagger.Component(
        dependencies = [Deps::class]
    )
    internal interface Component {
        val viewModel: UserPageViewModel

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun router(router: TreeRouter): Builder

            @BindsInstance
            fun argument(userPageArgument: UserPageArgument): Builder

            fun deps(dependency: Deps): Builder

            fun build(): Component
        }
    }
}