package com.gmkornilov.mainpage.domain

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.mainpage.mainpage.PostTimeRange
import com.gmkornilov.mainpage.mainpage.toTimeRange
import com.gmkornilov.mainpage.model.PostPreviewData
import com.gmkornilov.mainpage.model.toPostPreviewBookmarkStatus
import com.gmkornilov.mainpage.model.toPostPreviewLikeStatus
import com.gmkornilov.post_bookmarks.PostBookmarkRepository
import com.gmkornilov.post_likes.PostLikeRepository
import com.gmkornilov.source.FirebasePostSource
import com.gmkornilov.user.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class MainpageInteractor @Inject constructor(
    private val firebasePostSource: FirebasePostSource,
    private val postLikeRepository: PostLikeRepository,
    private val authInteractor: AuthInteractor,
    private val postBookmarkRepository: PostBookmarkRepository,
) {
    suspend fun loadDataWithTimeRange(postTimeRange: PostTimeRange): List<PostPreviewData> {
        val timeRange = postTimeRange.toTimeRange()

        val currentUser = authInteractor.getPostiumUser()

        val posts = firebasePostSource.getPostsFromTimeRange(timeRange)

        val users = posts.map {
            it.
            userReference!!.get().await().toObject(User::class.java)
        }

        val postLikeStatuses = currentUser?.let {
            postLikeRepository.getLikesStatuses(it.getUid(), posts.map { post -> post.id })
        } ?: emptyMap()

        val postBookmarkStatuses = currentUser?.let {
            postBookmarkRepository.getBookmarkStatuses(it.getUid(), posts.map { post -> post.id })
        } ?: emptyMap()

        return posts.mapIndexed { index, post ->
            val user = users[index]

            PostPreviewData(
                id = post.id,
                title = post.title,
                username = user?.name,
                avatarUrl = user?.avatarUrl,
                likeStatus = postLikeStatuses[post.id].toPostPreviewLikeStatus(),
                bookmarkStatus = postBookmarkStatuses[post.id].toPostPreviewBookmarkStatus(),
            )
        }
    }

    suspend fun likePost(postPreviewData: PostPreviewData) {
        if (postPreviewData.id.isEmpty()) {
            return
        }
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            postLikeRepository.likePost(it.getUid(), postPreviewData.id)
        }
    }

    suspend fun dislikePost(postPreviewData: PostPreviewData) {
        if (postPreviewData.id.isEmpty()) {
            return
        }
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            postLikeRepository.dislikePost(it.getUid(), postPreviewData.id)
        }
    }

    suspend fun removeLikeStatus(postPreviewData: PostPreviewData) {
        if (postPreviewData.id.isEmpty()) {
            return
        }
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            postLikeRepository.removeStatus(it.getUid(), postPreviewData.id)
        }
    }

    suspend fun addBookmark(postPreviewData: PostPreviewData) {
        if (postPreviewData.id.isEmpty()) {
            return
        }
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            postBookmarkRepository.addBookmark(it.getUid(), postPreviewData.id)
        }
    }

    suspend fun removeBookmark(postPreviewData: PostPreviewData) {
        if (postPreviewData.id.isEmpty()) {
            return
        }
        val currentUser = authInteractor.getPostiumUser()

        currentUser?.let {
            postBookmarkRepository.removeBookmark(it.getUid(), postPreviewData.id)
        }
    }
}