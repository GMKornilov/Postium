package com.gmkornilov.authorizarion.data

import com.gmkornilov.authorizarion.model.PostiumUser
import com.gmkornilov.authorizarion.model.PostiumUserImpl
import com.gmkornilov.authorizarion.model.toPostiumUser
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthInteractorImpl @Inject internal constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthInteractor {
    @ExperimentalCoroutinesApi
    private val authStateFlow: MutableStateFlow<PostiumUser?> = MutableStateFlow(null)

    @ExperimentalCoroutinesApi
    override val authState = authStateFlow

    override fun getPostiumUser(): PostiumUser? {
        return authStateFlow.value
    }

    @ExperimentalCoroutinesApi
    override fun isAuthorized(): Boolean {
        return authStateFlow.value != null
    }

    @ExperimentalCoroutinesApi
    override suspend fun signInWithCredential(credential: AuthCredential): SignInResult {
        val result = firebaseAuth.signInWithCredential(credential).await()

        val isNewUser = result.additionalUserInfo?.isNewUser ?: false

        return if (isNewUser) {
            SignInResult.NewUser(result.user!!.toPostiumUser())
        } else {
            SignInResult.ExistingUser(result.user!!.toPostiumUser())
        }
    }

    override suspend fun createUser(email: String, password: String): SignInResult {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return SignInResult.NewUser(result.user!!.toPostiumUser())
    }

    override suspend fun resetPassword(email: String): ResetPasswordResult {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            ResetPasswordResult.Success
        } catch (e: FirebaseAuthInvalidUserException) {
            ResetPasswordResult.UserDoesntExist
        }
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