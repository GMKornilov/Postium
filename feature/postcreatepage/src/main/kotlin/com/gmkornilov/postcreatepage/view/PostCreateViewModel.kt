package com.gmkornilov.postcreatepage.view

import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.postcreatepage.domain.PostCreateCategory
import com.gmkornilov.postcreatepage.domain.PostCreateInteractor
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.*
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

internal class PostCreateViewModel @Inject constructor(
    private val postCreateInteractor: PostCreateInteractor,
    private val listener: PostCreateListener,
) : BaseViewModel<PostCreateState, PostCreateSideEffect>(), PostCreateEvents {
    override fun getBaseState(): PostCreateState {
        return PostCreateState()
    }

    fun backPressed() = intent {
        postSideEffect(PostCreateSideEffect.ShowExitDialog)
    }

    fun backConfirmed() = intent {
        listener.exitScreen()
    }

    fun loadCategories() = intent {
        reduce { this.state.copy(categoryState = ListState.Loading) }
        try {
            val categories = postCreateInteractor.getCategories().map {
                PostCreateCategory(it, false)
            }
            reduce { this.state.copy(categoryState = ListState.Success(categories)) }
        } catch (e: Exception) {
            Timber.e(e)
            reduce { this.state.copy(categoryState = ListState.Error(e)) }
        }
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

        viewModelScope.launch(Dispatchers.IO) {
            try {
                reduce { PostCreateState(isLoading = true) }
                val isSuccessful = postCreateInteractor.createPost(title, content)
                reduce { PostCreateState(isLoading = false) }

                if (isSuccessful) {
                    listener.exitScreen()
                } else {
                    postSideEffect(PostCreateSideEffect.Error)
                }
            } catch (e: Exception) {
                Timber.e(e)
                postSideEffect(PostCreateSideEffect.Error)
            }
        }
    }

    override fun markPost(isMarked: Boolean, category: PostCreateCategory) = intent {
        if (isMarked) {
            postCreateInteractor.addCategory(category.category)
        } else {
            postCreateInteractor.removeCategory(category.category)
        }

        val state = this.state.categoryState
        if (state is ListState.Success) {
            val newCategory = category.copy(isMarked = isMarked)
            val categories = state.contents.toMutableList().apply {
                val index = this.indexOf(category)
                if (index != -1) {
                    this[index] = newCategory
                }
            }
            reduce { this.state.copy(categoryState = ListState.Success(categories)) }
        }
    }

    fun loadDraft() = intent {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val draft = postCreateInteractor.getDraft()
                postSideEffect(PostCreateSideEffect.RestoreDraft(draft.title, draft.contents))
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun saveDraft(title: String, contents: String) = runBlocking {
        coroutineScope {
            withContext(Dispatchers.IO) {
                postCreateInteractor.saveDraft(title, contents)
            }
        }
    }
}

interface PostCreateListener {
    fun exitScreen()
}