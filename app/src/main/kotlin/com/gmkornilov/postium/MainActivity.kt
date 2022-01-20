package com.gmkornilov.postium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.alphicc.brick.TreeRouter
import com.alphicc.brick.navigationContainers.ScreensContainer
import com.gmkornilov.activity_utils.ActivityHelper
import com.gmkornilov.design.theme.PostiumTheme
import com.gmkornilov.navigation.BottomNavigationScreen
import com.gmkornilov.navigation.BottomNavigationScreenDeps
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var activityHelper: ActivityHelper

    @Inject
    lateinit var router: TreeRouter

    @Inject
    lateinit var bottomNavigationScreenDeps: BottomNavigationScreenDeps

    @Inject
    lateinit var bottomNavigationScreen: BottomNavigationScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
            }

            AppContent()
        }

        if (savedInstanceState == null) {
            router.addScreen(bottomNavigationScreen.screen, bottomNavigationScreenDeps)
        }
    }

    @Composable
    private fun AppContent() {
        ProvideWindowInsets {
            PostiumTheme {
                ScreensContainer(router)
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