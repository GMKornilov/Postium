package com.gmkornilov.authorizarion.email

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class EmailAuthInteractorImpl @Inject constructor(
    private val authInteractor: AuthInteractor,
): EmailAuthInteractor {
    @ExperimentalCoroutinesApi
    override suspend fun signIn(login: String, password: String): EmailAuthResult {
        val credential = EmailAuthProvider.getCredential(login, password)
        return try {
            authInteractor.signInWithCredential(credential).toEmailAuthResult()
        } catch (_: FirebaseAuthInvalidUserException) {
            EmailAuthResult.UserDoesntExist
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            EmailAuthResult.WrongPassword
        }
    }
}