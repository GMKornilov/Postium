package com.gmkornilov.comment_likes.model

internal data class CommentLikes(
    val likes: Map<String, Boolean> = emptyMap(),
)
