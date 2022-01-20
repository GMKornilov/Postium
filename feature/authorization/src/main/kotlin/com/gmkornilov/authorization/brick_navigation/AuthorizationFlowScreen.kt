package com.gmkornilov.authorization.brick_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.gmkornilov.authorization.di.AuthorizationDeps
import com.gmkornilov.authorization.di.DaggerAuthorizationComponent
import com.gmkornilov.authorization.home.Home
import com.gmkornilov.authorization.home.HomeViewModel
import com.gmkornilov.brick_navigation.BaseScreen
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
private val authorizationHomeScreen = BaseScreen(
    key = "Authorization Home",
    onCreate = { _, argument ->
        val deps = argument.get<AuthorizationDeps>()

        val component = DaggerAuthorizationComponent.builder()
            .authorizationDeps(deps)
            .build()

        return@BaseScreen component.homeViewModel
    },
    content = {
        val viewModel = it.get<HomeViewModel>()

        Home(
            scaffoldState = rememberScaffoldState(),
            navController = rememberNavController(),
            viewModel = viewModel,
            modifier = Modifier.fillMaxSize()
        )
    }
)

class AuthorizationFlowScreen @Inject constructor(): NavigationScreenProvider<HomeViewModel> {
    override val screen = authorizationHomeScreen
}