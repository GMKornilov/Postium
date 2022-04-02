package com.gmkornilov.commentpage.view

sealed class CommentPageSideEffect {
    data class ShowSnackbar(val message: String): CommentPageSideEffect()

    object ClearTextField: CommentPageSideEffect()
}
