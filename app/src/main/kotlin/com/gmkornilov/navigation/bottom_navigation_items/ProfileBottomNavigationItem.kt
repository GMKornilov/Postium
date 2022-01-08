package com.gmkornilov.navigation.bottom_navigation_items

import android.content.Context
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorization.feature_api.AuthorizationFlowFeature
import com.gmkornilov.navigation.BottomNavigationItem
import com.gmkornilov.postium.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProfileBottomNavigationItem @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val authorizationFlowFeature: AuthorizationFlowFeature,
    @ApplicationContext context: Context,
): BottomNavigationItem {
    @Composable
    override fun IconComposable() {
        val user by authInteractor.authState.collectAsState()

        val icon = if (user != null) {
            Icons.Filled.Person
        } else {
            Icons.Outlined.AccountBox
        }

        Icon(imageVector = icon, contentDescription = "home")
    }

    override fun isInTab(route: String): Boolean {
        return authorizationFlowFeature.containsRoute(route)
    }

    override val title = context.getString(R.string.profile_tab)

    override val route: String = authorizationFlowFeature.route
}