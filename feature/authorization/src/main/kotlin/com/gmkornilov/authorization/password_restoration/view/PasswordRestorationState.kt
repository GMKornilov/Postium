package com.gmkornilov.authorization.password_restoration.view

sealed class PasswordRestorationState {
    data class EnterEmail(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
    ): PasswordRestorationState()

    object EmailSend: PasswordRestorationState()
}