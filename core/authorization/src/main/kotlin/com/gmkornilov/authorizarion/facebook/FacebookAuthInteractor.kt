package com.gmkornilov.authorizarion.facebook

import com.gmkornilov.authorizarion.data.SignInResult
import kotlinx.coroutines.ExperimentalCoroutinesApi

interface FacebookAuthInteractor {
    @ExperimentalCoroutinesApi
    suspend fun signIn(): FacebookAuthStatus

    @ExperimentalCoroutinesApi
    suspend fun passToken(token: String): SignInResult
}