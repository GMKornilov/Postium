package com.gmkornilov.playlists.repository

import com.gmkornilov.playlists.model.Playlist
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val USER_PLAYLISTS_COLLECTION = "user_playlists"
private const val PLAYLISTS_SUBCOLLECTION = "playlists"

private const val POSTS_FIELD = "posts"
private const val NAME_FIELD = "name"

class PlaylistRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    suspend fun createPlaylist(userId: String, playlistName: String): Playlist {
        val createMap = mapOf(
            NAME_FIELD to playlistName
        )
        val ref = firestore
            .collection(USER_PLAYLISTS_COLLECTION)
            .document(userId)
            .collection(PLAYLISTS_SUBCOLLECTION)
            .add(createMap)
            .await()
        return ref.get().await().toObject(Playlist::class.java)!!.copy(id = ref.id)
    }

    suspend fun getUserPlaylists(userId: String): List<Playlist> {
        val snapshot = firestore
            .collection(USER_PLAYLISTS_COLLECTION)
            .document(userId)
            .collection(PLAYLISTS_SUBCOLLECTION)
            .get()
            .await()

        return mapSnapshot(snapshot)
    }

    suspend fun addPostToPlaylist(userId: String, postId: String, playlistId: String) {
        val addMap = mapOf(
            POSTS_FIELD to FieldValue.arrayUnion(postId)
        )

        firestore
            .collection(USER_PLAYLISTS_COLLECTION)
            .document(userId)
            .collection(PLAYLISTS_SUBCOLLECTION)
            .document(playlistId)
            .update(addMap)
    }

    suspend fun removePostFromPlaylist(userId: String, postId: String, playlistId: String) {
        val removeMap = mapOf(
            POSTS_FIELD to FieldValue.arrayRemove(postId)
        )

        firestore
            .collection(USER_PLAYLISTS_COLLECTION)
            .document(userId)
            .collection(PLAYLISTS_SUBCOLLECTION)
            .document(playlistId)
            .update(removeMap)
    }

    private fun mapSnapshot(snapshot: QuerySnapshot): List<Playlist> {
        return snapshot.documents.map {
            it.toObject(Playlist::class.java)!!.copy(id = it.id)
        }
    }
}