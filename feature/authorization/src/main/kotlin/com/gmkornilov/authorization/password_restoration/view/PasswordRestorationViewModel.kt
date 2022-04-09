package com.gmkornilov.authorization.password_restoration.view

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.data.ResetPasswordResult
import com.gmkornilov.authorization.password_restoration.domain.PasswordRestorationFlowEvents
import com.gmkornilov.authorization.password_restoration.domain.PasswordRestorationStringsProvider
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

class PasswordRestorationViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val stringsProvider: PasswordRestorationStringsProvider,
    private val passwordRestorationFlowEvents: PasswordRestorationFlowEvents,
) : BaseViewModel<PasswordRestorationState, Unit>(), PasswordRestorationEvents {
    override fun getBaseState() = PasswordRestorationState.EnterEmail()

    override fun sendRestorationEmail(email: String) = intent {
        if (email.isEmpty()) {
            return@intent
        }
        val trimmedEmail = email.trim()
        reduce { PasswordRestorationState.EnterEmail(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newState = when (authInteractor.resetPassword(trimmedEmail)) {
                    ResetPasswordResult.Success -> PasswordRestorationState.EmailSend
                    ResetPasswordResult.UserDoesntExist -> PasswordRestorationState.EnterEmail(
                        isLoading = false,
                        errorMessage = stringsProvider.getUserDoesntExist()
                    )
                }
                reduce { newState }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { PasswordRestorationState.EnterEmail() }
            }
        }
    }

    override fun backToLogin() = passwordRestorationFlowEvents.passwordRestorationBBack()
}