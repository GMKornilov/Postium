package com.gmkornilov.authorizarion.facebook

sealed class FacebookAuthStatus {
    data class AuthSuccessful(val token: String): FacebookAuthStatus()

    object AuthCancelled: FacebookAuthStatus()

    object AuthError: FacebookAuthStatus()
}