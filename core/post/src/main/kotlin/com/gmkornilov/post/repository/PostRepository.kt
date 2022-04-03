package com.gmkornilov.post.repository

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.categories.repository.CategoriesRepository
import com.gmkornilov.model.Post
import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.post.model.*
import com.gmkornilov.post_bookmarks.PostBookmarkRepository
import com.gmkornilov.post_likes.PostLikeRepository
import com.gmkornilov.source.FirebasePostSource
import com.gmkornilov.user.model.User
import com.gmkornilov.user.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val firebasePostSource: FirebasePostSource,
    private val likeRepository: PostLikeRepository,
    private val authInteractor: AuthInteractor,
    private val bookmarkRepository: PostBookmarkRepository,
    private val userRepository: UserRepository,
    private val categoryRepository: CategoriesRepository,
) {
    suspend fun loadDataWithTimeRange(postTimeRange: SelectionTimeRange): List<PostPreviewData> {
        val postLoader = PostLoader {
            firebasePostSource.getPostsFromTimeRange(postTimeRange.toTimeRange())
        }
        return loadPostsPreview(postLoader)
    }

    suspend fun loadDataWithUserId(userId: String): List<PostPreviewData> {
        val postLoader = PostLoader {
            val reference = userRepository.getUserReference(userId)
            firebasePostSource.getPostsWithUserReference(reference)
        }
        return loadPostsPreview(postLoader)
    }

    suspend fun loadUserBookmarks(userId: String): List<PostPreviewData> {
        val postLoader = PostLoader {
            val bookmarkIds = bookmarkRepository.getUserBookmarks(userId)
            firebasePostSource.getPostWithIds(bookmarkIds)
        }
        return loadPostsPreview(postLoader)
    }

    suspend fun loadCategoryPosts(categoryId: String): List<PostPreviewData> {
        val postLoader = PostLoader {
            val categoryReference = categoryRepository.getReference(categoryId)
            firebasePostSource.getPostsWithCategory(categoryReference)
        }
        return loadPostsPreview(postLoader)
    }

    suspend fun loadPlaylistPosts(playlist: Playlist): List<PostPreviewData> {
        val postLoader = PostLoader {
            firebasePostSource.getPostWithIds(playlist.postIds)
        }
        return loadPostsPreview(postLoader)
    }

    private suspend fun loadPostsPreview(postLoader: PostLoader): List<PostPreviewData> {
        val currentUser = authInteractor.getPostiumUser()

        val posts = postLoader.loadPosts()

        val users = posts.map {
            userRepository.getByReference(it.userReference!!)
        }

        val postLikeStatuses = currentUser?.let {
            likeRepository.getLikesStatuses(it.getUid(), posts.map { post -> post.id })
        } ?: emptyMap()

        val postBookmarkStatuses = currentUser?.let {
            bookmarkRepository.getBookmarkStatuses(it.getUid(), posts.map { post -> post.id })
        } ?: emptyMap()

        return posts.mapIndexed { index, post ->
            val user = users[index]

            PostPreviewData(
                id = post.id,
                title = post.title,
                userId = post.userReference!!.id,
                username = user.name,
                avatarUrl = user.avatarUrl?.ifBlank { null },
                likeStatus = postLikeStatuses[post.id].toPostLikeStatus(),
                bookmarkStatus = postBookmarkStatuses[post.id].toPostBookmarkStatus(),
            )
        }
    }

    suspend fun setLikeStatus(postId: String, likeStatus: PostLikeStatus) {
        if (postId.isEmpty()) {
            return
        }
        val currentUser = authInteractor.getPostiumUser() ?: return
        when (likeStatus) {
            PostLikeStatus.LIKED -> likeRepository.likePost(currentUser.getUid(), postId)
            PostLikeStatus.DISLIKED -> likeRepository.dislikePost(currentUser.getUid(), postId)
            PostLikeStatus.NONE -> likeRepository.removeStatus(currentUser.getUid(), postId)
        }
    }

    suspend fun setBookmarkStatus(postId: String, bookmarkStatus: PostBookmarkStatus) {
        if (postId.isBlank()) {
            return
        }
        val currentUser = authInteractor.getPostiumUser() ?: return
        when (bookmarkStatus) {
            PostBookmarkStatus.BOOKMARKED -> bookmarkRepository.addBookmark(
                currentUser.getUid(),
                postId
            )
            PostBookmarkStatus.NOT_BOOKMARKED -> bookmarkRepository.removeBookmark(
                currentUser.getUid(),
                postId
            )
        }
    }

    private fun interface PostLoader {
        suspend fun loadPosts(): List<Post>
    }
}