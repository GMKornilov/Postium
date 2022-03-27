package com.gmkornilov.mainpage.domain

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.mainpage.model.PostPreviewData
import com.gmkornilov.mainpage.model.PostPreviewLikeStatus
import com.gmkornilov.mainpage.model.toPostPreviewLikeStatus
import com.gmkornilov.post_likes.PostLikeRepository
import com.gmkornilov.source.FirebasePostSource
import javax.inject.Inject

class MainpageInteractor @Inject constructor(
    private val firebasePostSource: FirebasePostSource,
    private val postLikeRepository: PostLikeRepository,
    private val authInteractor: AuthInteractor,
) {
    suspend fun loadData(): List<PostPreviewData> {
        val currentUser = authInteractor.getPostiumUser()

        val posts = firebasePostSource.getAllPosts()
        val postStatuses = currentUser?.let {
            postLikeRepository.getLikesStatuses(it.getUid(), posts.map { post -> post.id })
        } ?: emptyMap()

        return posts.map {
            PostPreviewData(
                id = it.id,
                title = it.title,
                likeStatus = postStatuses[it.id]?.toPostPreviewLikeStatus()
                    ?: PostPreviewLikeStatus.NONE
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
}