package com.gmkornilov.user_playlists.playlist_posts.view

import com.gmkornilov.post.model.PostPreviewData

internal interface PlaylistPostsEvents {
    fun refreshData()

    fun openPost(post: PostPreviewData)

    fun openProfile(post: PostPreviewData)

    fun likePost(post: PostPreviewData)

    fun dislikePost(post: PostPreviewData)

    fun bookmarkPost(post: PostPreviewData)

    companion object  : PlaylistPostsEvents {
            override fun refreshData() = Unit
            override fun openPost(post: PostPreviewData) = Unit
            override fun openProfile(post: PostPreviewData) = Unit
            override fun likePost(post: PostPreviewData) = Unit
            override fun dislikePost(post: PostPreviewData) = Unit
            override fun bookmarkPost(post: PostPreviewData) = Unit
    }
}