package com.gmkornilov.commentpage.domain

import com.gmkornilov.commentpage.R
import com.gmkornilov.strings.StringsProvider
import javax.inject.Inject

class CommentPageStringProvider @Inject constructor(
    private val stringsProvider: StringsProvider,
) {
    fun getSendCommentError(): String {
        return stringsProvider.getStringById(R.string.comment_send_error)
    }
}