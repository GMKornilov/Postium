package com.gmkornilov.user_playlists.playlist_list.view

import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.user_playlists.playlist_list.domain.PlaylistListInteractor
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

internal class PlaylistListViewModel @Inject constructor(
    private val playlistListInteractor: PlaylistListInteractor,
    private val listener: PlaylistListListener,
): BaseViewModel<PlaylistListState, Unit>(), PlaylistListEvents {
    override fun getBaseState(): PlaylistListState {
        return PlaylistListState()
    }

    fun loadData(isRefresh: Boolean = false) = intent {
        if (isRefresh) {
            reduce { this.state.copy(isRefreshing = true) }
        } else {
            reduce { this.state.copy(listState = ListState.Loading) }
        }

        viewModelScope.launch {
            try {
                val playlists = playlistListInteractor.getPlaylists()
                reduce { this.state.copy(listState = ListState.Success(playlists)) }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { this.state.copy(listState = ListState.Error(e)) }
            }
        }
    }

    override fun refreshData() {
        loadData(true)
    }

    override fun openPlaylist(playlist: Playlist) {
        listener.openPlaylist(playlist)
    }
}

interface PlaylistListListener {
    fun openPlaylist(playlist: Playlist)
}