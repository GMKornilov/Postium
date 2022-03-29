package com.gmkornilov.postpage.view

import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.authorization.feature_flow.AuthorizationFlowScreenFactory
import com.gmkornilov.postpage.brick_navigation.PostPageArgument
import com.gmkornilov.postpage.domain.PostPageInteractor
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

internal class PostpageViewModel @Inject constructor(
    private val postPageArgument: PostPageArgument,
    private val router: TreeRouter,
    private val postPageInteractor: PostPageInteractor,
    private val authorizationFlowScreenFactory: AuthorizationFlowScreenFactory,
    private val authInteractor: AuthInteractor,
) : BaseViewModel<PostpageState, Unit>(), PostpageEvents {
    override fun getBaseState(): PostpageState {
        return PostpageState(postPageArgument, ContentState.None)
    }

    private val likeUserHandler = UserResultHandler {
        intent {
            val argument = this.state.argument
            val newState =
                this.state.copy(argument = argument.copy(likeStatus = argument.likeStatus.toOppositeLikeStatus()))
            reduce { newState }

            viewModelScope.launch {
                try {
                    if (newState.argument.likeStatus == PostpageLikeStatus.LIKED) {
                        postPageInteractor.likePost(postPageArgument.id)
                    } else {
                        postPageInteractor.removeLikeStatus(postPageArgument.id)
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    private val dislikeUserHandler = UserResultHandler {
        intent {
            val argument = this.state.argument
            val newState =
                this.state.copy(argument = argument.copy(likeStatus = argument.likeStatus.toOppositeDislikeStatus()))
            reduce { newState }

            viewModelScope.launch {
                try {
                    if (newState.argument.likeStatus == PostpageLikeStatus.DISLIKED) {
                        postPageInteractor.dislikePost(postPageArgument.id)
                    } else {
                        postPageInteractor.removeLikeStatus(postPageArgument.id)
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    private val bookmarkUserHandler = UserResultHandler {
        intent {
            val argument = this.state.argument
            val newState =
                this.state.copy(argument = argument.copy(bookmarkStatus = argument.bookmarkStatus.toOppositeStatus()))
            reduce { newState }

            viewModelScope.launch {
                try {
                    if (newState.argument.bookmarkStatus == PostpageBookmarkStatus.BOOKMARKED) {
                        postPageInteractor.addBookmark(postPageArgument.id)
                    } else {
                        postPageInteractor.removeBookmark(postPageArgument.id)
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    fun loadContent() = intent {
        reduce { this.state.copy(contentState = ContentState.Loading) }

        viewModelScope.launch {
            try {
                val content = postPageInteractor.loadContent(postPageArgument.id)
                reduce { this.state.copy(contentState = ContentState.Success(content)) }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { this.state.copy(contentState = ContentState.Error(e)) }
            }
        }
    }

    override fun openComments() {
    }

    override fun likePost() {
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            likeUserHandler.handleResult(currentUser)
        } ?: authorizationFlowScreenFactory.start(likeUserHandler, router)
    }

    override fun dislikePost() {
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            dislikeUserHandler.handleResult(currentUser)
        } ?: authorizationFlowScreenFactory.start(dislikeUserHandler, router)
    }

    override fun bookmarkPost() {
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            bookmarkUserHandler.handleResult(currentUser)
        } ?: authorizationFlowScreenFactory.start(bookmarkUserHandler, router)
    }
}