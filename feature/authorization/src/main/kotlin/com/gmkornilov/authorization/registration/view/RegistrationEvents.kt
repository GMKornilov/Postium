package com.gmkornilov.authorization.registration.view

internal interface RegistrationEvents {
    fun registerUser(
        username: String,
        email: String,
        password: String,
        passwordConfirmation: String
    )

    companion object {
        val MOCK = object : RegistrationEvents {
            override fun registerUser(
                username: String,
                email: String,
                password: String,
                passwordConfirmation: String
            ) = Unit
        }
    }
}