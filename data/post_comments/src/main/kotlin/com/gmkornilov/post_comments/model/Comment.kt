package com.gmkornilov.post_comments.model

import com.google.firebase.firestore.DocumentReference

data class Comment(
    val id: String = "",
    val text: String = "",
    val likes: Int = 0,
    val dislikes: Int = 0,
    val user: DocumentReference? = null,
)