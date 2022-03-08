package com.gmkornilov.authorizarion.email

import com.gmkornilov.authorizarion.data.SignInResult
import com.gmkornilov.authorizarion.model.PostiumUser
import kotlinx.coroutines.ExperimentalCoroutinesApi

interface EmailAuthInteractor {
    @ExperimentalCoroutinesApi
    suspend fun signIn(login: String, password: String): EmailAuthResult

    suspend fun createUser(email: String, password: String): EmailRegisterResult
}

sealed class EmailAuthResult {
    data class Success(val postiumUser: PostiumUser): EmailAuthResult()

    object UserDoesntExist: EmailAuthResult()

    object WrongPassword: EmailAuthResult()
}

internal fun SignInResult.toEmailAuthResult() = when (this) {
    is SignInResult.ExistingUser -> EmailAuthResult.Success(this.user)
    is SignInResult.NewUser -> EmailAuthResult.Success(this.user)
}


sealed class EmailRegisterResult {
    data class Success(val postiumUser: PostiumUser): EmailRegisterResult()

    object WeakPassword : EmailRegisterResult()

    object MalformedEmail: EmailRegisterResult()

    object UserAlreadyExists: EmailRegisterResult()
}

internal fun SignInResult.toEmailRegisterResult() = when (this) {
    is SignInResult.ExistingUser -> EmailRegisterResult.Success(this.user)
    is SignInResult.NewUser -> EmailRegisterResult.Success(this.user)
}