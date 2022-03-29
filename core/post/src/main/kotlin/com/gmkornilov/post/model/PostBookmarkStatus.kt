package com.gmkornilov.post.model

import com.gmkornilov.post_bookmarks.BookmarkStatus as DataBookmarkStatus

enum class PostBookmarkStatus(val isBookmarked: Boolean) {
    BOOKMARKED(true),
    NOT_BOOKMARKED(false),
}

fun PostBookmarkStatus.toOppositeStatus() = when (this) {
    PostBookmarkStatus.BOOKMARKED -> PostBookmarkStatus.NOT_BOOKMARKED
    PostBookmarkStatus.NOT_BOOKMARKED -> PostBookmarkStatus.BOOKMARKED
}

fun DataBookmarkStatus?.toPostBookmarkStatus() = when (this) {
    DataBookmarkStatus.BOOKMARKED -> PostBookmarkStatus.BOOKMARKED
    DataBookmarkStatus.NOT_BOOKMARKED -> PostBookmarkStatus.NOT_BOOKMARKED
    null -> PostBookmarkStatus.NOT_BOOKMARKED
}

fun PostBookmarkStatus.toDataStatus() = when (this) {
    PostBookmarkStatus.BOOKMARKED -> DataBookmarkStatus.BOOKMARKED
    PostBookmarkStatus.NOT_BOOKMARKED -> DataBookmarkStatus.NOT_BOOKMARKED
}