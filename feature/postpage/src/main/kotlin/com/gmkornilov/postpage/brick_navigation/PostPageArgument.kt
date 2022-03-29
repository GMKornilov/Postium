package com.gmkornilov.postpage.brick_navigation

import com.gmkornilov.postpage.view.PostpageBookmarkStatus
import com.gmkornilov.postpage.view.PostpageLikeStatus

data class PostPageArgument(
    val id: String,
    val title: String,
    val username: String?,
    val avatarUrl: String?,
    val likeStatus: PostpageLikeStatus,
    val bookmarkStatus: PostpageBookmarkStatus,
)