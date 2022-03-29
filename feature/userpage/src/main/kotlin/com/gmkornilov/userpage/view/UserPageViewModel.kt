package com.gmkornilov.userpage.view

import android.util.Log
import com.gmkornilov.userpage.brick_navigation.UserPageArgument
import com.gmkornilov.view_model.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

internal class UserPageViewModel @Inject constructor(
    private val navArgument: UserPageArgument,
): BaseViewModel<UserPageState, Unit>(), UserPageEvents {
    override fun getBaseState(): UserPageState {
        return UserPageState(
            headerState = HeaderState(navArgument.username, navArgument.avatarUrl)
        )
    }

    override fun tabSelected(tab: Tab) {
        Timber.log(Log.INFO, tab.toString())
    }
}