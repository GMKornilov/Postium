package com.gmkornilov.authorization.password_restoration.view

interface PasswordRestorationEvents {
    fun sendRestorationEmail(email: String)

    fun backToLogin()

    companion object {
        val MOCK = object : PasswordRestorationEvents {
            override fun sendRestorationEmail(email: String) = Unit
            override fun backToLogin() = Unit
        }
    }
}