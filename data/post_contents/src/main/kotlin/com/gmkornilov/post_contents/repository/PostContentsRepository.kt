package com.gmkornilov.post_contents.repository

import com.gmkornilov.post_contents.model.PostContents
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val POST_CONTENTS_COLLECTION = "post_contents"

class PostContentsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    suspend fun loadPostContents(postId: String): PostContents {
        val snapshot = firestore
            .collection(POST_CONTENTS_COLLECTION)
            .document(postId)
            .get()
            .await()
        return mapSnaphot(snapshot)
    }

    private fun mapSnaphot(snapshot: DocumentSnapshot): PostContents {
        return snapshot.toObject(PostContents::class.java)!!
    }
}