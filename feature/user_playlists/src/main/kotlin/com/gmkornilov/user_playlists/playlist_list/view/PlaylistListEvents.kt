package com.gmkornilov.user_playlists.playlist_list.view

import com.gmkornilov.playlists.model.Playlist

internal interface PlaylistListEvents {
    fun refreshData()

    fun openPlaylist(playlist: Playlist)

    fun createPlaylist()

    companion object: PlaylistListEvents {
        override fun refreshData() = Unit
        override fun openPlaylist(playlist: Playlist) = Unit
        override fun createPlaylist() = Unit
    }
}