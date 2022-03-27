package com.gmkornilov.post

import com.google.firebase.firestore.DocumentReference

data class Post(
    val id: String = "",
    val title: String = "",
    val likes: Int = 0,
    val dislikes: Int = 0,
    val userReference: DocumentReference? = null,
)
