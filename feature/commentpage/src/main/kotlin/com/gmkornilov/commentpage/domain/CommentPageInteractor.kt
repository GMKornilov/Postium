package com.gmkornilov.commentpage.domain

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.post_comments.model.CommentLikeStatus
import com.gmkornilov.post_comments.model.CommentPreviewData
import com.gmkornilov.post_comments.repostiory.CommentRepository
import javax.inject.Inject

class CommentPageInteractor @Inject constructor(
    private val commentRepository: CommentRepository,
    private val authInteractor: AuthInteractor,
) {
    suspend fun setCommentLikeStatus(commentId: String, likeStatus: CommentLikeStatus) {
        val currentUser = authInteractor.getPostiumUser() ?: return
        commentRepository.setCommentLikeStatus(currentUser.getUid(), commentId, likeStatus)
    }

    suspend fun loadComments(postId: String): List<CommentPreviewData> {
        return commentRepository.loadPostComments(postId)
    }

    suspend fun sendComment(postId: String, comment: String): CommentPreviewData? {
        if (comment.isBlank()) {
            return null
        }
        val currentUser = authInteractor.getPostiumUser() ?: return null
        return commentRepository.sendComment(postId, currentUser.getUid(), comment)
    }
}