package com.gmkornilov.authorizarion.model

import com.google.firebase.auth.FirebaseUser

internal class PostiumUserImpl(
    private val firebaseUser: FirebaseUser
): PostiumUser {
    override fun getUid() = firebaseUser.uid

    override fun getDisplayName() = firebaseUser.displayName

    override fun getEmail() = firebaseUser.email

    override fun getProfilePhotoUrl() = firebaseUser.photoUrl?.toString()
}

internal fun FirebaseUser.toPostiumUser(): PostiumUser = PostiumUserImpl(this)