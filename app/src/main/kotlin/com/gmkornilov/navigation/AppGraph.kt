package com.gmkornilov.navigation

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.gmkornilov.authorization.feature_api.AuthorizationFlowFeature
import com.gmkornilov.feature_api.FeatureApi
import com.gmkornilov.mainpage.feature_api.MainpageFeature

@Composable
fun AppNavGraph(
    navController: NavHostController,
    homeFeature: MainpageFeature,
    features: Set<FeatureApi>,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        homeFeature.route
    ) {
        for (feature in features) {
            register(feature, navController, scaffoldState, modifier)
        }
    }
}