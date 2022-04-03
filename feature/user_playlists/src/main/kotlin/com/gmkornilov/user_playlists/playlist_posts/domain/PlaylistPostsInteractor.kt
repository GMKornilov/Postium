package com.gmkornilov.user_playlists.playlist_posts.domain

import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.post.model.PostBookmarkStatus
import com.gmkornilov.post.model.PostLikeStatus
import com.gmkornilov.post.repository.PostRepository
import javax.inject.Inject

internal class PlaylistPostsInteractor @Inject constructor(
    private val postRepository: PostRepository,
) {
    suspend fun loadPosts(playlist: Playlist) = postRepository.loadPlaylistPosts(playlist)

    suspend fun setLikeStatus(postId: String, likeStatus: PostLikeStatus) {
        postRepository.setLikeStatus(postId, likeStatus)
    }

    suspend fun setBookmarkStatus(postId: String, bookmarkStatus: PostBookmarkStatus) {
        postRepository.setBookmarkStatus(postId, bookmarkStatus)
    }
}