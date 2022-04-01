package com.gmkornilov.commentpage.view

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.domain.UserResultHandler
import com.gmkornilov.commentpage.brick_navigation.PostCommentArgument
import com.gmkornilov.commentpage.domain.CommentPageInteractor
import com.gmkornilov.letIf
import com.gmkornilov.post_comments.model.CommentPreviewData
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

internal class CommentPageViewModel @Inject constructor(
    private val navArgument: PostCommentArgument,
    private val commentPageInteractor: CommentPageInteractor,
    private val authInteractor: AuthInteractor,
    private val listener: CommentpageListener,
) : BaseViewModel<CommentPageState, Unit>(), CommentPageEvents {
    private fun getLikeUserHandler(comment: CommentPreviewData) = UserResultHandler {
        intent {
            val newLikeStatus = comment.likeStatus.toOppositeLikeStatus()

            val newComment = comment.copy(likeStatus = newLikeStatus)
            replaceComment(comment, newComment)

            viewModelScope.launch {
                try {
                    commentPageInteractor.setCommentLikeStatus(comment.id, newLikeStatus)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    private fun getDislikeUserHandler(comment: CommentPreviewData) = UserResultHandler {
        intent {
            val newLikeStatus = comment.likeStatus.toOppositeDislikeStatus()

            val newComment = comment.copy(likeStatus = newLikeStatus)
            replaceComment(comment, newComment)

            viewModelScope.launch {
                try {
                    commentPageInteractor.setCommentLikeStatus(comment.id, newLikeStatus)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    fun loadData(isRefresh: Boolean = false) = intent {
        viewModelScope.launch {
            if (isRefresh) {
                reduce { this.state.copy(isRefreshing = isRefresh) }
            } else {
                reduce { this.state.copy(listState = ListState.Loading) }
            }

            try {
                val posts = commentPageInteractor.loadComments(navArgument.postId)
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

    override fun getBaseState(): CommentPageState {
        return CommentPageState()
    }

    override fun likeComment(comment: CommentPreviewData) {
        val currentUser = authInteractor.getPostiumUser()
        val userResultHandler = getLikeUserHandler(comment)

        currentUser?.let {
            userResultHandler.handleResult(it)
        } ?: listener.startAuthorizationFlow(userResultHandler)
    }

    override fun dislikeComment(comment: CommentPreviewData) {
        val currentUser = authInteractor.getPostiumUser()
        val userResultHandler = getDislikeUserHandler(comment)

        currentUser?.let {
            userResultHandler.handleResult(it)
        } ?: listener.startAuthorizationFlow(userResultHandler)
    }

    override fun openProfile(comment: CommentPreviewData) {
        listener.openProfile(comment)
    }

    override fun reloadData() {
        loadData(true)
    }

    override fun sendComment(comment: String) = intent {
        reduce { this.state.copy(sendCommentState = SendCommentState.Loading) }
        viewModelScope.launch {
            try {
                val newComment = commentPageInteractor.sendComment(navArgument.postId, comment)
                val newListState =
                    this@intent.state.letIf(
                        { it.listState is ListState.Success && newComment != null },
                        {
                            require(it.listState is ListState.Success && newComment != null)
                            ListState.Success(it.listState.comments + listOf(newComment))
                        },
                        { it.listState }
                    )
                reduce { this.state.copy(sendCommentState = SendCommentState.None, listState = newListState) }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { this.state.copy(sendCommentState = SendCommentState.None) }
            }
        }
    }

    private fun replaceComment(
        oldComment: CommentPreviewData,
        newComment: CommentPreviewData
    ) = intent {
        reduce {
            val currentState = this.state.listState
            val newState = currentState.letIf(
                currentState is ListState.Success,
                { state ->
                    require(state is ListState.Success)
                    val newItems = state.comments.toMutableList().apply {
                        val index = this.indexOf(oldComment)
                        if (index != -1) {
                            this[index] = newComment
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

interface CommentpageListener {
    fun startAuthorizationFlow(userResultHandler: UserResultHandler)

    fun openProfile(comment: CommentPreviewData)
}