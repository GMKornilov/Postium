package com.gmkornilov.authorization.brick_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.email.EmailAuthInteractor
import com.gmkornilov.authorizarion.facebook.FacebookAuthInteractor
import com.gmkornilov.authorizarion.google.GoogleAuthInteractor
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.authorization.home.Home
import com.gmkornilov.authorization.home.HomeViewModel
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, InternalCoroutinesApi::class)
class AuthorizationFlowScreenFactory @Inject constructor(
    override val dependency: AuthorizationDeps,
): NavigationScreenProvider<AuthorizationFlowScreenFactory.AuthorizationDeps> {

    private val authorizationHomeScreen = BaseScreen(
        key = "Authorization Home",
        onCreate = { _, argument ->
            val arg = argument.get<UserResultHandler>()

            val component = DaggerAuthorizationFlowScreenFactory_Component.builder()
                .authorizationDeps(dependency)
                .userResultHandler(arg)
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

    fun build(): Screen<HomeViewModel> {
        return authorizationHomeScreen
    }

    interface AuthorizationDeps: Dependency {
        val googleAuthInteractor: GoogleAuthInteractor
        val facebookAuthInteractor: FacebookAuthInteractor
        val emailAuthInteractor: EmailAuthInteractor

        val router: TreeRouter
    }

    @dagger.Component(dependencies = [AuthorizationDeps::class, UserResultHandler::class])
    interface Component {
        val homeViewModel: HomeViewModel
    }
}