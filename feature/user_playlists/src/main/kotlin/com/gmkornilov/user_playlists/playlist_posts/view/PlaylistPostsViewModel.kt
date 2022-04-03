package com.gmkornilov.user_playlists.playlist_posts.view

import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.post_list.view.PostListEvents
import com.gmkornilov.post_list.view.PostListViewModel
import com.gmkornilov.view_model.BaseViewModel
import javax.inject.Inject

internal class PlaylistPostsViewModel @Inject constructor(
    playlist: Playlist,
    val listViewModel: PostListViewModel,
): BaseViewModel<Unit, Unit>(), PostListEvents by listViewModel {
    val playlistName = playlist.name

    override fun getBaseState() = Unit

    override fun onDestroy() {
        super.onDestroy()
        listViewModel.onDestroy()
    }
}