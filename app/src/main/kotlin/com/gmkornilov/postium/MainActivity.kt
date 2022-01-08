package com.gmkornilov.postium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gmkornilov.activity_utils.ActivityHelper
import com.gmkornilov.authorization.feature_api.AuthorizationFlowFeature
import com.gmkornilov.design.theme.PostiumTheme
import com.gmkornilov.feature_api.FeatureApi
import com.gmkornilov.mainpage.feature_api.MainpageFeature
import com.gmkornilov.navigation.AppNavGraph
import com.gmkornilov.navigation.BottomNavigationItem
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var features: Set<@JvmSuppressWildcards FeatureApi>

    @Inject
    lateinit var homeFeature: AuthorizationFlowFeature

    @Inject
    lateinit var mainpageFeature: MainpageFeature

    @Inject
    lateinit var activityHelper: ActivityHelper

    @Inject
    lateinit var bottomNavigationItems: List<@JvmSuppressWildcards BottomNavigationItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppContent()
        }
    }

    @Composable
    private fun AppContent() {
        ProvideWindowInsets {
            PostiumTheme {
                val navController = rememberNavController()

                val scaffoldState = rememberScaffoldState()

                Scaffold(
                    backgroundColor = MaterialTheme.colors.background,
                    bottomBar = { BottomBar(navController, bottomNavigationItems) },
                    scaffoldState = scaffoldState
                ) {
                    AppNavGraph(
                        navController,
                        mainpageFeature,
                        features,
                        scaffoldState,
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                    )
                }
            }
        }
    }

    @Composable
    private fun BottomBar(
        navController: NavController,
        bottomNavigationItems: List<BottomNavigationItem>
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: mainpageFeature.route

        val bottomNavigationVisible = bottomNavigationItems.any { it.isInTab(currentRoute) }

        if (bottomNavigationVisible) {
            BottomNavigation(
                Modifier.navigationBarsHeight(additional = 56.dp)
            ) {
                bottomNavigationItems.forEach { bottomNavigationItem ->
                    BottomNavigationItem(
                        icon = { bottomNavigationItem.IconComposable() },
                        selected = bottomNavigationItem.isInTab(currentRoute),
                        onClick = {
                            if (bottomNavigationItem.route != currentRoute) {
                                navController.navigate(bottomNavigationItem.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        modifier = Modifier.navigationBarsPadding()
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        activityHelper.setActivity(this)
    }

    override fun onStop() {
        super.onStop()

        activityHelper.resetActivity()
    }
}