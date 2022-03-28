package com.gmkornilov.postpage.domain

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.post_bookmarks.PostBookmarkRepository
import com.gmkornilov.post_contents.repository.PostContentsRepository
import com.gmkornilov.post_likes.PostLikeRepository
import javax.inject.Inject

internal class PostPageInteractor @Inject constructor(
    private val postContentsRepository: PostContentsRepository,
    private val postBookmarkRepository: PostBookmarkRepository,
    private val postLikeRepository: PostLikeRepository,
    private val authInteractor: AuthInteractor,
) {
    suspend fun loadContent(postId: String): String {
        return postContentsRepository.loadPostContents(postId).content
    }

    suspend fun likePost(postId: String) {
        if (postId.isEmpty()) {
            return
        }
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            postLikeRepository.likePost(it.getUid(), postId)
        }
    }

    suspend fun dislikePost(postId: String) {
        if (postId.isEmpty()) {
            return
        }
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            postLikeRepository.dislikePost(it.getUid(), postId)
        }
    }

    suspend fun removeLikeStatus(postId: String) {
        if (postId.isEmpty()) {
            return
        }
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            postLikeRepository.removeStatus(it.getUid(), postId)
        }
    }

    suspend fun addBookmark(postId: String) {
        if (postId.isEmpty()) {
            return
        }
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            postBookmarkRepository.addBookmark(it.getUid(), postId)
        }
    }

    suspend fun removeBookmark(postId: String) {
        if (postId.isEmpty()) {
            return
        }
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            postBookmarkRepository.removeBookmark(it.getUid(), postId)
        }
    }
}