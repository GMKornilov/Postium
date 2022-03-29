package com.gmkornilov.mainpage.domain

import com.gmkornilov.mainpage.mainpage.PostTimeRange
import com.gmkornilov.mainpage.mainpage.toSelectionTimeRange
import com.gmkornilov.post.model.PostBookmarkStatus
import com.gmkornilov.post.model.PostLikeStatus
import com.gmkornilov.post.model.PostPreviewData

import com.gmkornilov.post.repository.PostRepository
import javax.inject.Inject

internal class MainpageInteractor @Inject constructor(
    private val postRepository: PostRepository,
) {
    suspend fun loadDataWithTimeRange(postTimeRange: PostTimeRange): List<PostPreviewData> {
        return postRepository.loadDataWithTimeRange(postTimeRange.toSelectionTimeRange())
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