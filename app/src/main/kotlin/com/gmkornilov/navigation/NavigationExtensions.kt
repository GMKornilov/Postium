package com.gmkornilov.navigation

import androidx.compose.material.ScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.gmkornilov.feature_api.FeatureApi

fun NavGraphBuilder.register(
    featureApi: FeatureApi,
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier
) {
    featureApi.registerGraph(
        navGraphBuilder = this,
        navController = navController,
        scaffoldState = scaffoldState,
        modifier = modifier,
    )
}