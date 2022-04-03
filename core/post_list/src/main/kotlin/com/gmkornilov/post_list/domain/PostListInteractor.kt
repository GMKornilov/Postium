package com.gmkornilov.post_list.domain

import com.gmkornilov.post.model.PostBookmarkStatus
import com.gmkornilov.post.model.PostLikeStatus
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.post.repository.PostRepository
import javax.inject.Inject

class PostListInteractor @Inject constructor(
    private val postLoader: PostRepository.PostLoader,
    private val postRepository: PostRepository,
) {
    suspend fun loadPosts(): List<PostPreviewData> {
        return postRepository.loadPostsPreview(postLoader)
    }

    suspend fun setLikeStatus(postId: String, likeStatus: PostLikeStatus) {
        postRepository.setLikeStatus(postId, likeStatus)
    }

    suspend fun setBookmarkStatus(postId: String, bookmarkStatus: PostBookmarkStatus) {
        postRepository.setBookmarkStatus(postId, bookmarkStatus)
    }
}