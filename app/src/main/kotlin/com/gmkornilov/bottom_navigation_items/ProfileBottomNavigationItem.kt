package com.gmkornilov.bottom_navigation_items

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.model.PostiumUser
import com.gmkornilov.authorization.brick_navigation.AuthorizationArgument
import com.gmkornilov.authorization.brick_navigation.AuthorizationFlowScreen
import com.gmkornilov.authorization.di.AuthorizationDeps
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.navigation.BottomNavigationScreen
import com.gmkornilov.postium.R
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ProfileBottomNavigationsDeps : AuthorizationDeps

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileBottomNavigationItem @Inject constructor(
    private val authInteractor: AuthInteractor,
    parentRouter: TreeRouter,
    bottomNavigationScreen: BottomNavigationScreen,
    private val authorizationFlowScreen: AuthorizationFlowScreen,
    private val profileBottomNavigationItem: ProfileBottomNavigationsDeps
) : BottomNavigationItem {
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

    private val userResultHandler = object : UserResultHandler {
        override fun handleResult(user: PostiumUser?) {
            // TODO: navigate to some screen if authorization was unsuccessful
            user?.let {
                // TODO: add root profile screen
                // router.newRootScreen()
                router.backScreen()
            }
        }

    }

    @Composable
    override fun TitleComposable() {
        Text(stringResource(R.string.profile_tab))
    }

    override fun onSelected() {
        super.onSelected()

        if (authInteractor.getPostiumUser() == null) {
            router.addScreen(
                authorizationFlowScreen.screen,
                AuthorizationArgument(profileBottomNavigationItem, userResultHandler)
            )
        }
    }

    override val router: TreeRouter = parentRouter.branch(bottomNavigationScreen.screen.key)

    private fun TreeRouter.isEmpty(): Boolean {
        return this.screen.value == null
    }
}