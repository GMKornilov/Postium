package com.gmkornilov.postpage.view

enum class PostpageBookmarkStatus(val isBookmarked: Boolean = false) {
    BOOKMARKED(isBookmarked = true),
    NOT_BOOKMARKED(isBookmarked = false);

    fun toOppositeStatus() = when (this) {
        BOOKMARKED -> NOT_BOOKMARKED
        NOT_BOOKMARKED -> BOOKMARKED
    }
}