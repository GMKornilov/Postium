package com.gmkornilov.user_playlists.playlist_create.domain

import com.gmkornilov.playlists.model.Playlist

fun interface PlaylistCreateResultHandler {
    fun handleResult(playlist: Playlist)
}


fun PlaylistCreateResultHandler.merge(other: PlaylistCreateResultHandler) = PlaylistCreateResultHandler {
    this.handleResult(it)
    other.handleResult(it)
}