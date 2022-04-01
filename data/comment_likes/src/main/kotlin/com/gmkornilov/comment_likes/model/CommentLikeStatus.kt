package com.gmkornilov.comment_likes.model

enum class CommentLikeStatus {
    LIKED,
    DISLIKED,
    NONE
}

fun Boolean?.toCommentLikeStatus() = when (this) {
    true -> CommentLikeStatus.LIKED
    false -> CommentLikeStatus.DISLIKED
    else -> CommentLikeStatus.NONE
}