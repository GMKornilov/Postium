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
import com.gmkornilov.authorization.feature_flow.AuthorizationFlowScreenFactory
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.postium.R
import com.gmkornilov.root_screen.RootScreenFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
class ProfileBottomNavigationItem @Inject constructor(
    private val authInteractor: AuthInteractor,
    rootRouter: TreeRouter,
    bottomNavigationScreenFactory: RootScreenFactory,
    private val authorizationFlowScreenFactory: AuthorizationFlowScreenFactory,
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
        override fun handleResult(user: PostiumUser) {
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
            authorizationFlowScreenFactory.start(userResultHandler, router)
        }
    }

    override val router: TreeRouter = rootRouter.branch(bottomNavigationScreenFactory.screenKey)
}