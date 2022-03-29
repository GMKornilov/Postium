package com.gmkornilov.userpage.view

import com.gmkornilov.view_model.BaseViewModel
import javax.inject.Inject

internal class UserPageViewModel @Inject constructor(

): BaseViewModel<UserPageState, Unit>(), UserPageEvents {
    override fun getBaseState(): UserPageState {
        return UserPageState()
    }
}