package com.gmkornilov.authorization.registration

sealed class RegistrationState {
    object None: RegistrationState()

    object PasswordDontMathc: RegistrationState()
}