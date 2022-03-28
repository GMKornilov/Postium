package com.gmkornilov.postpage.postpage

enum class PostpageLikeStatus(val isLiked: Boolean = false, val isDisliked: Boolean = false) {
    LIKED(isLiked = true),
    DISLIKED(isDisliked = true),
    NONE;
}