package com.gmkornilov.postpage.brick_navigation

import com.gmkornilov.post.model.PostBookmarkStatus
import com.gmkornilov.post.model.PostLikeStatus

data class PostPageArgument(
    val id: String,
    val title: String,
    val username: String,
    val avatarUrl: String?,
    val likeStatus: PostLikeStatus,
    val bookmarkStatus: PostBookmarkStatus,
)