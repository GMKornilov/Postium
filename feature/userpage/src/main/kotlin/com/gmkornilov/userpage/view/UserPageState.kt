package com.gmkornilov.userpage.view

import androidx.annotation.StringRes
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.userpage.R

internal data class UserPageState(
    val headerState: HeaderState = HeaderState(),
    val tabStates: Map<Tab, TabState> = Tab.values().map { it to TabState.None }.toMap(),
)

data class HeaderState(
    val needLoading: Boolean = false,
    val username: String = "",
    val avatarUrl: String? = null,
)

sealed class TabState {
    object None : TabState()

    object Loading: TabState()

    data class Error(val e: Exception): TabState()

    data class Success(val posts: List<PostPreviewData>): TabState()
}

internal enum class Tab(@StringRes val headerRes: Int) {
    POSTS(R.string.posts),
    BOOKMARKS(R.string.bookmarks),
    // TODO: add collections
}