package com.gmkornilov.postcreatepage.domain

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.post_contents.repository.PostContentsRepository
import com.gmkornilov.source.FirebasePostSource
import com.gmkornilov.user.repository.UserRepository
import javax.inject.Inject

internal class PostCreateInteractor @Inject constructor(
    private val firebasePostSource: FirebasePostSource,
    private val postContentsRepository: PostContentsRepository,
    private val userRepository: UserRepository,
    private val authInteractor: AuthInteractor
) {
    suspend fun createPost(title: String, content: String): Boolean {
        if (title.isBlank() || content.isBlank()) {
            return false
        }
        val currentUser = authInteractor.getPostiumUser() ?: return false

        val userReference = userRepository.getUserReference(currentUser.getUid())
        val post = firebasePostSource.createPost(userReference, title)

        postContentsRepository.uploadPostContent(post.id, content)
        return true
    }
}