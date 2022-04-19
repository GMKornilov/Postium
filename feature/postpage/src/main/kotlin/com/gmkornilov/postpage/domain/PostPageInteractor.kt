package com.gmkornilov.postpage.domain

import com.gmkornilov.post.model.PostBookmarkStatus
import com.gmkornilov.post.model.PostLikeStatus
import com.gmkornilov.post.repository.PostRepository
import com.gmkornilov.post_contents.repository.PostContentsRepository
import javax.inject.Inject

internal class PostPageInteractor @Inject constructor(
    private val postContentsRepository: PostContentsRepository,
    private val postRepository: PostRepository,
) {
    suspend fun loadContent(postId: String): String {
        return postContentsRepository.loadPostContents(postId).content
    }

    suspend fun setLikeStatus(postId: String, likeStatus: PostLikeStatus) {
        postRepository.setLikeStatus(postId, likeStatus)
    }

    suspend fun setBookmarkStatus(postId: String, bookmarkStatus: PostBookmarkStatus) {
        postRepository.setBookmarkStatus(postId, bookmarkStatus)
    }

    suspend fun deletePost(postId: String) {
        postRepository.deletePost(postId)
    }
}