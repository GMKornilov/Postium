package com.gmkornilov.authorizarion.facebook

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface FacebookAuthInteractor {
    @ExperimentalCoroutinesApi
    fun signIn(): Flow<FacebookAuthStatus>

    @ExperimentalCoroutinesApi
    fun passToken(token: String): Flow<Boolean>
}