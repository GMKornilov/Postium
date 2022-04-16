package com.gmkornilov.comments.repostiory

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.comment_likes.repository.CommentLikesRepository
import com.gmkornilov.comments.model.CommentLikeStatus
import com.gmkornilov.comments.model.CommentPreviewData
import com.gmkornilov.comments.model.toCommentLikeStatus
import com.gmkornilov.post_comments.repository.PostCommentRepository
import com.gmkornilov.user.repository.UserRepository
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val commentLikesRepository: CommentLikesRepository,
    private val postCommentRepository: PostCommentRepository,
    private val userRepository: UserRepository,
    private val authInteractor: AuthInteractor,
) {
    suspend fun loadPostComments(postId: String): List<CommentPreviewData> {
        val currentUser = authInteractor.getPostiumUser()

        val comments = postCommentRepository.getPostComments(postId)

        val users = comments.map {
            userRepository.getByReference(it.user!!)
        }

        val commentLikeStatuses = currentUser?.let {
            commentLikesRepository.getLikesStatuses(
                it.getUid(),
                comments.map { comment -> comment.id })
        } ?: emptyMap()

        return comments.mapIndexed { index, comment ->
            val user = users[index]
            CommentPreviewData(
                id = comment.id,
                userId = comment.user!!.id,
                comment = comment.text,
                likes = comment.likes,
                dislikes = comment.dislikes,
                username = user.name,
                avatarUrl = user.avatarUrl?.ifBlank { null },
                likeStatus = commentLikeStatuses[comment.id].toCommentLikeStatus(),
            )
        }
    }

    suspend fun setCommentLikeStatus(
        postId: String,
        userId: String,
        commentId: String,
        commentLikeStatus: CommentLikeStatus
    ) {
        if (commentId.isBlank()) {
            return
        }
        val currentLikeStatus = commentLikesRepository.getLikeStatus(userId, commentId)
        when (commentLikeStatus) {
            CommentLikeStatus.LIKED -> commentLikesRepository.likeComment(
                userId,
                commentId
            )
            CommentLikeStatus.DISLIKED -> commentLikesRepository.dislikeComment(
                userId,
                commentId
            )
            CommentLikeStatus.NONE -> commentLikesRepository.removeStatus(
                userId,
                commentId
            )
        }

        when {
            currentLikeStatus.isLiked && !commentLikeStatus.isLiked -> postCommentRepository.decrementLikes(
                postId,
                commentId
            )
            !currentLikeStatus.isLiked && commentLikeStatus.isLiked -> postCommentRepository.incrementLikes(
                postId,
                commentId
            )
        }

        when {
            currentLikeStatus.isDisliked && !commentLikeStatus.isDisliked -> postCommentRepository.decrementDislikes(
                postId,
                commentId
            )
            !currentLikeStatus.isDisliked && commentLikeStatus.isDisliked -> postCommentRepository.incrementDislikes(
                postId,
                commentId
            )
        }
    }

    suspend fun sendComment(
        postId: String,
        userId: String,
        commentContent: String
    ): CommentPreviewData {
        val userReference = userRepository.getUserReference(userId)

        val comment = postCommentRepository.addComment(postId, userReference, commentContent)
        val user = userRepository.getByReference(userReference)

        return CommentPreviewData(
            id = comment.id,
            userId = userId,
            comment = commentContent,
            username = user.name,
            avatarUrl = user.avatarUrl?.ifBlank { null },
            likeStatus = CommentLikeStatus.NONE,
            likes = 0,
            dislikes = 0,
        )
    }
}