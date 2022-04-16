package com.gmkornilov.post_list.view

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.domain.UserResultHandler
import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.letIf
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.post.model.toOppositeStatus
import com.gmkornilov.post_list.domain.PostListInteractor
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

class PostListViewModel @Inject constructor(
    private val postListInteractor: PostListInteractor,
    private val listener: PostsListListener,
    private val authInteractor: AuthInteractor,
) : BaseViewModel<PostListState, Unit>(), PostListEvents {
    override fun getBaseState(): PostListState {
        return PostListState()
    }

    private fun getLikeUserHandler(post: PostPreviewData) = UserResultHandler {
        intent {
            val newLikeStatus = post.likeStatus.toOppositeLikeStatus()
            val newLikes = if (newLikeStatus.isLiked) post.likes + 1 else post.likes - 1
            val newDislikes = if (post.likeStatus.isDisliked) post.dislikes - 1 else post.dislikes
            val newPost =
                post.copy(likeStatus = newLikeStatus, likes = newLikes, dislikes = newDislikes)
            replacePost(post, newPost)

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    postListInteractor.setLikeStatus(post.id, newLikeStatus)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    private fun getDislikeUserHandler(post: PostPreviewData) = UserResultHandler {
        intent {
            val newLikeStatus = post.likeStatus.toOppositeDislikeStatus()
            val newLikes = if (post.likeStatus.isLiked) post.likes - 1 else post.likes
            val newDislikes = if (newLikeStatus.isDisliked) post.dislikes + 1 else post.dislikes - 1
            val newPost =
                post.copy(likeStatus = newLikeStatus, likes = newLikes, dislikes = newDislikes)
            replacePost(post, newPost)

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    postListInteractor.setLikeStatus(post.id, newLikeStatus)
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
                postListInteractor.setBookmarkStatus(post.id, newBookmarkStatus)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun getAddToPlaylistsUserHandler(post: PostPreviewData) = UserResultHandler {
        listener.addToPlaylist(post)
    }

    fun loadAllPosts(isRefresh: Boolean = false) = intent {
        viewModelScope.launch(Dispatchers.IO) {
            if (isRefresh) {
                reduce { this.state.copy(isRefreshing = true) }
            } else {
                reduce { this.state.copy(listState = ListState.Loading) }
            }
            try {
                val posts = postListInteractor.loadPosts()
                reduce {
                    this.state.copy(
                        listState = ListState.Success(posts),
                        isRefreshing = false
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { this.state.copy(listState = ListState.Error(e), isRefreshing = false) }
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

    override fun addToPlaylists(post: PostPreviewData) {
        val addToPLaylistsUserHandler = getAddToPlaylistsUserHandler(post)
        val user = authInteractor.getPostiumUser()
        user?.let {
            addToPLaylistsUserHandler.handleResult(user)
        } ?: listener.startAuthorizationFlow(addToPLaylistsUserHandler)
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

interface PostsListListener {
    fun openPost(postPreviewData: PostPreviewData)

    fun startAuthorizationFlow(userResultHandler: UserResultHandler)

    fun openUserProfile(postPreviewData: PostPreviewData)

    fun addToPlaylist(post: PostPreviewData)
}