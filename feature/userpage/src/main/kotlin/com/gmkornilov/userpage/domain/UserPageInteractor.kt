package com.gmkornilov.userpage.domain

import com.gmkornilov.post.model.PostBookmarkStatus
import com.gmkornilov.post.model.PostLikeStatus
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.post.repository.PostRepository
import com.gmkornilov.user.model.User
import com.gmkornilov.user.repository.UserRepository
import javax.inject.Inject

internal class UserPageInteractor @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
) {
    suspend fun loadPosts(userId: String): List<PostPreviewData> {
        val posts = postRepository.loadDataWithUserId(userId)

        return posts.map {
            it.copy(avatarUrl = null, username = "")
        }
    }

    suspend fun loadHeader(userId: String): User {
        return userRepository.getUser(userId)
    }

    suspend fun loadBookmarks(userId: String): List<PostPreviewData> {
        return postRepository.loadUserBookmarks(userId)
    }

    suspend fun setLikeStatus(postPreviewData: PostPreviewData, likeStatus: PostLikeStatus) {
        postRepository.setLikeStatus(postPreviewData.id, likeStatus)
    }

    suspend fun setBookmarkStatus(
        postPreviewData: PostPreviewData,
        bookmarkStatus: PostBookmarkStatus
    ) {
        postRepository.setBookmarkStatus(postPreviewData.id, bookmarkStatus)
    }
}