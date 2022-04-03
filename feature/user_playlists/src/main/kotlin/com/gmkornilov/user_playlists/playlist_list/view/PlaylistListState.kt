package com.gmkornilov.user_playlists.playlist_list.view

import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.playlists.model.Playlist

internal data class PlaylistListState(
    val listState: ListState<Playlist> = ListState.None,
    val isRefreshing: Boolean = false,
)
