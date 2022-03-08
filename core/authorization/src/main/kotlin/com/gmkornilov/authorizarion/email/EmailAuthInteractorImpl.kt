package com.gmkornilov.authorizarion.email

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.google.firebase.auth.*
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

    override suspend fun createUser(email: String, password: String): EmailRegisterResult {
        return try {
            val signInResult = authInteractor.createUser(email, password)
            signInResult.toEmailRegisterResult()
        } catch (_: FirebaseAuthWeakPasswordException) {
            EmailRegisterResult.WeakPassword
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            EmailRegisterResult.MalformedEmail
        } catch (_: FirebaseAuthUserCollisionException) {
            EmailRegisterResult.UserAlreadyExists
        }
    }
}