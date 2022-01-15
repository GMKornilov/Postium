package com.gmkornilov.authorizarion.email

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class EmailAuthInteractorImpl @Inject constructor(
    private val authInteractor: AuthInteractor,
): EmailAuthInteractor {
    @ExperimentalCoroutinesApi
    override fun signIn(login: String, password: String): Flow<Boolean> {
        val credential = EmailAuthProvider.getCredential(login, password)
        return authInteractor.signInWithCredential(credential)
    }
}