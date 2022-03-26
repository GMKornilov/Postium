package com.gmkornilov.source

import com.gmkornilov.post.Post
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebasePostSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    suspend fun getAllPosts(): List<Post> {
        val snapshot = firestore.collection("posts").get().await()
        return snapshot.documents.map { it.toObject(Post::class.java)!!.copy() }
    }
}