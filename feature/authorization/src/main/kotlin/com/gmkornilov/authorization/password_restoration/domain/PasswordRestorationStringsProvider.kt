package com.gmkornilov.authorization.password_restoration.domain

import com.gmkornilov.authorization.R
import com.gmkornilov.strings.StringsProvider
import javax.inject.Inject

class PasswordRestorationStringsProvider @Inject constructor(
    private val stringsProvider: StringsProvider,
) {
    fun getUserDoesntExist(): String {
        return stringsProvider.getStringById(R.string.user_doesnt_exist)
    }
}