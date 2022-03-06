package com.gmkornilov.authorization.registration.view

import com.gmkornilov.authorization.registration.domain.RegistrationFlowEvents
import com.gmkornilov.view_model.BaseViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val registrationFlowEvents: RegistrationFlowEvents,
) : BaseViewModel<RegistrationState, RegistrationSideEffect>(), RegistrationEvents {
    override fun getBaseState(): RegistrationState {
        return RegistrationState.None
    }

    override fun registerUser(username: String, password: String, passwordConfirmation: String) = intent {
        if (password != passwordConfirmation) {
            reduce {
                RegistrationState.PasswordDontMathc
            }
            return@intent
        }
    }
}