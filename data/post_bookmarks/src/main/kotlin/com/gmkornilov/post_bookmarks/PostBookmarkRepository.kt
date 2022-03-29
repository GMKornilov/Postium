package com.gmkornilov.post_bookmarks

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val POST_BOOKMARK_COLLECTION = "post_bookmarks"
private const val BOOKMARKS_FIELD = "bookmarks"

class PostBookmarkRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    suspend fun getBookmarkStatus(userId: String, postId: String): BookmarkStatus {
        val postBookmarks = firestore
            .collection(POST_BOOKMARK_COLLECTION)
            .document(userId)
            .get()
            .await()
            .toObject(PostBookmarks::class.java)
        return postBookmarks?.bookmarks?.contains(postId).toBookmarkStatus()
    }

    suspend fun getBookmarkStatuses(
        userId: String,
        postIds: List<String>
    ): Map<String, BookmarkStatus> {
        val postBookmarks = firestore
            .collection(POST_BOOKMARK_COLLECTION)
            .document(userId)
            .get()
            .await()
            .toObject(PostBookmarks::class.java)

        return postBookmarks?.bookmarks?.let {
            postIds.map { postId -> postId to it.contains(postId).toBookmarkStatus() }.toMap()
        } ?: emptyMap()
    }

    suspend fun addBookmark(userId: String, postId: String) {
        val addMap = mapOf(
            BOOKMARKS_FIELD to FieldValue.arrayUnion(postId)
        )

        firestore
            .collection(POST_BOOKMARK_COLLECTION)
            .document(userId)
            .set(addMap, SetOptions.merge())
            .await()
    }

    suspend fun getUserBookmarks(userId: String): List<String> {
        val bookmarks = firestore
            .collection(POST_BOOKMARK_COLLECTION)
            .document(userId)
            .get()
            .await()
            .toObject(PostBookmarks::class.java)
        return bookmarks?.bookmarks.orEmpty()
    }

    suspend fun removeBookmark(userId: String, postId: String) {
        val removeMap = mapOf(
            BOOKMARKS_FIELD to FieldValue.arrayRemove(postId)
        )

        firestore
            .collection(POST_BOOKMARK_COLLECTION)
            .document(userId)
            .set(removeMap, SetOptions.merge())
            .await()
    }
}