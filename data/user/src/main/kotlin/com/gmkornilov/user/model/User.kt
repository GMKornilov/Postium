package com.gmkornilov.user.model

import com.google.firebase.firestore.PropertyName

data class User(
    val name: String = "",
    @get:PropertyName("avatar_url")
    @set:PropertyName("avatar_url")
    var avatarUrl: String = ""
)