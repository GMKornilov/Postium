package com.gmkornilov.postcreatepage.view

import com.alphicc.brick.TreeRouter
import com.gmkornilov.postcreatepage.domain.PostCreateInteractor
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

internal class PostCreateViewModel @Inject constructor(
    private val router: TreeRouter,
    private val postCreateInteractor: PostCreateInteractor,
) : BaseViewModel<PostCreateState, PostCreateSideEffect>(), PostCreateEvents {
    override fun getBaseState(): PostCreateState {
        return PostCreateState()
    }

    fun backPressed() = intent {
        postSideEffect(PostCreateSideEffect.ShowExitDialog)
    }

    fun backConfirmed() = intent {
        router.back()
    }

    override fun createPost(title: String, content: String) = intent {
        if (title.isBlank()) {
            postSideEffect(PostCreateSideEffect.EmptyTitle)
            return@intent
        }
        if (content.isBlank()) {
            postSideEffect(PostCreateSideEffect.EmptyContent)
            return@intent
        }

        viewModelScope.launch {
            try {
                reduce { PostCreateState(isLoading = true) }
                val isSuccessful = postCreateInteractor.createPost(title, content)
                reduce { PostCreateState(isLoading = false) }

                if (isSuccessful) {
                    router.back()
                } else {
                    postSideEffect(PostCreateSideEffect.Error)
                }
            } catch (e: Exception) {
                Timber.e(e)
                postSideEffect(PostCreateSideEffect.Error)
            }
        }
    }
}