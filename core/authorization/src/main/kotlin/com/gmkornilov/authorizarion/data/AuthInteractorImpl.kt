package com.gmkornilov.authorizarion.data

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthInteractorImpl @Inject internal constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthInteractor {
    @ExperimentalCoroutinesApi
    private val authStateFlow: MutableStateFlow<FirebaseUser?> = MutableStateFlow(null)

    @ExperimentalCoroutinesApi
    override val authState = authStateFlow

    @ExperimentalCoroutinesApi
    override fun signInWithCredential(credential: AuthCredential): Flow<Boolean> = callbackFlow {
        firebaseAuth
            .signInWithCredential(credential)
            .addOnCompleteListener {
                sendBlocking(it.isSuccessful)
            }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    @ExperimentalCoroutinesApi
    private fun initialize() {
        firebaseAuth.addAuthStateListener {
            authStateFlow.value = it.currentUser
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