package com.gmkornilov.authorization.registration.domain

import com.gmkornilov.authorization.R
import com.gmkornilov.strings.StringsProvider
import javax.inject.Inject

class RegistrationStringsProvider @Inject constructor(
    private val stringsProvider: StringsProvider,
) {
    fun getPasswordDontMatch(): String {
        return stringsProvider.getStringById(R.string.password_dont_match)
    }

    fun getWeakPassword(): String {
        return stringsProvider.getStringById(R.string.weak_password)
    }

    fun getMalformedEmail(): String {
        return stringsProvider.getStringById(R.string.malformed_email)
    }

    fun getUserAlreadyExists(): String {
        return stringsProvider.getStringById(R.string.user_already_exists)
    }
}