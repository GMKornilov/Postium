package com.gmkornilov.user_playlists.playlist_add.view

import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.playlists.model.Playlist

internal data class PlaylistAddState(
    val listState: ListState<PlaylistItemState> = ListState.None,
    val isLoading: Boolean = false,
)

internal data class PlaylistItemState(val playlist: Playlist, val isSelected: Boolean = false)