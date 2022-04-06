package com.gmkornilov.user_playlists.playlist_add.view

import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.user_playlists.playlist_add.domain.PlaylistAddInteractor
import com.gmkornilov.user_playlists.playlist_create.domain.PlaylistCreateResultHandler
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

internal class PlaylistAddViewModel @Inject constructor(
    private val postPreviewData: PostPreviewData,
    private val playlistAddInteractor: PlaylistAddInteractor,
    private val listener: PlaylistAddListener,
): BaseViewModel<PlaylistAddState, Unit>() {
    override fun getBaseState(): PlaylistAddState {
        return PlaylistAddState()
    }

    fun loadData() = intent {
        reduce { this.state.copy(listState = ListState.Loading) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val playlists = playlistAddInteractor.loadData(postPreviewData.id)
                reduce { this.state.copy(listState = ListState.Success(playlists)) }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { this.state.copy(listState = ListState.Error(e)) }
            }
        }
    }

    fun selectPlaylist(playlistItemState: PlaylistItemState, isSelected: Boolean) = intent {
        playlistAddInteractor.selectPlaylist(playlistItemState.playlist, isSelected)
        reduce {
            val state = this.state.listState
            if (state !is ListState.Success) {
                return@reduce this.state
            }
            val newPlaylistItemState = playlistItemState.copy(isSelected = isSelected)
            val newList = state.contents.toMutableList().apply {
                val index = this.indexOf(playlistItemState)
                if (index != -1) {
                    this[index] = newPlaylistItemState
                }
            }
            this.state.copy(listState = ListState.Success(newList))
        }
    }

    fun submit() = intent {
        try {
            reduce { this.state.copy(isLoading = true) }
            playlistAddInteractor.submitData(postPreviewData.id)
            listener.exitRootScreen()
        } catch (e: Exception) {
            Timber.e(e)
            reduce { this.state.copy(isLoading = false) }
        }
    }

    fun onCloseBottomSheet() {
        listener.exitRootScreen()
    }
}

interface PlaylistAddListener {
    fun createPlaylist(playlistCreateResultHandler: PlaylistCreateResultHandler)

    fun exitRootScreen()
}