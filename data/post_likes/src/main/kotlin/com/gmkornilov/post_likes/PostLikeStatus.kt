package com.gmkornilov.post_likes

enum class PostLikeStatus(val isLiked: Boolean = false, val isDisliked: Boolean = false) {
    LIKED(isLiked = true),
    DISLIKED(isDisliked = true),
    NONE
}

fun Boolean?.toPostLikeStatus(): PostLikeStatus {
    return when (this) {
        true -> PostLikeStatus.LIKED
        false -> PostLikeStatus.DISLIKED
        null -> PostLikeStatus.NONE
    }
}