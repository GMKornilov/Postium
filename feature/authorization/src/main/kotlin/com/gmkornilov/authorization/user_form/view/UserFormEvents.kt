package com.gmkornilov.authorization.user_form.view

import android.net.Uri

internal interface UserFormEvents {
    fun photoUploadedClicked()

    fun photoUploaded(uri: Uri?)

    fun updateUsername(username: String)

    fun formFinished()

    companion object {
        val MOCK = object : UserFormEvents {
            override fun photoUploadedClicked() = Unit
            override fun photoUploaded(uri: Uri?) = Unit
            override fun updateUsername(username: String) = Unit
            override fun formFinished() = Unit
        }
    }
}