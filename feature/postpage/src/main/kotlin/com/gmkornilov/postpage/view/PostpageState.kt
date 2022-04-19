package com.gmkornilov.postpage.view

import com.gmkornilov.postpage.brick_navigation.PostPageArgument

internal data class PostpageState(
    val argument: PostPageArgument,
    val contentState: ContentState,
    val isRefresh: Boolean = false,
    val showEditContent: Boolean = false,
)

internal sealed class ContentState {
    object None: ContentState()

    object Loading: ContentState()

    data class Success(val content: String): ContentState()

    data class Error(val e: Exception): ContentState()
}