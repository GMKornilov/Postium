package com.gmkornilov.navigation.bottom_navigation_items

import android.content.Context
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import com.gmkornilov.mainpage.feature_api.MainpageFeature
import com.gmkornilov.navigation.BottomNavigationItem
import com.gmkornilov.postium.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HomeBottomNavigationItem @Inject constructor(
    private val mainpageFeature: MainpageFeature,
    @ApplicationContext context: Context,
): BottomNavigationItem {
    @Composable
    override fun IconComposable() {
        Icon(imageVector = Icons.Filled.Home, contentDescription = "home")
    }

    override fun isInTab(route: String): Boolean {
        return mainpageFeature.containsRoute(route)
    }

    override val title: String = context.getString(R.string.home_tab)

    override val route: String = mainpageFeature.route
}