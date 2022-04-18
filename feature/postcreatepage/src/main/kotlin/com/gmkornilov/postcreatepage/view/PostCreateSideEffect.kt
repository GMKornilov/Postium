package com.gmkornilov.postcreatepage.view

internal sealed class PostCreateSideEffect {
    data class RestoreDraft(val title: String, val contents: String): PostCreateSideEffect()

    object ShowExitDialog: PostCreateSideEffect()

    object EmptyTitle: PostCreateSideEffect()

    object EmptyContent: PostCreateSideEffect()

    object Error: PostCreateSideEffect()
}