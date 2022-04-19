package com.gmkornilov.postcreatepage.view

internal sealed class PostCreateSideEffect {
    data class RestoreDraft(val title: String, val contents: String): PostCreateSideEffect()

    data class ShowExitDialog(val title: String, val message: String): PostCreateSideEffect()

    object EmptyTitle: PostCreateSideEffect()

    object EmptyContent: PostCreateSideEffect()

    object Error: PostCreateSideEffect()
}