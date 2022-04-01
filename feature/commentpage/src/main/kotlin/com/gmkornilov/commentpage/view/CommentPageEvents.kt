package com.gmkornilov.commentpage.view

import com.gmkornilov.post_comments.model.CommentPreviewData

interface CommentPageEvents {
    fun likeComment(comment: CommentPreviewData)

    fun dislikeComment(comment: CommentPreviewData)

    fun openProfile(comment: CommentPreviewData)

    fun reloadData()

    fun sendComment(comment: String)

    companion object : CommentPageEvents {
        override fun likeComment(comment: CommentPreviewData) = Unit
        override fun dislikeComment(comment: CommentPreviewData)  = Unit
        override fun openProfile(comment: CommentPreviewData) = Unit
        override fun reloadData() = Unit
        override fun sendComment(comment: String) = Unit
    }
}