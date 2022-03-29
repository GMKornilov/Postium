package com.gmkornilov.post.model

import com.gmkornilov.post_likes.PostLikeStatus as DataLikeStatus

enum class PostLikeStatus(val isLiked: Boolean = false, val isDisliked: Boolean = false) {
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

fun PostLikeStatus.toDataLikeStatus() = when (this) {
    PostLikeStatus.LIKED -> DataLikeStatus.LIKED
    PostLikeStatus.DISLIKED -> DataLikeStatus.DISLIKED
    PostLikeStatus.NONE -> DataLikeStatus.NONE
}

fun DataLikeStatus?.toPostLikeStatus() = when (this) {
    DataLikeStatus.LIKED -> PostLikeStatus.LIKED
    DataLikeStatus.DISLIKED -> PostLikeStatus.DISLIKED
    DataLikeStatus.NONE -> PostLikeStatus.NONE
    null -> PostLikeStatus.NONE
}