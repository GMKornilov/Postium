package com.gmkornilov.authorization.registration.view

import com.gmkornilov.authorization.registration.domain.RegistrationFlowEvents
import com.gmkornilov.view_model.BaseViewModel
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val registrationFlowEvents: RegistrationFlowEvents,
) : BaseViewModel<RegistrationState, RegistrationSideEffect>(), RegistrationEvents {
    override fun getBaseState(): RegistrationState {
        return RegistrationState.None
    }
}