package com.gmkornilov.user_playlists.playlist_add.domain

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.playlists.repository.PlaylistRepository
import com.gmkornilov.user_playlists.playlist_add.view.PlaylistItemState
import javax.inject.Inject

internal class PlaylistAddInteractor @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val playlistRepository: PlaylistRepository,
) {
    private val playlistSelectionStatuses = emptyMap<Playlist, Boolean>().toMutableMap()
    private lateinit var initialSelectionStatuses: Map<Playlist, Boolean>

    suspend fun loadData(postId: String): List<PlaylistItemState> {
        val user = authInteractor.getPostiumUser() ?: return emptyList()
        val playlists = playlistRepository.getUserPlaylists(user.getUid())

        val playlistMaps = playlists.map {
            it to it.postIds.contains(postId)
        }.toMap()

        initialSelectionStatuses = playlistMaps
        playlistSelectionStatuses.putAll(playlistMaps)

        return playlists.map { PlaylistItemState(it, playlistMaps.getValue(it)) }
    }

    suspend fun submitData(postId: String) {
        val user = authInteractor.getPostiumUser() ?: return
        playlistSelectionStatuses.forEach {
            val isAdded = it.value && !initialSelectionStatuses.getValue(it.key)
            val isRemoved = !it.value && initialSelectionStatuses.getValue(it.key)
            when {
                isAdded -> playlistRepository.addPostToPlaylist(user.getUid(), postId, it.key.id)
                isRemoved -> playlistRepository.removePostFromPlaylist(
                    user.getUid(),
                    postId,
                    it.key.id
                )
            }
        }
    }

    fun selectPlaylist(playlist: Playlist, isSelected: Boolean) {
        playlistSelectionStatuses[playlist] = isSelected
    }
}