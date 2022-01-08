package com.gmkornilov.most_popular.feature

import androidx.compose.material.ScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gmkornilov.most_popular.ui.MostPopularList

private const val ROUTE = "most_popular"

internal class MostPopularFeatureImpl constructor(): MostPopularFeature {
    override val route = ROUTE

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        scaffoldState: ScaffoldState,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(route) {
            MostPopularList()
        }
    }

    override fun containsRoute(route: String): Boolean {
        return false
    }
}