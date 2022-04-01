package com.gmkornilov.comment_likes.repository

import com.gmkornilov.comment_likes.model.CommentLikeStatus
import com.gmkornilov.comment_likes.model.CommentLikes
import com.gmkornilov.comment_likes.model.toCommentLikeStatus
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val COMMENT_LIKE_COLLECTION = "comment_likes"

class CommentLikesRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    suspend fun likeComment(userId: String, commentId: String) {
        setValue(userId, commentId, true)
    }

    suspend fun dislikeComment(userId: String, commentId: String) {
        setValue(userId, commentId, false)
    }

    suspend fun getLikeStatus(userId: String, commentId: String): CommentLikeStatus {
        val postLikes = firestore
            .collection(COMMENT_LIKE_COLLECTION)
            .document(userId)
            .get()
            .await()
            .toObject(CommentLikes::class.java)

        return postLikes?.likes?.get(commentId).toCommentLikeStatus()
    }

    suspend fun getLikesStatuses(
        userId: String,
        commentIds: List<String>
    ): Map<String, CommentLikeStatus> {
        val postLikes = firestore
            .collection(COMMENT_LIKE_COLLECTION)
            .document(userId)
            .get()
            .await()
            .toObject(CommentLikes::class.java)
        return postLikes?.likes
            ?.filterKeys { commentIds.contains(it) }
            ?.mapValues { it.value.toCommentLikeStatus() }
            ?: emptyMap()
    }

    suspend fun removeStatus(userId: String, commentId: String) {
        val removeData = mapOf(
            "likes.$commentId" to FieldValue.delete()
        )
        firestore
            .collection(COMMENT_LIKE_COLLECTION)
            .document(userId)
            .update(removeData)
            .await()
    }

    private suspend fun setValue(userId: String, commentId: String, isLiked: Boolean) {
        firestore
            .collection(COMMENT_LIKE_COLLECTION)
            .document(userId)
            .set(mapOf("likes" to mapOf(commentId to isLiked)), SetOptions.merge())
            .await()
    }
}