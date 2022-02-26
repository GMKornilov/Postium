package com.gmkornilov.authorization.registration.view

sealed class RegistrationState {
    object None: RegistrationState()

    object PasswordDontMathc: RegistrationState()
}