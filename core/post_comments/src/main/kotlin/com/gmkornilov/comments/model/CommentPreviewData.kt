package com.gmkornilov.comments.model

data class CommentPreviewData(
    val id: String,
    val userId: String,
    val comment: String,
    val username: String,
    val avatarUrl: String?,
    val likeStatus: CommentLikeStatus,
    val likes: Int,
    val dislikes: Int,
)