package com.gmkornilov.user_playlists.playlist_posts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.brick_navigation.ScreenFactory
import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.post.repository.PostRepository
import com.gmkornilov.user_playlists.playlist_posts.view.PlaylistPostsList
import com.gmkornilov.user_playlists.playlist_posts.view.PlaylistPostsListener
import com.gmkornilov.user_playlists.playlist_posts.view.PlaylistPostsViewModel
import com.gmkornilov.view_model.BaseViewModel
import dagger.BindsInstance
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Scope

private const val PLAYLIST_POSTS_KEY = "playlist_posts"

class PlaylistPostsScreenFactory @Inject constructor(
    override val dependency: Deps,
) : DependencyProvider<PlaylistPostsScreenFactory.Deps> {
    private inner class Factory(
        private val listener: PlaylistPostsListener,
        private val playlist: Playlist,
    ) : ScreenFactory() {
        override fun buildKey(): String {
            return "${PLAYLIST_POSTS_KEY}_${playlist.name}"
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer,
        ): BaseViewModel<*, *> {
            val component = DaggerPlaylistPostsScreenFactory_Component.builder()
                .listener(listener)
                .playlist(playlist)
                .deps(dependency)
                .build()
            return component.viewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<PlaylistPostsViewModel>()
            PlaylistPostsList(viewModel = viewModel, modifier = Modifier.fillMaxSize())
        }
    }

    fun build(listener: PlaylistPostsListener, playlist: Playlist, prevPath: String): Screen<*> {
        return Factory(listener, playlist).build(prevPath)
    }

    interface Deps : Dependency {
        val postsRepository: PostRepository
        val authInteractor: AuthInteractor
    }

    @Scope
    internal annotation class PlaylistPostsScope

    @dagger.Component(dependencies = [Deps::class])
    @PlaylistPostsScope
    internal interface Component {
        val viewModel: PlaylistPostsViewModel

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun playlist(playlist: Playlist): Builder

            @BindsInstance
            fun listener(listener: PlaylistPostsListener): Builder

            fun deps(dependency: Deps): Builder

            fun build(): Component
        }
    }
}