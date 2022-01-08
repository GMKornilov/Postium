package com.gmkornilov.feature_api

import androidx.compose.material.ScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface FeatureApi {
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        scaffoldState: ScaffoldState,
        modifier: Modifier = Modifier
    )

    fun containsRoute(route: String): Boolean
}