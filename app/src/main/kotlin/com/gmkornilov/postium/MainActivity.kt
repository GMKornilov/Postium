package com.gmkornilov.postium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.gmkornilov.activity_utils.ActivityHelper
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorization.feature_api.AuthorizationFlowFeature
import com.gmkornilov.design.theme.PostiumTheme
import com.gmkornilov.feature_api.FeatureApi
import com.gmkornilov.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var features: Set<@JvmSuppressWildcards FeatureApi>

    @Inject
    lateinit var homeFeature: AuthorizationFlowFeature

    @Inject
    lateinit var activityHelper: ActivityHelper

    @Inject
    lateinit var authInteractor: AuthInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PostiumTheme {
                val navController = rememberNavController()

                AppNavGraph(
                    navController = navController,
                    homeFeature = homeFeature,
                    features = features,
                )
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

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PostiumTheme {
        Greeting("Android")
    }
}