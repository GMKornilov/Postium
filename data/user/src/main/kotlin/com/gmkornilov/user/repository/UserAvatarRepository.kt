package com.gmkornilov.user.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAvatarRepository @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
) {
    suspend fun uploadPhoto(userId: String, uri: Uri): String {
        val path = "${userId}/avatar.jpg"
        val storageRef = firebaseStorage.reference

        val avatarReference = storageRef.child(path)
        avatarReference.putFile(uri).await()
        return avatarReference.downloadUrl.await().toString()
    }
}