package com.gmkornilov.postcreatepage.domain

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.categories.model.Category
import com.gmkornilov.categories.repository.CategoriesRepository
import com.gmkornilov.post_contents.repository.PostContentsRepository
import com.gmkornilov.postcreatepage.brick_navigation.PostEditResult
import com.gmkornilov.postcreatepage.brick_navigation.PostEnterPageArgument
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
    private val postEnterPageArgument: PostEnterPageArgument,
) {
    private val postId = when (postEnterPageArgument) {
        PostEnterPageArgument.CreatePost -> null
        is PostEnterPageArgument.EditPost -> postEnterPageArgument.postId
    }

    private var initializedCategories = false

    private val categorySet = mutableSetOf<Category>()

    suspend fun getCategories(): List<PostCreateCategory> {
        val postCategories = postId?.let { id ->
            val references = firebasePostSource.getPostById(id).categoryReference
            references.map {
                categoriesRepository.getCategoryByReference(it)
            }
        } ?: emptyList()
        initializedCategories = true
        return categoriesRepository.getCategories().map {
            val isMarked = postCategories.contains(it)
            PostCreateCategory(it, isMarked)
        }
    }

    suspend fun createPost(title: String, content: String): Boolean {
        if (title.isBlank() || content.isBlank()) {
            return false
        }
        val currentUser = authInteractor.getPostiumUser() ?: return false

        val references = categoriesRepository.getReferences(categorySet.toList().map { it.id })

        val userReference = userRepository.getUserReference(currentUser.getUid())
        return if (postEnterPageArgument is PostEnterPageArgument.CreatePost) {
            val post = firebasePostSource.createPost(userReference, title, references)
            postContentsRepository.uploadPostContent(post.id, content)
            true
        } else false
    }

    suspend fun editPost(title: String, content: String): PostEditResult? {
        val currentUser = authInteractor.getPostiumUser() ?: return null

        val references = if (initializedCategories) {
            categoriesRepository.getReferences(categorySet.toList().map { it.id })
        } else null

        userRepository.getUserReference(currentUser.getUid())

        return if (postEnterPageArgument is PostEnterPageArgument.EditPost) {
            firebasePostSource.updatePost(postEnterPageArgument.postId, title, references)
            postContentsRepository.uploadPostContent(postEnterPageArgument.postId, content)
            PostEditResult(title, content)
        } else {
            null
        }
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