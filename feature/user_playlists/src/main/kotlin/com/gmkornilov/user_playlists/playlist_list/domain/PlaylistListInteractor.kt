package com.gmkornilov.user_playlists.playlist_list.domain

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.playlists.repository.PlaylistRepository
import com.gmkornilov.source.FirebasePostSource
import javax.inject.Inject

internal class PlaylistListInteractor @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val firebasePostSource: FirebasePostSource,
    private val authInteractor: AuthInteractor,
) {
    suspend fun getPlaylists(): List<Playlist> {
        // TODO: add bookmark playlist
        val currentUser = authInteractor.getPostiumUser() ?: return emptyList()
        val playlists = playlistRepository.getUserPlaylists(currentUser.getUid())
        val actualPlaylists = playlists.map {
            val posts = firebasePostSource.getPostWithIds(it.postIds)
            it.copy(postIds = posts.map { post -> post.id })
        }
        return actualPlaylists
    }
}