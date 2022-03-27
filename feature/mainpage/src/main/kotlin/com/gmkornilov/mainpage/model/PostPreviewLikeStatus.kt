package com.gmkornilov.mainpage.model

import com.gmkornilov.post_likes.PostLikeStatus

enum class PostPreviewLikeStatus(val isLiked: Boolean = false, val isDisliked: Boolean = false) {
    LIKED(isLiked = true),
    DISLIKED(isDisliked = true),
    NONE,
}

fun PostLikeStatus?.toPostPreviewLikeStatus() = when (this) {
    PostLikeStatus.LIKED -> PostPreviewLikeStatus.LIKED
    PostLikeStatus.DISLIKED -> PostPreviewLikeStatus.DISLIKED
    PostLikeStatus.NONE -> PostPreviewLikeStatus.NONE
    null -> PostPreviewLikeStatus.NONE
}