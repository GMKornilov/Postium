package com.gmkornilov.user.repository

import com.gmkornilov.user.model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val USERS_COLLECTION = "users"

private const val AVATAR_FIELD = "avatar_url"
private const val USERNAME_FIELD = "name"

class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    fun getUserReference(userId: String): DocumentReference {
        return firestore.collection(USERS_COLLECTION).document(userId)
    }

    suspend fun getByReference(userReference: DocumentReference): User {
        return userReference.get().await().toObject(User::class.java)!!
    }

    suspend fun getUser(userId: String): User {
        val snapshot =  firestore.collection(USERS_COLLECTION).document(userId).get().await()
        return snapshot.toObject(User::class.java)!!
    }

    suspend fun createUser(userId: String, user: User) {
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .set(user)
            .await()
    }

    suspend fun updateUserAvatarUrl(userId: String, url: String) {
        val updateMap = mapOf(
            AVATAR_FIELD to url
        )

        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .set(updateMap, SetOptions.merge())
            .await()
    }

    suspend fun updateUsername(userId: String, username: String) {
        val updateMap = mapOf(
            USERNAME_FIELD to username
        )

        firestore.collection(USERS_COLLECTION)
            .document(userId)
            .set(updateMap, SetOptions.merge())
            .await()
    }

    suspend fun getUsers(userIds: List<String>): Map<String, User> {
        val snapshot = firestore
            .collection(USERS_COLLECTION)
            .whereIn(FieldPath.documentId(), userIds)
            .get()
            .await()

        return snapshot.documents.map { it.id to it.toObject(User::class.java)!! }.toMap()
    }
}