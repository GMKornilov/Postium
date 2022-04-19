package com.gmkornilov.postpage.view

internal sealed class PostpageSideEffect {
    object ShowDeleteConfirmDialog: PostpageSideEffect()
}