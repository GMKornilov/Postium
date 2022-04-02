package com.gmkornilov.categories.repository

import com.gmkornilov.categories.model.Category
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val CATEGORY_COLLECTION = "categories"

class CategoriesRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    fun getReference(categoryId: String): DocumentReference {
        return firestore.collection(CATEGORY_COLLECTION).document(categoryId)
    }

    suspend fun getCategoryByReference(categoryReference: DocumentReference): Category {
        val snapshot = categoryReference.get().await()
        return parseSnapshot(snapshot)
    }

    suspend fun getCategories(): List<Category> {
        val snapshot = firestore.collection(CATEGORY_COLLECTION).get().await()
        return snapshot.documents.map {
            parseSnapshot(it)
        }
    }

    private fun parseSnapshot(snapshot: DocumentSnapshot): Category {
        return snapshot.toObject(Category::class.java)!!.copy(id = snapshot.id)
    }
}