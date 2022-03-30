package com.gmkornilov.postcreatepage.view

internal sealed class PostCreateSideEffect {
    object ShowExitDialog: PostCreateSideEffect()

    object EmptyTitle: PostCreateSideEffect()

    object EmptyContent: PostCreateSideEffect()

    object Error: PostCreateSideEffect()
}