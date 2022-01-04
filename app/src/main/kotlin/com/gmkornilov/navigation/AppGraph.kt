package com.gmkornilov.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.gmkornilov.authorization.feature_api.AuthorizationFlowFeature
import com.gmkornilov.feature_api.FeatureApi

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeFeature: AuthorizationFlowFeature,
    features: Set<FeatureApi>
) {
    NavHost(
        navController = navController,
        homeFeature.route
    ) {
        for (feature in features) {
            register(feature, navController, modifier)
        }
    }
}