package com.gmkornilov.source

import com.gmkornilov.model.Post
import com.gmkornilov.model.TimeRange
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

private const val POSTS_COLLECTION = "posts"

private const val DATE_FIELD = "date"
private const val LIKE_FIELD = "likes"
private const val USER_FIELD = "user"
private const val TITLE_FIELD = "title"
private const val CATEGORIES_FIELD = "categories"

private const val LIMIT = 50L

class FirebasePostSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    suspend fun createPost(userReference: DocumentReference, title: String): Post {
        val newPost = mapOf(
            DATE_FIELD to FieldValue.serverTimestamp(),
            USER_FIELD to userReference,
            TITLE_FIELD to title,
        )

        val reference = firestore
            .collection(POSTS_COLLECTION)
            .add(newPost)
            .await()

        val snapshot = reference.get().await()
        return snapshot.toObject(Post::class.java)!!.copy(id = snapshot.id)
    }

    suspend fun getPostsFromTimeRange(timeRange: TimeRange): List<Post> {
        val currentDate = Timestamp.now().toDate()
        val dateAfter = timeRange.getStartDate(currentDate)

        val timestamp = Timestamp(dateAfter)

        val snapshot = firestore
            .collection(POSTS_COLLECTION)
            .whereGreaterThan(DATE_FIELD, timestamp)
            .orderBy(DATE_FIELD)
            .limit(LIMIT)
            .get()
            .await()

        return mapPosts(snapshot)
    }

    suspend fun getPostsWithUserReference(userReference: DocumentReference): List<Post> {
        val snapshot = firestore
            .collection(POSTS_COLLECTION)
            .whereEqualTo(USER_FIELD, userReference)
            .orderBy(DATE_FIELD)
            .limit(LIMIT)
            .get()
            .await()
        return mapPosts(snapshot)
    }

    suspend fun getPostsWithCategory(categoryReference: DocumentReference): List<Post> {
        val snapshot = firestore
            .collection(POSTS_COLLECTION)
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
            .whereIn(FieldPath.documentId(), ids)
            .get()
            .await()
        return mapPosts(snapshot)
    }

    private fun mapPosts(snapshot: QuerySnapshot): List<Post> {
        return snapshot.documents.map {
            it.toObject(Post::class.java)!!.copy(id = it.id)
        }
    }
}