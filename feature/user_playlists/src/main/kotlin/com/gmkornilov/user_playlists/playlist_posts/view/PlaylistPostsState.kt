package com.gmkornilov.user_playlists.playlist_posts.view

import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.post.model.PostPreviewData

internal data class PlaylistPostsState(
    val playlistName: String,
    val isRefreshing: Boolean = false,
    val listState: ListState<PostPreviewData> = ListState.None,
)
