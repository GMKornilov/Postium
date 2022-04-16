package com.gmkornilov.userpage.view

import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.post.model.PostPreviewData

internal interface UserPageEvents {
    fun tabSelected(tab: Tab)

    fun likePost(postPreviewItem: TabListItem.PostPreviewItem)

    fun dislikePost(postPreviewItem: TabListItem.PostPreviewItem)

    fun bookmarkPost(postPreviewItem: TabListItem.PostPreviewItem)

    fun openPost(postPreviewItem: TabListItem.PostPreviewItem)

    fun openProfile(postPreviewData: PostPreviewData)

    fun addToPlaylists(post: PostPreviewData)

    fun openPlaylist(playlist: Playlist)

    fun loadHeader()

    fun createPost()

    fun refreshData()

    companion object : UserPageEvents {
        override fun tabSelected(tab: Tab) = Unit
        override fun likePost(postPreviewItem: TabListItem.PostPreviewItem) = Unit
        override fun dislikePost(postPreviewItem: TabListItem.PostPreviewItem) = Unit
        override fun bookmarkPost(postPreviewItem: TabListItem.PostPreviewItem) = Unit
        override fun openPost(postPreviewItem: TabListItem.PostPreviewItem) = Unit
        override fun openProfile(postPreviewData: PostPreviewData) = Unit
        override fun addToPlaylists(post: PostPreviewData) = Unit
        override fun openPlaylist(playlist: Playlist) = Unit
        override fun loadHeader() = Unit
        override fun createPost() = Unit
        override fun refreshData() = Unit
    }
}