package com.gmkornilov.mainpage.model

data class PostPreviewData(
    val id: String,
    val title: String,
    val likeStatus: PostPreviewLikeStatus,
    val bookmarkStatus: PostPreviewBookmarkStatus,
)