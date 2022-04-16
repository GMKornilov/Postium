package com.gmkornilov.post_comments.repository

import com.gmkornilov.post_comments.model.Comment
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val POST_COMMENTS_COLLECTION = "post_comments"
private const val COMMENTS_SUBCOLLECTION = "comments"

private const val DATE_FIELD = "date"
private const val TEXT_FIELD = "text"
private const val USER_FIELD = "user"
private const val LIKES_FIELD = "likes"
private const val DISLIKES_FIELD = "dislikes"

class PostCommentRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    suspend fun getPostComments(postId: String): List<Comment> {
        val snapshot = firestore
            .collection(POST_COMMENTS_COLLECTION)
            .document(postId)
            .collection(COMMENTS_SUBCOLLECTION)
            .orderBy(DATE_FIELD)
            .get()
            .await()

        return mapPosts(snapshot)
    }

    suspend fun addComment(
        postId: String,
        userReference: DocumentReference,
        comment: String
    ): Comment {
        val createMap = mapOf(
            TEXT_FIELD to comment,
            DATE_FIELD to FieldValue.serverTimestamp(),
            USER_FIELD to userReference,
        )

        val createdReference = firestore
            .collection(POST_COMMENTS_COLLECTION)
            .document(postId)
            .collection(COMMENTS_SUBCOLLECTION)
            .add(createMap)
            .await()
        return createdReference.get().await().toObject(Comment::class.java)!!
            .copy(id = createdReference.id)
    }

    suspend fun incrementLikes(postId: String, commentId: String) {
        val updateMap = mapOf(
            LIKES_FIELD to FieldValue.increment(1)
        )

        firestore
            .collection(POST_COMMENTS_COLLECTION)
            .document(postId)
            .collection(COMMENTS_SUBCOLLECTION)
            .document(commentId)
            .update(updateMap)
            .await()
    }

    suspend fun decrementLikes(postId: String, commentId: String) {
        val updateMap = mapOf(
            LIKES_FIELD to FieldValue.increment(-1)
        )

        firestore
            .collection(POST_COMMENTS_COLLECTION)
            .document(postId)
            .collection(COMMENTS_SUBCOLLECTION)
            .document(commentId)
            .update(updateMap)
            .await()
    }

    suspend fun incrementDislikes(postId: String, commentId: String) {
        val updateMap = mapOf(
            DISLIKES_FIELD to FieldValue.increment(1)
        )

        firestore
            .collection(POST_COMMENTS_COLLECTION)
            .document(postId)
            .collection(COMMENTS_SUBCOLLECTION)
            .document(commentId)
            .update(updateMap)
            .await()
    }

    suspend fun decrementDislikes(postId: String, commentId: String) {
        val updateMap = mapOf(
            DISLIKES_FIELD to FieldValue.increment(-1)
        )

        firestore
            .collection(POST_COMMENTS_COLLECTION)
            .document(postId)
            .collection(COMMENTS_SUBCOLLECTION)
            .document(commentId)
            .update(updateMap)
            .await()
    }

    private fun mapPosts(snapshot: QuerySnapshot): List<Comment> {
        return snapshot.documents.map {
            it.toObject(Comment::class.java)!!.copy(id = it.id)
        }
    }
}