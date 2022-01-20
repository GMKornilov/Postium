package com.gmkornilov.postium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.alphicc.brick.TreeRouter
import com.alphicc.brick.navigationContainers.ScreensContainer
import com.gmkornilov.activity_utils.ActivityHelper
import com.gmkornilov.design.theme.PostiumTheme
import com.gmkornilov.bottom_navigation_items.BottomNavigationItem
import com.gmkornilov.navigation.BottomNavigationScreen
import com.gmkornilov.navigation.BottomNavigationScreenDeps
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var activityHelper: ActivityHelper

    @Inject
    lateinit var bottomNavigationItems: List<@JvmSuppressWildcards BottomNavigationItem>

    @Inject
    lateinit var router: TreeRouter

    @Inject
    lateinit var bottomNavigationScreen: BottomNavigationScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppContent()
        }

        if (savedInstanceState == null) {
            router.addScreen(bottomNavigationScreen.screen, object : BottomNavigationScreenDeps {
                override val router = this@MainActivity.router
                override val bottomNavigationItems = this@MainActivity.bottomNavigationItems
            })
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