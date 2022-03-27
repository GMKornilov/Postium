package com.gmkornilov.post_likes

enum class PostLikeStatus {
    LIKED,
    DISLIKED,
    NONE
}

fun Boolean?.toPostLikeStatus(): PostLikeStatus {
    return when (this) {
        true -> PostLikeStatus.LIKED
        false -> PostLikeStatus.DISLIKED
        null -> PostLikeStatus.NONE
    }
}