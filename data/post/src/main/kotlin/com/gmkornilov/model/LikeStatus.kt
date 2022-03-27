package com.gmkornilov.model

enum class LikeStatus(val isLiked: Boolean = false, val isDisliked: Boolean = false) {
    LIKED(isLiked = true),
    DISLIKED(isDisliked = true),
    NONE,
}