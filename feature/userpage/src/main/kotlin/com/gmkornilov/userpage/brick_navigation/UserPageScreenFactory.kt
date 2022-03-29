package com.gmkornilov.userpage.brick_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alphicc.brick.Screen
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import com.gmkornilov.userpage.view.UserPage
import com.gmkornilov.userpage.view.UserPageViewModel
import dagger.Binds
import dagger.BindsInstance
import javax.inject.Inject

private const val USER_PAGE_SCREEN_KEY = "user page"

class UserPageScreenFactory @Inject constructor(
    override val dependency: Deps
): NavigationScreenProvider<UserPageScreenFactory.Deps> {
    private val screen = BaseScreen(
        key = USER_PAGE_SCREEN_KEY,
        onCreate = { _, arg ->
            val userPageArgument = arg.get<UserPageArgument>()
            val component = DaggerUserPageScreenFactory_Component.builder()
                .deps(dependency)
                .argument(userPageArgument)
                .build()

            component.viewModel
        }
    ) {
        val viewModel = it.get<UserPageViewModel>()

        UserPage(viewModel = viewModel, modifier = Modifier.fillMaxSize())
    }

    fun build(): Screen<*> {
        return screen
    }

    interface Deps : Dependency {

    }

    @dagger.Component(
        dependencies = [Deps::class]
    )
    internal interface Component {
        val viewModel: UserPageViewModel

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun argument(userPageArgument: UserPageArgument): Builder

            fun deps(dependency: Deps): Builder

            fun build(): Component
        }
    }
}