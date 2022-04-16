package com.gmkornilov.post.model


data class PostPreviewData(
    val id: String,
    val title: String,
    val userId: String,
    val username: String,
    val avatarUrl: String?,
    val likeStatus: PostLikeStatus,
    val bookmarkStatus: PostBookmarkStatus,
    val likes: Int,
    val dislikes: Int,
    val commentAmount: Int,
)