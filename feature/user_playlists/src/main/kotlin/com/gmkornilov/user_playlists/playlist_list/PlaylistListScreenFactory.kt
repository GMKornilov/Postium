package com.gmkornilov.user_playlists.playlist_list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.brick_navigation.ScreenFactory
import com.gmkornilov.playlists.repository.PlaylistRepository
import com.gmkornilov.user_playlists.playlist_list.view.PlaylistList
import com.gmkornilov.user_playlists.playlist_list.view.PlaylistListListener
import com.gmkornilov.user_playlists.playlist_list.view.PlaylistListViewModel
import com.gmkornilov.view_model.BaseViewModel
import dagger.BindsInstance
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

private const val PLAYLISTS_KEY = "playlists"

class PlaylistListScreenFactory @Inject constructor(
    override val dependency: Deps,
): DependencyProvider<PlaylistListScreenFactory.Deps> {
    private inner class Factory(private val listener: PlaylistListListener): ScreenFactory() {
        override fun buildKey(): String {
            return PLAYLISTS_KEY
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer,
        ): BaseViewModel<*, *> {
            val component = DaggerPlaylistListScreenFactory_Component.builder()
                .listener(listener)
                .deps(dependency)
                .build()

            return component.viewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<PlaylistListViewModel>()
            
            PlaylistList(viewModel = viewModel, modifier = Modifier.fillMaxSize())
        }
    }

    fun build(listener: PlaylistListListener, prevPath: String): Screen<*> {
        return Factory(listener).build(prevPath)
    }
    
    interface Deps: Dependency {
        val playlistRepository: PlaylistRepository
        val authInteractor: AuthInteractor
    }

    @dagger.Component(dependencies = [Deps::class])
    internal interface Component {
        val viewModel: PlaylistListViewModel
        
        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun listener(listener: PlaylistListListener): Builder
            
            fun deps(dependency: Deps): Builder
            
            fun build(): Component
        }
    }
}