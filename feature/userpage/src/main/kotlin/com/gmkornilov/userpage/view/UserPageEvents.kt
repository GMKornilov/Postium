package com.gmkornilov.userpage.view

import com.gmkornilov.post.model.PostPreviewData

internal interface UserPageEvents {
    fun tabSelected(tab: Tab)

    fun likePost(postPreviewData: PostPreviewData)

    fun dislikePost(postPreviewData: PostPreviewData)

    fun bookmarkPost(postPreviewData: PostPreviewData)

    fun openPost(postPreviewData: PostPreviewData)

    companion object : UserPageEvents {
        override fun tabSelected(tab: Tab) = Unit
        override fun likePost(postPreviewData: PostPreviewData) = Unit
        override fun dislikePost(postPreviewData: PostPreviewData) = Unit
        override fun bookmarkPost(postPreviewData: PostPreviewData) = Unit
        override fun openPost(postPreviewData: PostPreviewData) = Unit
    }
}