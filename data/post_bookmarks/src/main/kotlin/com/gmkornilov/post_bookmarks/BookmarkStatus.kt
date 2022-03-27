package com.gmkornilov.post_bookmarks

enum class BookmarkStatus {
    BOOKMARKED,
    NOT_BOOKMARKED,
}

fun Boolean?.toBookmarkStatus() = when(this) {
    true -> BookmarkStatus.BOOKMARKED
    false -> BookmarkStatus.NOT_BOOKMARKED
    null -> BookmarkStatus.NOT_BOOKMARKED
}