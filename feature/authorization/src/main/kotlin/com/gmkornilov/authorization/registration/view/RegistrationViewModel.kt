package com.gmkornilov.authorization.registration.view

import com.gmkornilov.authorizarion.email.EmailAuthInteractor
import com.gmkornilov.authorizarion.email.EmailRegisterResult
import com.gmkornilov.authorization.registration.domain.RegistrationFlowEvents
import com.gmkornilov.authorization.registration.domain.RegistrationStringsProvider
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

internal class RegistrationViewModel @Inject constructor(
    private val registrationFlowEvents: RegistrationFlowEvents,
    private val emailAuthInteractor: EmailAuthInteractor,
    private val registrationStringsProvider: RegistrationStringsProvider,
) : BaseViewModel<RegistrationState, RegistrationSideEffect>(), RegistrationEvents {
    override fun getBaseState(): RegistrationState {
        return RegistrationState.DEFAULT
    }

    override fun registerUser(email: String, password: String, passwordConfirmation: String) =
        intent {
            if (password != passwordConfirmation) {
                reduce {
                    RegistrationState(
                        passwordError = true,
                        passwordConfirmationError = true,
                        errorLabel = registrationStringsProvider.getPasswordDontMatch()
                    )
                }
                return@intent
            }
            registerUnsafe(email.trim(), password)
        }

    private fun registerUnsafe(email: String, password: String) = intent {
        viewModelScope.launch {
            reduce {
                this.state.copy(loading = true)
            }
            val result = emailAuthInteractor.createUser(email, password)
            reduce {
                when (result) {
                    is EmailRegisterResult.Success -> {
                        registrationFlowEvents.successfulRegistration(result.postiumUser)
                        RegistrationState.DEFAULT
                    }
                    EmailRegisterResult.WeakPassword -> RegistrationState(
                        passwordError = true,
                        passwordConfirmationError = true,
                        errorLabel = registrationStringsProvider.getWeakPassword(),
                    )
                    EmailRegisterResult.MalformedEmail -> RegistrationState(
                        emailError = true,
                        errorLabel = registrationStringsProvider.getMalformedEmail(),
                    )
                    EmailRegisterResult.UserAlreadyExists -> RegistrationState(
                        emailError = true,
                        passwordError = true,
                        errorLabel = registrationStringsProvider.getUserAlreadyExists(),
                    )
                }
            }
        }
    }
}