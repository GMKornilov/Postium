package com.gmkornilov.post_comments.model

import com.google.firebase.firestore.DocumentReference

data class Comment(
    val id: String = "",
    val text: String = "",
    val likes: String = "",
    val dislikes: String = "",
    val user: DocumentReference? = null,
)