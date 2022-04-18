package com.gmkornilov.postcreatepage.domain

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.categories.model.Category
import com.gmkornilov.categories.repository.CategoriesRepository
import com.gmkornilov.model.Post
import com.gmkornilov.post_contents.repository.PostContentsRepository
import com.gmkornilov.postcreatepage.data.PostDraft
import com.gmkornilov.postcreatepage.data.PostDraftRepository
import com.gmkornilov.source.FirebasePostSource
import com.gmkornilov.user.repository.UserRepository
import javax.inject.Inject

internal class PostCreateInteractor @Inject constructor(
    private val firebasePostSource: FirebasePostSource,
    private val postContentsRepository: PostContentsRepository,
    private val userRepository: UserRepository,
    private val authInteractor: AuthInteractor,
    private val categoriesRepository: CategoriesRepository,
    private val postDraftRepository: PostDraftRepository,
) {
    private val categorySet = mutableSetOf<Category>()

    suspend fun getCategories(): List<Category> {
        return categoriesRepository.getCategories()
    }

    suspend fun createPost(title: String, content: String): Boolean {
        if (title.isBlank() || content.isBlank()) {
            return false
        }
        val currentUser = authInteractor.getPostiumUser() ?: return false

        val references = categoriesRepository.getReferences(categorySet.toList().map { it.id })

        val userReference = userRepository.getUserReference(currentUser.getUid())
        val post = firebasePostSource.createPost(userReference, title, references)

        postContentsRepository.uploadPostContent(post.id, content)
        return true
    }

    fun addCategory(category: Category) {
        categorySet.add(category)
    }

    fun removeCategory(category: Category) {
        categorySet.remove(category)
    }

    suspend fun saveDraft(title: String, contents: String) {
        postDraftRepository.updateDraft(PostDraft(title, contents))
    }

    suspend fun getDraft(): PostDraft {
        return postDraftRepository.getPostDraft()
    }
}