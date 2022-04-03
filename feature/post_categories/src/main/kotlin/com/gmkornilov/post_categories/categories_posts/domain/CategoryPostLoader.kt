package com.gmkornilov.post_categories.categories_posts.domain

import com.gmkornilov.categories.model.Category
import com.gmkornilov.categories.repository.CategoriesRepository
import com.gmkornilov.model.Post
import com.gmkornilov.post.repository.PostRepository
import com.gmkornilov.source.FirebasePostSource
import javax.inject.Inject

class CategoryPostLoader @Inject constructor(
    private val category: Category,
    private val firebasePostSource: FirebasePostSource,
    private val categoryRepository: CategoriesRepository,
): PostRepository.PostLoader {
    override suspend fun loadPosts(): List<Post> {
        val categoryReference = categoryRepository.getReference(category.id)
        return firebasePostSource.getPostsWithCategory(categoryReference)
    }
}