package com.gmkornilov.postpage.domain

import com.gmkornilov.post_bookmarks.PostBookmarkRepository
import com.gmkornilov.post_contents.repository.PostContentsRepository
import com.gmkornilov.post_likes.PostLikeRepository
import javax.inject.Inject

internal class PostPageInteractor @Inject constructor(
    private val postContentsRepository: PostContentsRepository,
    private val postBookmarkRepository: PostBookmarkRepository,
    private val postLikeRepository: PostLikeRepository,
) {
    suspend fun loadContent(postId: String): String {
        return postContentsRepository.loadPostContents(postId).content
    }
}