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

internal enum class Tab(
    @StringRes val headerRes: Int,
    @StringRes val errorRes: Int,
    @StringRes val emptyRes: Int,
) {
    POSTS(R.string.posts, R.string.posts_error, R.string.posts_empty),
    BOOKMARKS(R.string.bookmarks, R.string.bookmarks_error, R.string.bookmarks_empty),
    // TODO: add collections
}