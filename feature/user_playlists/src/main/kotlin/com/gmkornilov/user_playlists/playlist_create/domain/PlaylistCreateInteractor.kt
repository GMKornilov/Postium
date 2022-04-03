package com.gmkornilov.user_playlists.playlist_create.domain

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.playlists.repository.PlaylistRepository
import javax.inject.Inject

internal class PlaylistCreateInteractor @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val authInteractor: AuthInteractor,
) {
    suspend fun createPost(playlistName: String): Playlist {
        val currentUser = authInteractor.getPostiumUser() ?: return Playlist()
        return playlistRepository.createPlaylist(currentUser.getUid(), playlistName)
    }
}