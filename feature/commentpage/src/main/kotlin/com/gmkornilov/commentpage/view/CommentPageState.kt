package com.gmkornilov.commentpage.view

import com.gmkornilov.post_comments.model.CommentPreviewData

data class CommentPageState(
    val listState: ListState = ListState.Loading,
    val isRefreshing: Boolean = false,
    val sendCommentState: SendCommentState = SendCommentState.None,
)

sealed class ListState {
    object Loading: ListState()

    data class Success(val comments: List<CommentPreviewData>): ListState()

    data class Error(val e: Exception): ListState()
}

sealed class SendCommentState {
    object None: SendCommentState()

    object Loading: SendCommentState()
}