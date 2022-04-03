package com.gmkornilov.user_playlists.playlist_list.domain

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.playlists.repository.PlaylistRepository
import javax.inject.Inject

internal class PlaylistListInteractor @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val authInteractor: AuthInteractor,
) {
    suspend fun getPlaylists(): List<Playlist> {
        // TODO: add bookmark playlist
        val currentUser = authInteractor.getPostiumUser() ?: return emptyList()
        return playlistRepository.getUserPlaylists(currentUser.getUid())
    }
}