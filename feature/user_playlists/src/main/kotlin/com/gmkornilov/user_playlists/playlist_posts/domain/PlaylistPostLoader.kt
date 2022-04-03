package com.gmkornilov.user_playlists.playlist_posts.domain

import com.gmkornilov.model.Post
import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.post.repository.PostRepository
import com.gmkornilov.source.FirebasePostSource
import javax.inject.Inject

class PlaylistPostLoader @Inject constructor(
    private val playlist: Playlist,
    private val firebasePostSource: FirebasePostSource,
): PostRepository.PostLoader {
    override suspend fun loadPosts(): List<Post> {
        return firebasePostSource.getPostWithIds(playlist.postIds)
    }
}