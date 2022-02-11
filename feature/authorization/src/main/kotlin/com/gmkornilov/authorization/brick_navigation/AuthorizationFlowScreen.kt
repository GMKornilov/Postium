package com.gmkornilov.authorization.brick_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorization.di.AuthorizationDeps
import com.gmkornilov.authorization.di.DaggerAuthorizationComponent
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.authorization.home.Home
import com.gmkornilov.authorization.home.HomeViewModel
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

data class AuthorizationArgument(
    val authorizationDeps: AuthorizationDeps,
    val userResultHandler: UserResultHandler,
    val router: TreeRouter,
)

@OptIn(ExperimentalCoroutinesApi::class)
private val authorizationHomeScreen = BaseScreen(
    key = "Authorization Home",
    onCreate = { _, argument ->
        val arg = argument.get<AuthorizationArgument>()

        val component = DaggerAuthorizationComponent.builder()
            .authorizationDeps(arg.authorizationDeps)
            .userResultHandler(arg.userResultHandler)
            .build()

        return@BaseScreen component.homeViewModel
    },
    content = {
        val viewModel = it.get<HomeViewModel>()

        Home(
            viewModel = viewModel,
            modifier = Modifier.fillMaxSize()
        )
    }
)

class AuthorizationFlowScreen @Inject constructor(): NavigationScreenProvider<HomeViewModel> {
    override val screen = authorizationHomeScreen
}