package com.gmkornilov.postium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.alphicc.brick.TreeRouter
import com.alphicc.brick.navigationContainers.ScreensContainer
import com.gmkornilov.activity_utils.ActivityHelper
import com.gmkornilov.design.theme.LocalSystemUiController
import com.gmkornilov.design.theme.PostiumTheme
import com.gmkornilov.root_screen.RootScreenFactory
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import javax.inject.Inject


class MainActivity : ComponentActivity() {
    @Inject
    lateinit var activityHelper: ActivityHelper

    @Inject
    lateinit var router: TreeRouter

    @Inject
    lateinit var bottomNavigationScreenFactory: RootScreenFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MainApplication).component.inject(this)

        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()

            CompositionLocalProvider(
                LocalSystemUiController provides systemUiController
            ) {
                AppContent()
            }
        }

        if (savedInstanceState == null) {
            router.newRootScreen(bottomNavigationScreenFactory.build())
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