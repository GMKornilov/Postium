package com.gmkornilov.authorization.user_form.domain

import android.net.Uri
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.user.repository.UserAvatarRepository
import com.gmkornilov.user.repository.UserRepository
import javax.inject.Inject

internal class UserFormInteractor @Inject constructor(
    private val userAvatarRepository: UserAvatarRepository,
    private val userRepository: UserRepository,
    private val authInteractor: AuthInteractor,
) {
    suspend fun uploadPhoto(uri: Uri) {
        val currentUser = authInteractor.getPostiumUser() ?: return

        val downloadUrl = userAvatarRepository.uploadPhoto(currentUser.getUid(), uri)

        userRepository.updateUserAvatarUrl(currentUser.getUid(), downloadUrl)
    }

    suspend fun updateUsername(username: String) {
        val currentUser = authInteractor.getPostiumUser() ?: return
        userRepository.updateUsername(currentUser.getUid(), username)
    }
}