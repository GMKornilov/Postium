package com.gmkornilov.authorization.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.email.EmailAuthInteractor
import com.gmkornilov.authorizarion.facebook.FacebookAuthInteractor
import com.gmkornilov.authorizarion.google.GoogleAuthInteractor
import com.gmkornilov.authorizarion.domain.UserResultHandler
import com.gmkornilov.authorization.home.domain.HomeFlowEvents
import com.gmkornilov.authorization.home.view.Home
import com.gmkornilov.authorization.home.view.HomeViewModel
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.brick_navigation.ScreenFactory
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Scope

private const val HOME_KEY = "authorization_home"

@OptIn(ExperimentalCoroutinesApi::class, InternalCoroutinesApi::class)
internal class HomeScreenFactory @Inject constructor(
    override val dependency: Deps,
): DependencyProvider<HomeScreenFactory.Deps> {
    private inner class Factory(
        private val userResultHandler: UserResultHandler,
    ): ScreenFactory() {
        override fun buildKey(): String {
            return HOME_KEY
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer
        ): BaseViewModel<*, *> {
            val component = DaggerHomeScreenFactory_Component.builder()
                .deps(dependency)
                .userResultHandler(userResultHandler)
                .build()

            return component.homeViewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<HomeViewModel>()

            Home(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    fun build(userResultHandler: UserResultHandler): Screen<*> {
        return Factory(userResultHandler).build("")
    }

    interface Deps: Dependency {
        val googleAuthInteractor: GoogleAuthInteractor
        val facebookAuthInteractor: FacebookAuthInteractor
        val emailAuthInteractor: EmailAuthInteractor

        val homeFlowEvents: HomeFlowEvents
    }

    @Scope
    annotation class HomeScope

    @dagger.Component(dependencies = [Deps::class, UserResultHandler::class])
    @HomeScope
    interface Component {
        val homeViewModel: HomeViewModel
    }
}