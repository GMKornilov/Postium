package com.gmkornilov.mainpage.model

import com.gmkornilov.postpage.brick_navigation.PostPageArgument
import com.gmkornilov.userpage.brick_navigation.UserPageArgument

data class PostPreviewData(
    val id: String,
    val title: String,
    val username: String?,
    val avatarUrl: String?,
    val likeStatus: PostPreviewLikeStatus,
    val bookmarkStatus: PostPreviewBookmarkStatus,
)

fun PostPreviewData.toPostPageArgument() = PostPageArgument(
    id = id,
    title = title,
    username = username,
    avatarUrl = avatarUrl,
    likeStatus = likeStatus.toPostpageLikeStatus(),
    bookmarkStatus = bookmarkStatus.toPostPageBookmarkStatus(),
)

fun PostPreviewData.toUserPageArgument() = UserPageArgument(
    id = "",
    username = username,
    avatarUrl = avatarUrl
)