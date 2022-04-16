package com.gmkornilov.postpage.brick_navigation

import com.gmkornilov.post.model.PostBookmarkStatus
import com.gmkornilov.post.model.PostLikeStatus
import com.gmkornilov.post.model.PostPreviewData

data class PostPageArgument(
    val id: String,
    val title: String,
    val username: String,
    val userId: String,
    val avatarUrl: String?,
    val likeStatus: PostLikeStatus,
    val bookmarkStatus: PostBookmarkStatus,
    val likes: Int,
    val dislikes: Int,
    val comments: Int,
)

internal fun PostPageArgument.toPostPreviewData() = PostPreviewData(
    id = id,
    title = title,
    username = username,
    userId = userId,
    avatarUrl = avatarUrl,
    likeStatus = likeStatus,
    bookmarkStatus = bookmarkStatus,
    likes = likes,
    dislikes = dislikes,
    commentAmount = comments
)