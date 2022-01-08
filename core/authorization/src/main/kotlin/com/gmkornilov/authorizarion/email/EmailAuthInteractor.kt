package com.gmkornilov.authorizarion.email

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface EmailAuthInteractor {
    @ExperimentalCoroutinesApi
    fun signIn(login: String, password: String): Flow<Boolean>
}