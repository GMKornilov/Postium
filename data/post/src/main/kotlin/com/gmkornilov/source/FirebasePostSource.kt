package com.gmkornilov.source

import com.gmkornilov.model.Post
import com.gmkornilov.model.TimeRange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

private const val POSTS_COLLECTION = "posts"

private const val DATE_FIELD = "date"
private const val LIKE_FIELD = "likes"

private const val LIMIT = 50L

class FirebasePostSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    suspend fun getPostsFromTimeRange(timeRange: TimeRange): List<Post> {
        val currentDate = Calendar.getInstance().time
        val dateAfter = timeRange.getStartDate(currentDate)

        val snapshot = firestore
            .collection(POSTS_COLLECTION)
            .whereGreaterThan(DATE_FIELD, dateAfter)
            .orderBy(DATE_FIELD)
            .orderBy(LIKE_FIELD)
            .limit(LIMIT)
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