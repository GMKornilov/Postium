package com.gmkornilov.post_likes

internal data class PostLikes(
    val likes: Map<String, Boolean> = emptyMap(),
)
