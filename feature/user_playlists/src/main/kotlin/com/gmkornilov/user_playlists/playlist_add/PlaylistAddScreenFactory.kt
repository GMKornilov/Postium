package com.gmkornilov.user_playlists.playlist_add

import androidx.compose.runtime.Composable
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.brick_navigation.ScreenFactory
import com.gmkornilov.playlists.repository.PlaylistRepository
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.user_playlists.playlist_add.view.PlaylistAdd
import com.gmkornilov.user_playlists.playlist_add.view.PlaylistAddListener
import com.gmkornilov.user_playlists.playlist_add.view.PlaylistAddViewModel
import com.gmkornilov.view_model.BaseViewModel
import dagger.BindsInstance
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Scope

private const val PLAYLIST_ADD_KEY = "playlist_add"

class PlaylistAddScreenFactory @Inject constructor(
    override val dependency: Deps,
): DependencyProvider<PlaylistAddScreenFactory.Deps> {
    private inner class Factory(
        private val listener: PlaylistAddListener,
        private val postPreviewData: PostPreviewData,
    ): ScreenFactory() {
        override fun buildKey(): String {
            return PLAYLIST_ADD_KEY
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer,
        ): BaseViewModel<*, *> {
            val component = DaggerPlaylistAddScreenFactory_Component.builder()
                .listener(listener)
                .postPreviewData(postPreviewData)
                .deps(dependency)
                .build()

            return component.viewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<PlaylistAddViewModel>()
            PlaylistAdd(viewModel = viewModel)
        }
    }

    fun build(listener: PlaylistAddListener, postPreviewData: PostPreviewData, prevPath: String): Screen<*> {
        return Factory(listener, postPreviewData).build(prevPath)
    }

    interface Deps: Dependency {
        val authInteractor: AuthInteractor
        val playlistRepository: PlaylistRepository
    }

    @Scope
    annotation class PlaylistAddScope

    @dagger.Component(dependencies = [Deps::class], modules = [Module::class])
    @PlaylistAddScope
    internal interface Component {
        val viewModel: PlaylistAddViewModel

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun listener(playlistAddListener: PlaylistAddListener): Builder

            @BindsInstance
            fun postPreviewData(postPreviewData: PostPreviewData): Builder

            fun deps(dependency: Deps): Builder

            fun build(): Component
        }
    }

    @dagger.Module
    internal interface Module {

    }
}