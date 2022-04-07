package com.gmkornilov.userpage.domain

import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.playlists.repository.PlaylistRepository
import com.gmkornilov.post.model.PostBookmarkStatus
import com.gmkornilov.post.model.PostLikeStatus
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.post.repository.PostRepository
import com.gmkornilov.user.model.User
import com.gmkornilov.user.repository.UserRepository
import javax.inject.Inject

internal class UserPageInteractor @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val playlistRepository: PlaylistRepository,
) {
    suspend fun loadPlaylists(userId: String): List<Playlist> {
        return playlistRepository.getUserPlaylists(userId)
    }

    suspend fun loadPosts(userId: String): List<PostPreviewData> {
        return postRepository.loadDataWithUserId(userId)
    }

    suspend fun loadHeader(userId: String): User {
        return userRepository.getUser(userId)
    }

    suspend fun loadBookmarks(userId: String): List<PostPreviewData> {
        return postRepository.loadUserBookmarks(userId)
    }

    suspend fun setLikeStatus(postPreviewData: PostPreviewData, likeStatus: PostLikeStatus) {
        postRepository.setLikeStatus(postPreviewData.id, likeStatus)
    }

    suspend fun setBookmarkStatus(
        postPreviewData: PostPreviewData,
        bookmarkStatus: PostBookmarkStatus
    ) {
        postRepository.setBookmarkStatus(postPreviewData.id, bookmarkStatus)
    }
}