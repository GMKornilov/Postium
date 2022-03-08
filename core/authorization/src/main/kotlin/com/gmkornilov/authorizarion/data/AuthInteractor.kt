package com.gmkornilov.authorizarion.data

import com.gmkornilov.authorizarion.model.PostiumUser
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

interface AuthInteractor {
    @ExperimentalCoroutinesApi
    val authState: StateFlow<PostiumUser?>

    fun getPostiumUser(): PostiumUser?

    @ExperimentalCoroutinesApi
    fun isAuthorized(): Boolean

    @ExperimentalCoroutinesApi
    suspend fun signInWithCredential(credential: AuthCredential): SignInResult

    suspend fun createUser(email: String, password: String): SignInResult

    fun signOut()
}

sealed class SignInResult {
    data class NewUser(val user: PostiumUser): SignInResult()

    data class ExistingUser(val user: PostiumUser): SignInResult()
}