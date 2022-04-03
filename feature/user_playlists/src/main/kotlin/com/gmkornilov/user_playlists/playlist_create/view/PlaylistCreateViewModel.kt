package com.gmkornilov.user_playlists.playlist_create.view

import com.gmkornilov.user_playlists.playlist_create.domain.PlaylistCreateInteractor
import com.gmkornilov.user_playlists.playlist_create.domain.PlaylistCreateResultHandler
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

internal class PlaylistCreateViewModel @Inject constructor(
    private val playlistCreateResultHandler: PlaylistCreateResultHandler,
    private val playlistCreateInteractor: PlaylistCreateInteractor,
    private val listener: PlaylistCreateListener,
): BaseViewModel<PlaylistCreateState, Unit>() {
    override fun getBaseState() = PlaylistCreateState()

    fun onCloseBottomSheet() {
        listener.exitRootScreen()
    }

    fun createPlaylist(playlistName: String) = intent {
        if (playlistName.isBlank()) {
            return@intent
        }
        reduce { this.state.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val playlist = playlistCreateInteractor.createPost(playlistName)
                playlistCreateResultHandler.handleResult(playlist)
            } catch (e: Exception) {
                reduce { this.state.copy(isLoading = false) }
            }
        }
    }
}

interface PlaylistCreateListener {
    fun exitRootScreen()
}