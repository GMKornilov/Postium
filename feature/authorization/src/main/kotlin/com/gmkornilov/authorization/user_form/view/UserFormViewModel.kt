package com.gmkornilov.authorization.user_form.view

import android.net.Uri
import com.gmkornilov.authorizarion.model.PostiumUser
import com.gmkornilov.authorization.user_form.domain.UserFormFlowEvents
import com.gmkornilov.authorization.user_form.domain.UserFormInteractor
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

internal class UserFormViewModel @Inject constructor(
    private val postiumUser: PostiumUser,
    private val userFormInteractor: UserFormInteractor,
    private val userFormFlowEvents: UserFormFlowEvents,
) : BaseViewModel<UserFormState, UserFormSideEffect>(), UserFormEvents {
    override fun getBaseState(): UserFormState {
        return UserFormState(
            username = postiumUser.getDisplayName().orEmpty(),
            avatartUrl = postiumUser.getProfilePhotoUrl(),
        )
    }

    override fun photoUploadedClicked() = intent {
        postSideEffect(UserFormSideEffect.UploadPhoto)
    }

    override fun photoUploaded(uri: Uri?) = intent {
        reduce { this.state.copy(avatartUrl = uri?.toString()) }

        viewModelScope.launch {
            uri?.let {
                try {
                    userFormInteractor.uploadPhoto(uri)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    override fun updateUsername(username: String) = intent {
        if (username.isEmpty()) {
            return@intent
        }
        val trimmedUsername = username.trim()

        viewModelScope.launch {
            try {
                userFormInteractor.updateUsername(trimmedUsername)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        postSideEffect(UserFormSideEffect.ScrollToEnd)
    }

    override fun formFinished() = intent {
        userFormFlowEvents.userFormBack(postiumUser)
    }
}