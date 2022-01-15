package com.gmkornilov.mainpage.feature_api

import androidx.compose.material.ScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gmkornilov.mainpage.mainpage.Mainpage
import javax.inject.Inject

private const val ROUTE = "mainpage"

internal class MainpageFeatureImpl @Inject constructor(): MainpageFeature {
    override val route = ROUTE

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        scaffoldState: ScaffoldState,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(route) {
            Mainpage()
        }
    }

    override fun containsRoute(route: String) = this.route == route
}