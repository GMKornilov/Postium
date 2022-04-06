package com.gmkornilov.authorization.registration.view

internal data class RegistrationState(
    val loading: Boolean = false,
    val usernameError: Boolean = false,
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val passwordConfirmationError: Boolean = false,
    val errorLabel: String? = null,
) {
    companion object {
        val DEFAULT = RegistrationState()
    }
}