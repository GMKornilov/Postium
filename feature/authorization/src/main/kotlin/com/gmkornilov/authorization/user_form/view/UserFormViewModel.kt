package com.gmkornilov.authorization.user_form.view

import com.gmkornilov.view_model.BaseViewModel
import javax.inject.Inject

internal class UserFormViewModel @Inject constructor(

): BaseViewModel<UserFormState, UserFormSideEffect>() {
    override fun getBaseState(): UserFormState {
        return UserFormState()
    }
}