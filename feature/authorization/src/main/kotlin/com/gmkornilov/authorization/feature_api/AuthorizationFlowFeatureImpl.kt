package com.gmkornilov.authorization.feature_api

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.gmkornilov.authorization.home.Home
import com.gmkornilov.authorization.registration.Registration
import javax.inject.Inject

private const val ROUTE = "authorization"

private const val HOME_ROUTE = "authorization/home"
private const val REGISTRATION_ROUTE = "authorization/registration"

class AuthorizationFlowFeatureImpl @Inject constructor(): AuthorizationFlowFeature {
    override val route = ROUTE

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        modifier: Modifier
    ) {
        navGraphBuilder.navigation(
            route = ROUTE,
            startDestination = HOME_ROUTE
        ) {
            composable(HOME_ROUTE) {
                Home()
            }

            composable(REGISTRATION_ROUTE) {
                Registration()
            }
        }
    }
}