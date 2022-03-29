package com.gmkornilov.userpage.view

import androidx.annotation.StringRes
import com.gmkornilov.model.Post
import com.gmkornilov.userpage.R

internal data class UserPageState(
    val headerState: HeaderState = HeaderState(),
    val tabStates: Map<Tab, TabState> = Tab.values().map { it to TabState.None }.toMap(),
)

data class HeaderState(
    val username: String? = null,
    val avatarUrl: String? = null,
)

sealed class TabState {
    object None : TabState()

    object Loading: TabState()

    data class Success(val posts: List<Post>): TabState()
}

internal enum class Tab(@StringRes val headerRes: Int) {
    POSTS(R.string.posts),
    BOOKMARKS(R.string.bookmarks),
    // TODO: add
}