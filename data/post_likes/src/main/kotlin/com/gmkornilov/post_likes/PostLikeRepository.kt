package com.gmkornilov.post_likes

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostLikeRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    suspend fun likePost(userId: String, postId: String) {
        setValue(userId, postId, true)
    }

    suspend fun dislikePost(userId: String, postId: String) {
        setValue(userId, postId, false)
    }

    suspend fun getLikeStatus(userId: String, postId: String): PostLikeStatus {
        val postLikes = firestore
            .collection("post_likes")
            .document(userId)
            .get()
            .await()
            .toObject(PostLikes::class.java)

        return postLikes?.likes?.get(postId).toPostLikeStatus()
    }

    suspend fun getLikesStatuses(
        userId: String,
        postIds: List<String>
    ): Map<String, PostLikeStatus> {
        val postLikes = firestore
            .collection("post_likes")
            .document(userId)
            .get()
            .await()
            .toObject(PostLikes::class.java)
        return postLikes?.likes
            ?.filterKeys { postIds.contains(it) }
            ?.mapValues { it.value.toPostLikeStatus() }
            ?: emptyMap()
    }

    private suspend fun setValue(userId: String, postId: String, isLiked: Boolean) {
        firestore
            .collection("post_likes")
            .document(userId)
            .set(mapOf("likes" to mapOf(postId to isLiked)), SetOptions.merge())
            .await()
    }
}