package com.gmkornilov.authorization.registration

import com.gmkornilov.view_model.BaseViewModel
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(

) : BaseViewModel<RegistrationState, RegistrationSideEffect>(), RegistrationEvents {
    override fun getBaseState(): RegistrationState {
        return RegistrationState()
    }
}