package com.gmkornilov.comments.model

import com.gmkornilov.comment_likes.model.CommentLikeStatus as DataLikeStatus

enum class CommentLikeStatus(val isLiked: Boolean = false, val isDisliked: Boolean = false) {
    LIKED(isLiked = true),
    DISLIKED(isDisliked = true),
    NONE;

    fun toOppositeLikeStatus() = when (this) {
        LIKED -> NONE
        else -> LIKED
    }

    fun toOppositeDislikeStatus() = when (this) {
        DISLIKED -> NONE
        else -> DISLIKED
    }
}

fun DataLikeStatus?.toCommentLikeStatus() = when (this) {
    DataLikeStatus.LIKED -> CommentLikeStatus.LIKED
    DataLikeStatus.DISLIKED -> CommentLikeStatus.DISLIKED
    else -> CommentLikeStatus.NONE
}

fun CommentLikeStatus.toDataLikeStatus() = when (this) {
    CommentLikeStatus.LIKED -> DataLikeStatus.LIKED
    CommentLikeStatus.DISLIKED -> DataLikeStatus.DISLIKED
    CommentLikeStatus.NONE -> DataLikeStatus.NONE
}

