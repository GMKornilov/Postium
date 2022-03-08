package com.gmkornilov.authorization.registration.view

internal sealed class RegistrationState {
    object None: RegistrationState()

    object PasswordDontMathc: RegistrationState()
}