package com.gmkornilov.mainpage.model

import com.gmkornilov.post_bookmarks.BookmarkStatus
import com.gmkornilov.postpage.view.PostpageBookmarkStatus

enum class PostPreviewBookmarkStatus(val isBookmarked: Boolean = false) {
    BOOKMARKED(isBookmarked = true),
    NOT_BOOKMARKED(isBookmarked = false);

    fun toOppositeStatus() = when (this) {
        BOOKMARKED -> NOT_BOOKMARKED
        NOT_BOOKMARKED -> BOOKMARKED
    }
}

fun BookmarkStatus?.toPostPreviewBookmarkStatus() = when(this) {
    BookmarkStatus.BOOKMARKED -> PostPreviewBookmarkStatus.BOOKMARKED
    BookmarkStatus.NOT_BOOKMARKED -> PostPreviewBookmarkStatus.NOT_BOOKMARKED
    null -> PostPreviewBookmarkStatus.NOT_BOOKMARKED
}

fun PostPreviewBookmarkStatus.toPostPageBookmarkStatus() = when (this) {
    PostPreviewBookmarkStatus.BOOKMARKED -> PostpageBookmarkStatus.BOOKMARKED
    PostPreviewBookmarkStatus.NOT_BOOKMARKED -> PostpageBookmarkStatus.NOT_BOOKMARKED
}