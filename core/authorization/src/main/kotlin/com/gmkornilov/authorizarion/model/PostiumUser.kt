package com.gmkornilov.authorizarion.model

interface PostiumUser {
    fun getUid(): String

    fun getDisplayName(): String?

    fun getEmail(): String?

    fun getProfilePhotoUrl(): String?
}