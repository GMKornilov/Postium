package com.gmkornilov.source

import com.gmkornilov.model.Post
import com.gmkornilov.model.TimeRange
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val POSTS_COLLECTION = "posts"

private const val DATE_FIELD = "date"
private const val LIKES_FIELD = "likes"
private const val DISLIKES_FIELD = "dislikes"
private const val USER_FIELD = "user"
private const val TITLE_FIELD = "title"
private const val CATEGORIES_FIELD = "categories"

private const val IS_DELETED = "is_deleted"

private const val LIMIT = 50L

class FirebasePostSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    suspend fun createPost(
        userReference: DocumentReference,
        title: String,
        categoryReferences: List<DocumentReference>
    ): Post {
        val newPost = mapOf(
            DATE_FIELD to FieldValue.serverTimestamp(),
            USER_FIELD to userReference,
            TITLE_FIELD to title,
            CATEGORIES_FIELD to categoryReferences,
            IS_DELETED to false,
        )

        val reference = firestore
            .collection(POSTS_COLLECTION)
            .add(newPost)
            .await()

        val snapshot = reference.get().await()
        return snapshot.toObject(Post::class.java)!!.copy(id = snapshot.id)
    }

    suspend fun updatePost(
        postId: String,
        title: String,
        categoryReferences: List<DocumentReference>?,
    ) {
        val newPost = mutableMapOf<String, Any>(TITLE_FIELD to title)
        if (categoryReferences != null) {
            newPost[CATEGORIES_FIELD] = categoryReferences
        }

        firestore
            .collection(POSTS_COLLECTION)
            .document(postId)
            .set(newPost, SetOptions.merge())
            .await()

    }

    suspend fun getPostById(id: String): Post {
        return getPostWithIds(listOf(id)).first()
    }

    suspend fun getPostsFromTimeRange(timeRange: TimeRange): List<Post> {
        val currentDate = Timestamp.now().toDate()
        val dateAfter = timeRange.getStartDate(currentDate)

        val timestamp = Timestamp(dateAfter)

        val snapshot = firestore
            .collection(POSTS_COLLECTION)
            .whereEqualTo(IS_DELETED, false)
            .whereGreaterThan(DATE_FIELD, timestamp)
            .orderBy(DATE_FIELD, Query.Direction.DESCENDING)
            .limit(LIMIT)
            .get()
            .await()

        return mapPosts(snapshot)
    }

    suspend fun getPostsWithUserReference(userReference: DocumentReference): List<Post> {
        val snapshot = firestore
            .collection(POSTS_COLLECTION)
            .whereEqualTo(IS_DELETED, false)
            .whereEqualTo(USER_FIELD, userReference)
            .orderBy(DATE_FIELD, Query.Direction.DESCENDING)
            .limit(LIMIT)
            .get()
            .await()
        return mapPosts(snapshot)
    }

    suspend fun getPostsWithCategory(categoryReference: DocumentReference): List<Post> {
        val snapshot = firestore
            .collection(POSTS_COLLECTION)
            .whereEqualTo(IS_DELETED, false)
            .whereArrayContains(CATEGORIES_FIELD, categoryReference)
            .get()
            .await()
        return mapPosts(snapshot)
    }

    suspend fun getPostWithIds(ids: List<String>): List<Post> {
        if (ids.isEmpty()) {
            return emptyList()
        }
        val snapshot = firestore
            .collection(POSTS_COLLECTION)
            .whereEqualTo(IS_DELETED, false)
            .whereIn(FieldPath.documentId(), ids)
            .get()
            .await()
        return mapPosts(snapshot)
    }

    suspend fun incrementLikes(postId: String) {
        val updateMap = mapOf(
            LIKES_FIELD to FieldValue.increment(1)
        )

        firestore
            .collection(POSTS_COLLECTION)
            .document(postId)
            .update(updateMap)
            .await()
    }

    suspend fun decrementLikes(postId: String) {
        val updateMap = mapOf(
            LIKES_FIELD to FieldValue.increment(-1)
        )

        firestore
            .collection(POSTS_COLLECTION)
            .document(postId)
            .update(updateMap)
            .await()
    }

    suspend fun incrementDislikes(postId: String) {
        val updateMap = mapOf(
            DISLIKES_FIELD to FieldValue.increment(1)
        )

        firestore
            .collection(POSTS_COLLECTION)
            .document(postId)
            .update(updateMap)
            .await()
    }

    suspend fun decrementDislikes(postId: String) {
        val updateMap = mapOf(
            DISLIKES_FIELD to FieldValue.increment(-1)
        )

        firestore
            .collection(POSTS_COLLECTION)
            .document(postId)
            .update(updateMap)
            .await()
    }

    suspend fun deletePost(postId: String) {
        val updateMap = mapOf(
            IS_DELETED to true
        )

        firestore
            .collection(POSTS_COLLECTION)
            .document(postId)
            .update(updateMap)
            .await()
    }

    private fun mapPosts(snapshot: QuerySnapshot): List<Post> {
        return snapshot.documents.map {
            it.toObject(Post::class.java)!!.copy(id = it.id)
        }
    }
}