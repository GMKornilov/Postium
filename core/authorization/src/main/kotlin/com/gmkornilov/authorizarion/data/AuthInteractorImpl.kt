package com.gmkornilov.authorizarion.data

import com.gmkornilov.authorizarion.model.PostiumUser
import com.gmkornilov.authorizarion.model.PostiumUserImpl
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

internal class AuthInteractorImpl @Inject internal constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthInteractor {
    @ExperimentalCoroutinesApi
    private val authStateFlow: MutableStateFlow<PostiumUser?> = MutableStateFlow(null)

    @ExperimentalCoroutinesApi
    override val authState = authStateFlow

    @ExperimentalCoroutinesApi
    override fun isAuthorized(): Boolean {
        return authStateFlow.value != null
    }

    @ExperimentalCoroutinesApi
    override fun signInWithCredential(credential: AuthCredential): Flow<Boolean> = callbackFlow {
        firebaseAuth
            .signInWithCredential(credential)
            .addOnCompleteListener {
                sendBlocking(it.isSuccessful)
            }

        awaitClose { }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    @ExperimentalCoroutinesApi
    private fun initialize() {
        firebaseAuth.addAuthStateListener {
            authStateFlow.value =
                it.currentUser?.let { firebaseUser -> PostiumUserImpl(firebaseUser) }
        }
    }

    companion object {
        @ExperimentalCoroutinesApi
        internal fun createInstance(firebaseAuth: FirebaseAuth): AuthInteractorImpl {
            return AuthInteractorImpl(firebaseAuth).apply {
                initialize()
            }
        }
    }
}