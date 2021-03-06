package com.gmkornilov.post.repository

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.comments.repostiory.CommentRepository
import com.gmkornilov.model.Post
import com.gmkornilov.post.model.*
import com.gmkornilov.post_bookmarks.PostBookmarkRepository
import com.gmkornilov.post_likes.PostLikeRepository
import com.gmkornilov.source.FirebasePostSource
import com.gmkornilov.user.repository.UserRepository
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val firebasePostSource: FirebasePostSource,
    private val likeRepository: PostLikeRepository,
    private val authInteractor: AuthInteractor,
    private val bookmarkRepository: PostBookmarkRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository,
) {
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

    suspend fun loadPostsPreview(postLoader: PostLoader): List<PostPreviewData> {
        val currentUser = authInteractor.getPostiumUser()

        val posts = postLoader.loadPosts()

        val users = posts.map {
            userRepository.getByReference(it.userReference!!)
        }

        val commentsAmounts = posts.map {
            commentRepository.getPostCommentsAmount(it.id)
        }

        val postLikeStatuses = currentUser?.let {
            likeRepository.getLikesStatuses(it.getUid(), posts.map { post -> post.id })
        } ?: emptyMap()

        val postBookmarkStatuses = currentUser?.let {
            bookmarkRepository.getBookmarkStatuses(it.getUid(), posts.map { post -> post.id })
        } ?: emptyMap()

        return posts.mapIndexed { index, post ->
            val user = users[index]
            val commentsAmount = commentsAmounts[index]

            PostPreviewData(
                id = post.id,
                title = post.title,
                userId = post.userReference!!.id,
                username = user.name,
                avatarUrl = user.avatarUrl?.ifBlank { null },
                likeStatus = postLikeStatuses[post.id].toPostLikeStatus(),
                bookmarkStatus = postBookmarkStatuses[post.id].toPostBookmarkStatus(),
                likes = post.likes,
                dislikes = post.dislikes,
                commentAmount = commentsAmount
            )
        }
    }

    suspend fun setLikeStatus(postId: String, likeStatus: PostLikeStatus) {
        if (postId.isEmpty()) {
            return
        }
        val currentUser = authInteractor.getPostiumUser() ?: return
        val currentLikeStatus = likeRepository.getLikeStatus(currentUser.getUid(), postId)
        when (likeStatus) {
            PostLikeStatus.LIKED -> likeRepository.likePost(currentUser.getUid(), postId)
            PostLikeStatus.DISLIKED -> likeRepository.dislikePost(currentUser.getUid(), postId)
            PostLikeStatus.NONE -> likeRepository.removeStatus(currentUser.getUid(), postId)
        }

        when {
            currentLikeStatus.isLiked && !likeStatus.isLiked -> firebasePostSource.decrementLikes(
                postId
            )
            !currentLikeStatus.isLiked && likeStatus.isLiked -> firebasePostSource.incrementLikes(
                postId
            )
        }

        when {
            currentLikeStatus.isDisliked && !likeStatus.isDisliked -> firebasePostSource.decrementDislikes(
                postId
            )
            !currentLikeStatus.isDisliked && likeStatus.isDisliked -> firebasePostSource.incrementDislikes(
                postId
            )
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

    suspend fun deletePost(postId: String) {
        firebasePostSource.deletePost(postId)
    }

    fun interface PostLoader {
        suspend fun loadPosts(): List<Post>
    }
}