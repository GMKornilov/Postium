package com.gmkornilov.postpage.view

enum class PostpageLikeStatus(val isLiked: Boolean = false, val isDisliked: Boolean = false) {
    LIKED(isLiked = true),
    DISLIKED(isDisliked = true),
    NONE;

    fun toOppositeLikeStatus() = when (this) {
        LIKED -> NONE
        DISLIKED, NONE -> LIKED
    }

    fun toOppositeDislikeStatus() = when (this) {
        DISLIKED -> NONE
        LIKED, NONE -> DISLIKED
    }
}