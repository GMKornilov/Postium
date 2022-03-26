package com.gmkornilov.post

data class Post(
    val id: String = "",
    val title: String = "",
    val likes: Int = 0,
    val dislikes: Int = 0,
)
