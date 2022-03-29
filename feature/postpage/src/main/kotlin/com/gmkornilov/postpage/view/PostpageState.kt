package com.gmkornilov.postpage.view

import com.gmkornilov.postpage.brick_navigation.PostPageArgument

internal data class PostpageState(
    val argument: PostPageArgument,
    val contentState: ContentState
)

internal sealed class ContentState {
    object None: ContentState()

    object Loading: ContentState()

    data class Success(val content: String): ContentState()

    data class Error(val e: Exception): ContentState()
}