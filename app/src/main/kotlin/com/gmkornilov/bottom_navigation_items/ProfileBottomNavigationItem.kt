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
import com.gmkornilov.postium.R
import com.gmkornilov.root_screen.ROOT_KEY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
class ProfileBottomNavigationItem @Inject constructor(
    private val authInteractor: AuthInteractor,
    rootRouter: TreeRouter,
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

    @Composable
    override fun TitleComposable() {
        Text(stringResource(R.string.profile_tab))
    }

    override val router: TreeRouter = rootRouter.branch(ROOT_KEY)
}