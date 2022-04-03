package com.gmkornilov.user_playlists.playlist_create

import androidx.compose.runtime.Composable
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.brick_navigation.ScreenFactory
import com.gmkornilov.playlists.repository.PlaylistRepository
import com.gmkornilov.user_playlists.playlist_create.domain.PlaylistCreateResultHandler
import com.gmkornilov.user_playlists.playlist_create.view.PlaylistCreate
import com.gmkornilov.user_playlists.playlist_create.view.PlaylistCreateListener
import com.gmkornilov.user_playlists.playlist_create.view.PlaylistCreateViewModel
import com.gmkornilov.view_model.BaseViewModel
import dagger.BindsInstance
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

private const val PLAYLIST_CREATE_KEY = "playlist_create"

class PlaylistCreateScreenFactory @Inject constructor(
    override val dependency: Deps,
) : DependencyProvider<PlaylistCreateScreenFactory.Deps> {
    private inner class Factory(
        private val handler: PlaylistCreateResultHandler,
        private val listener: PlaylistCreateListener,
    ) : ScreenFactory() {
        override fun buildKey(): String {
            return PLAYLIST_CREATE_KEY
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer,
        ): BaseViewModel<*, *> {
            val component = DaggerPlaylistCreateScreenFactory_Component.builder()
                .deps(dependency)
                .listener(listener)
                .resultHandler(handler)
                .build()
            return component.viewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<PlaylistCreateViewModel>()
            PlaylistCreate(viewModel = viewModel)
        }
    }

    fun build(listener: PlaylistCreateListener, handler: PlaylistCreateResultHandler, prevPath: String): Screen<*> {
        return Factory(handler, listener).build(prevPath)
    }

    interface Deps : Dependency {
        val playlistRepository: PlaylistRepository
        val authInteractor: AuthInteractor
    }

    @dagger.Component(dependencies = [Deps::class])
    internal interface Component {
        val viewModel: PlaylistCreateViewModel

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun resultHandler(handler: PlaylistCreateResultHandler): Builder

            @BindsInstance
            fun listener(listener: PlaylistCreateListener): Builder

            fun deps(dependency: Deps): Builder

            fun build(): Component
        }
    }
}