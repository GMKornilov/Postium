package com.gmkornilov.userpage.brick_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.brick_navigation.ScreenFactory
import com.gmkornilov.post.repository.PostRepository
import com.gmkornilov.user.repository.UserRepository
import com.gmkornilov.userpage.view.UserPage
import com.gmkornilov.userpage.view.UserPageListener
import com.gmkornilov.userpage.view.UserPageViewModel
import com.gmkornilov.view_model.BaseViewModel
import dagger.BindsInstance
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Scope

private const val USER_PAGE_SCREEN_KEY = "user_page"

class UserPageScreenFactory @Inject constructor(
    override val dependency: Deps
): DependencyProvider<UserPageScreenFactory.Deps> {
    private inner class Factory(
        val listener: UserPageListener,
        val userPageArgument: UserPageArgument,
    ): ScreenFactory() {
        override fun buildKey(): String {
            return "${USER_PAGE_SCREEN_KEY}_${userPageArgument.id}"
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer
        ): BaseViewModel<*, *> {
            val component = DaggerUserPageScreenFactory_Component.builder()
                .deps(dependency)
                .listener(listener)
                .argument(userPageArgument)
                .build()

            return component.viewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<UserPageViewModel>()

            UserPage(viewModel = viewModel, modifier = Modifier.fillMaxSize())
        }

    }

    fun build(listener: UserPageListener, userPageArgument: UserPageArgument, prevPath: String): Screen<*> {
        return Factory(listener, userPageArgument).build(prevPath)
    }

    interface Deps : Dependency {
        val authInteractor: AuthInteractor
        val postRepository: PostRepository
        val userRepository: UserRepository
    }

    @Scope
    annotation class UserPageScope

    @dagger.Component(
        dependencies = [Deps::class]
    )
    @UserPageScope
    internal interface Component {
        val viewModel: UserPageViewModel

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun listener(listener: UserPageListener): Builder

            @BindsInstance
            fun argument(userPageArgument: UserPageArgument): Builder

            fun deps(dependency: Deps): Builder

            fun build(): Component
        }
    }
}