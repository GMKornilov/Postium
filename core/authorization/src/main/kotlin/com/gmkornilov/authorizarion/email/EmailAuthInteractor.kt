package com.gmkornilov.authorizarion.email

import com.gmkornilov.authorizarion.data.SignInResult
import com.gmkornilov.authorizarion.model.PostiumUser
import kotlinx.coroutines.ExperimentalCoroutinesApi

interface EmailAuthInteractor {
    @ExperimentalCoroutinesApi
    suspend fun signIn(login: String, password: String): EmailAuthResult
}

sealed class EmailAuthResult {
    data class ExistingUser(val postiumUser: PostiumUser): EmailAuthResult()

    data class NewUser(val postiumUser: PostiumUser): EmailAuthResult()

    object UserDoesntExist: EmailAuthResult()

    object WrongPassword: EmailAuthResult()
}

internal fun SignInResult.toEmailAuthResult() = when (this) {
    is SignInResult.ExistingUser -> EmailAuthResult.ExistingUser(this.user)
    is SignInResult.NewUser -> EmailAuthResult.NewUser(this.user)
}