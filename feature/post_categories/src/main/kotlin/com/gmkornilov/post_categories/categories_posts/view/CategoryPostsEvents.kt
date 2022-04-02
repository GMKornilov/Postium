package com.gmkornilov.post_categories.categories_posts.view

import com.gmkornilov.post.model.PostPreviewData

interface CategoryPostsEvents {
    fun refreshData()

    fun openPost(post: PostPreviewData)

    fun openProfile(post: PostPreviewData)

    fun likePost(post: PostPreviewData)

    fun dislikePost(post: PostPreviewData)

    fun bookmarkPost(post: PostPreviewData)

    companion object : CategoryPostsEvents {
        override fun refreshData() = Unit
        override fun openPost(post: PostPreviewData) = Unit
        override fun openProfile(post: PostPreviewData) = Unit
        override fun likePost(post: PostPreviewData) = Unit
        override fun dislikePost(post: PostPreviewData) = Unit
        override fun bookmarkPost(post: PostPreviewData) = Unit
    }
}