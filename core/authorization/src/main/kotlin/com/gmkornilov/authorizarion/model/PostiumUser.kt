package com.gmkornilov.authorizarion.model

interface PostiumUser {
    fun getDisplayName(): String?

    fun getEmail(): String?

    fun getProfilePhotoUrl(): String?
}