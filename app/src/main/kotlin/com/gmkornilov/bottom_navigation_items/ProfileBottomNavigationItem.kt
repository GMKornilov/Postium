package com.gmkornilov.bottom_navigation_items

import android.content.Context
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorization.brick_navigation.AuthorizationFlowScreen
import com.gmkornilov.navigation.BottomNavigationScreen
import com.gmkornilov.postium.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProfileBottomNavigationItem @Inject constructor(
    private val authInteractor: AuthInteractor,
    @ApplicationContext context: Context,
    parentRouter: TreeRouter,
    bottomNavigationScreen: BottomNavigationScreen,
    private val authorizationFlowScreen: AuthorizationFlowScreen,
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

    override val title = context.getString(R.string.profile_tab)

    override fun onSelected() {
        super.onSelected()

        if (router.isEmpty() && authInteractor.authState.value == null) {
            // TODO: add authorization deps
            router.addScreen(authorizationFlowScreen.screen)
        }
    }

    override val router: TreeRouter = parentRouter.branch(bottomNavigationScreen.screen.key)

    private fun TreeRouter.isEmpty(): Boolean {
        return this.screen.value == null
    }
}