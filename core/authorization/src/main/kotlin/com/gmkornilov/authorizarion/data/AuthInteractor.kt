package com.gmkornilov.authorizarion.data

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthInteractor {
    @ExperimentalCoroutinesApi
    val authState: StateFlow<FirebaseUser?>

    @ExperimentalCoroutinesApi
    fun isAuthorized(): Boolean

    @ExperimentalCoroutinesApi
    fun signInWithCredential(credential: AuthCredential): Flow<Boolean>

    fun signOut()
}