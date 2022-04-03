package com.gmkornilov.mainpage.domain

import com.gmkornilov.mainpage.mainpage.PostTimeRange
import com.gmkornilov.mainpage.mainpage.toDataTimeRange
import com.gmkornilov.model.Post
import com.gmkornilov.post.repository.PostRepository
import com.gmkornilov.source.FirebasePostSource
import javax.inject.Inject

internal class MainpagePostLoader @Inject constructor(
    private val timeRange: PostTimeRange,
    private val firebasePostSource: FirebasePostSource,
): PostRepository.PostLoader {
    override suspend fun loadPosts(): List<Post> {
        val dataTimeRange = timeRange.toDataTimeRange()
        return firebasePostSource.getPostsFromTimeRange(dataTimeRange)
    }
}