package com.gmkornilov.post_categories.categories_posts.domain

import com.gmkornilov.post.model.PostBookmarkStatus
import com.gmkornilov.post.model.PostLikeStatus
import com.gmkornilov.post.repository.PostRepository
import javax.inject.Inject

class CategoryPostsInteractor @Inject constructor(
    private val postsRepository: PostRepository,
) {
    suspend fun loadPosts(categoryId: String) = postsRepository.loadCategoryPosts(categoryId)

    suspend fun setLikeStatus(postId: String, likeStatus: PostLikeStatus) {
        postsRepository.setLikeStatus(postId, likeStatus)
    }

    suspend fun setBookmarkStatus(postId: String, bookmarkStatus: PostBookmarkStatus) {
        postsRepository.setBookmarkStatus(postId, bookmarkStatus)
    }
}