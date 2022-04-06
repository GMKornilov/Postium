package com.gmkornilov.authorization.registration.view

import com.gmkornilov.authorizarion.email.EmailAuthInteractor
import com.gmkornilov.authorizarion.email.EmailRegisterResult
import com.gmkornilov.authorizarion.model.PostiumUser
import com.gmkornilov.authorization.registration.domain.RegistrationFlowEvents
import com.gmkornilov.authorization.registration.domain.RegistrationStringsProvider
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.Dispatchers
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

    override fun registerUser(
        username: String,
        email: String,
        password: String,
        passwordConfirmation: String,
    ) =
        intent {
            if (username.isBlank()) {
                reduce {
                    RegistrationState(
                        usernameError = true,
                        errorLabel = registrationStringsProvider.getUsernameNotEmpty(),
                    )
                }
                return@intent
            }
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
            registerUnsafe(username.trim(), email.trim(), password)
        }

    private fun registerUnsafe(username: String, email: String, password: String) = intent {
        viewModelScope.launch(Dispatchers.IO) {
            reduce {
                this.state.copy(loading = true)
            }
            val result = emailAuthInteractor.createUser(email, password)
            reduce {
                when (result) {
                    is EmailRegisterResult.Success -> {
                        val userStub = object : PostiumUser {
                            override fun getUid() = result.postiumUser.getUid()
                            override fun getDisplayName() = username
                            override fun getEmail() = result.postiumUser.getEmail()
                            override fun getProfilePhotoUrl() = result.postiumUser.getProfilePhotoUrl()
                        }
                        viewModelScope.launch(Dispatchers.IO) {
                            registrationFlowEvents.successfulRegistration(userStub)
                        }
                        this.state
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