package com.gmkornilov.comment_likes.model

enum class CommentLikeStatus(val isLiked: Boolean = false, val isDisliked: Boolean = false) {
    LIKED(isLiked = true),
    DISLIKED(isDisliked = true),
    NONE
}

fun Boolean?.toCommentLikeStatus() = when (this) {
    true -> CommentLikeStatus.LIKED
    false -> CommentLikeStatus.DISLIKED
    else -> CommentLikeStatus.NONE
}