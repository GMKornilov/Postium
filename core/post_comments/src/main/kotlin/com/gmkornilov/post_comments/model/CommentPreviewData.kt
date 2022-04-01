package com.gmkornilov.post_comments.model

data class CommentPreviewData(
    val id: String,
    val userId: String,
    val comment: String,
    val username: String,
    val avatarUrl: String?,
    val likeStatus: CommentLikeStatus,
)