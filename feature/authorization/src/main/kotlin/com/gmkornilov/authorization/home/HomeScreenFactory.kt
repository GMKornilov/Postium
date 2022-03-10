package com.gmkornilov.authorization.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.email.EmailAuthInteractor
import com.gmkornilov.authorizarion.facebook.FacebookAuthInteractor
import com.gmkornilov.authorizarion.google.GoogleAuthInteractor
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.authorization.home.domain.HomeFlowEvents
import com.gmkornilov.authorization.home.view.Home
import com.gmkornilov.authorization.home.view.HomeViewModel
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

private const val HOME_KEY = "Authorization Home"

@OptIn(ExperimentalCoroutinesApi::class, InternalCoroutinesApi::class)
internal class HomeScreenFactory @Inject constructor(
    override val dependency: Deps,
): NavigationScreenProvider<HomeScreenFactory.Deps> {
    val screenKey = HOME_KEY

    private val authorizationHomeScreen = BaseScreen(
        key = HOME_KEY,
        onCreate = { _, argument ->
            val arg = argument.get<UserResultHandler>()

            val component = DaggerHomeScreenFactory_Component.builder()
                .deps(dependency)
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

    interface Deps: Dependency {
        val googleAuthInteractor: GoogleAuthInteractor
        val facebookAuthInteractor: FacebookAuthInteractor
        val emailAuthInteractor: EmailAuthInteractor

        val homeFlowEvents: HomeFlowEvents
    }

    @dagger.Component(dependencies = [Deps::class, UserResultHandler::class])
    interface Component {
        val homeViewModel: HomeViewModel
    }
}