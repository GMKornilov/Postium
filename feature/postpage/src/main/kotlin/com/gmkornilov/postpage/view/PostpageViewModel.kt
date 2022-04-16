package com.gmkornilov.postpage.view

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.domain.UserResultHandler
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.post.model.toOppositeStatus
import com.gmkornilov.postpage.brick_navigation.PostPageArgument
import com.gmkornilov.postpage.brick_navigation.toPostPreviewData
import com.gmkornilov.postpage.domain.PostPageInteractor
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

internal class PostpageViewModel @Inject constructor(
    private val postPageArgument: PostPageArgument,
    private val postPageInteractor: PostPageInteractor,
    private val authInteractor: AuthInteractor,
    private val listener: PostpageListener,
) : BaseViewModel<PostpageState, Unit>(), PostpageEvents {
    override fun getBaseState(): PostpageState {
        return PostpageState(postPageArgument, ContentState.None)
    }

    private val likeUserHandler = UserResultHandler {
        intent {
            val argument = this.state.argument
            val newLikeStatus = argument.likeStatus.toOppositeLikeStatus()
            val newLikes =
                if (newLikeStatus.isLiked) argument.likes + 1 else argument.likes - 1
            val newDislikes =
                if (argument.likeStatus.isDisliked) argument.dislikes - 1 else argument.dislikes
            val newState = this.state.copy(
                argument = argument.copy(
                    likeStatus = newLikeStatus,
                    likes = newLikes,
                    dislikes = newDislikes,
                )
            )
            reduce { newState }

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    postPageInteractor.setLikeStatus(postPageArgument.id, newLikeStatus)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    private val dislikeUserHandler = UserResultHandler {
        intent {
            val argument = this.state.argument
            val newLikeStatus = argument.likeStatus.toOppositeDislikeStatus()
            val newLikes = if (argument.likeStatus.isLiked) argument.likes - 1 else argument.likes
            val newDislikes =
                if (newLikeStatus.isDisliked) argument.dislikes + 1 else argument.dislikes - 1
            val newState = this.state.copy(
                argument = argument.copy(
                    likeStatus = newLikeStatus,
                    likes = newLikes,
                    dislikes = newDislikes
                )
            )
            reduce { newState }

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    postPageInteractor.setLikeStatus(postPageArgument.id, newLikeStatus)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    private val bookmarkUserHandler = UserResultHandler {
        intent {
            val argument = this.state.argument
            val newBookmarkStatus = argument.bookmarkStatus.toOppositeStatus()
            val newState =
                this.state.copy(argument = argument.copy(bookmarkStatus = newBookmarkStatus))
            reduce { newState }

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    postPageInteractor.setBookmarkStatus(postPageArgument.id, newBookmarkStatus)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    fun loadContent(isRefresh: Boolean = false) = intent {
        if (isRefresh) {
            reduce { this.state.copy(isRefresh = true) }
        } else {
            reduce { this.state.copy(contentState = ContentState.Loading) }
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val content = postPageInteractor.loadContent(postPageArgument.id)
                reduce {
                    this.state.copy(
                        contentState = ContentState.Success(content),
                        isRefresh = false,
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { this.state.copy(contentState = ContentState.Error(e), isRefresh = false) }
            }
        }
    }

    override fun openComments() {
        listener.openComments(postPageArgument.id)
    }

    override fun openProfile() {
        listener.openUserProfile(postPageArgument.toPostPreviewData())
    }

    override fun likePost() {
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            likeUserHandler.handleResult(currentUser)
        } ?: listener.startAuthorizationFlow(likeUserHandler)
    }

    override fun dislikePost() {
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            dislikeUserHandler.handleResult(currentUser)
        } ?: listener.startAuthorizationFlow(dislikeUserHandler)
    }

    override fun bookmarkPost() {
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            bookmarkUserHandler.handleResult(currentUser)
        } ?: listener.startAuthorizationFlow(bookmarkUserHandler)
    }

    override fun refreshData() {
        loadContent(true)
    }
}

interface PostpageListener {
    fun startAuthorizationFlow(userResultHandler: UserResultHandler)

    fun openUserProfile(postPreviewData: PostPreviewData)

    fun openComments(postId: String)
}