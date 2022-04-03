package com.gmkornilov.user_playlists.playlist_posts.view

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.domain.UserResultHandler
import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.letIf
import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.post.model.toOppositeStatus
import com.gmkornilov.user_playlists.playlist_posts.domain.PlaylistPostsInteractor
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

internal class PlaylistPostsViewModel @Inject constructor(
    private val playlist: Playlist,
    private val playlistPostsInteractor: PlaylistPostsInteractor,
    private val authInteractor: AuthInteractor,
    private val listener: PlaylistPostsListener,
): BaseViewModel<PlaylistPostsState, Unit>(), PlaylistPostsEvents {
    override fun getBaseState(): PlaylistPostsState {
        return PlaylistPostsState(playlist.name)
    }

    private fun getLikeUserHandler(post: PostPreviewData) = UserResultHandler {
        intent {
            val newLikeStatus = post.likeStatus.toOppositeLikeStatus()

            val newPost = post.copy(likeStatus = newLikeStatus)
            replacePost(post, newPost)

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    playlistPostsInteractor.setLikeStatus(post.id, newLikeStatus)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    private fun getDislikeUserHandler(post: PostPreviewData) = UserResultHandler {
        intent {
            val newLikeStatus = post.likeStatus.toOppositeDislikeStatus()

            val newPost = post.copy(likeStatus = newLikeStatus)
            replacePost(post, newPost)

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    playlistPostsInteractor.setLikeStatus(post.id, newLikeStatus)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    private fun getBookmarkUserHandler(post: PostPreviewData) = UserResultHandler {
        val newBookmarkStatus = post.bookmarkStatus.toOppositeStatus()
        val newPost = post.copy(bookmarkStatus = newBookmarkStatus)
        replacePost(post, newPost)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                playlistPostsInteractor.setBookmarkStatus(post.id, newBookmarkStatus)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun loadAllPosts(isRefresh: Boolean = false) = intent {
        viewModelScope.launch(Dispatchers.IO) {
            if (isRefresh) {
                reduce { this.state.copy(isRefreshing = true) }
            } else {
                reduce { this.state.copy(listState = ListState.Loading) }
            }
            try {
                val posts = playlistPostsInteractor.loadPosts(playlist)
                reduce { this.state.copy(listState = ListState.Success(posts)) }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { this.state.copy(listState = ListState.Error(e)) }
            }
        }
    }

    override fun refreshData() {
        loadAllPosts(true)
    }

    override fun openPost(post: PostPreviewData) {
        listener.openPost(post)
    }

    override fun openProfile(post: PostPreviewData) {
        listener.openUserProfile(post)
    }

    override fun likePost(post: PostPreviewData) {
        val likeUserHandler = getLikeUserHandler(post)
        val user = authInteractor.getPostiumUser()
        user?.let {
            likeUserHandler.handleResult(user)
        } ?: listener.startAuthorizationFlow(likeUserHandler)
    }

    override fun dislikePost(post: PostPreviewData) {
        val dislikeUserHandler = getDislikeUserHandler(post)
        val user = authInteractor.getPostiumUser()
        user?.let {
            dislikeUserHandler.handleResult(user)
        } ?: listener.startAuthorizationFlow(dislikeUserHandler)
    }

    override fun bookmarkPost(post: PostPreviewData) {
        val bookmarkUserHandler = getBookmarkUserHandler(post)
        val user = authInteractor.getPostiumUser()
        user?.let {
            bookmarkUserHandler.handleResult(user)
        } ?: listener.startAuthorizationFlow(bookmarkUserHandler)
    }

    private fun replacePost(oldPost: PostPreviewData, newPost: PostPreviewData) = intent {
        reduce {
            val currentState = this.state.listState
            val newState = currentState.letIf(
                currentState is ListState.Success,
                { state ->
                    val success = state as ListState.Success
                    val newItems = success.contents.toMutableList().apply {
                        val index = this.indexOf(oldPost)
                        if (index != -1) {
                            this[index] = newPost
                        }
                    }
                    ListState.Success(newItems)
                },
                { it }
            )
            this.state.copy(listState = newState)
        }
    }
}

interface PlaylistPostsListener {
    fun startAuthorizationFlow(userResultHandler: UserResultHandler)

    fun openPost(post: PostPreviewData)

    fun openUserProfile(post: PostPreviewData)
}