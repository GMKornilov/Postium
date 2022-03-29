package com.gmkornilov.mainpage.mainpage

import com.gmkornilov.post.model.PostPreviewData

internal interface MainPageEvents {
    fun selectTimeRange(postTimeRange: PostTimeRange)

    fun openPost(post: PostPreviewData)

    fun openProfile(post: PostPreviewData)

    fun likePost(post: PostPreviewData)

    fun dislikePost(post: PostPreviewData)

    fun bookmarkPost(post: PostPreviewData)

    companion object {
        val MOCK = object : MainPageEvents {
            override fun selectTimeRange(postTimeRange: PostTimeRange) = Unit
            override fun openPost(post: PostPreviewData) = Unit
            override fun openProfile(post: PostPreviewData) = Unit
            override fun likePost(post: PostPreviewData) = Unit
            override fun dislikePost(post: PostPreviewData) = Unit
            override fun bookmarkPost(post: PostPreviewData) = Unit
        }
    }
}