package com.gmkornilov.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class Post(
    val id: String = "",
    val title: String = "",
    val likes: Int = 0,
    val dislikes: Int = 0,

    @get:PropertyName("user")
    @set:PropertyName("user")
    var userReference: DocumentReference? = null,

    @get:PropertyName("category")
    @set:PropertyName("category")
    var categoryReference: DocumentReference? = null,
)
