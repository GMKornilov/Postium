package com.gmkornilov.postcreatepage.view

import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.postcreatepage.R
import com.gmkornilov.postcreatepage.brick_navigation.PostEnterPageArgument
import com.gmkornilov.postcreatepage.domain.PostCreateCategory
import com.gmkornilov.postcreatepage.domain.PostCreateInteractor
import com.gmkornilov.postcreatepage.domain.PostEditResultHandler
import com.gmkornilov.strings.StringsProvider
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.*
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import java.util.*
import javax.inject.Inject

internal class PostCreateViewModel @Inject constructor(
    private val postCreateInteractor: PostCreateInteractor,
    private val listener: PostCreateListener,
    private val enterPageArgument: PostEnterPageArgument,
    private val optionalResultHandler: PostEditResultHandler?,
    private val stringsProvider: StringsProvider,
) : BaseViewModel<PostCreateState, PostCreateSideEffect>(), PostCreateEvents {
    override fun getBaseState(): PostCreateState {
        return PostCreateState()
    }

    fun backPressed() = intent {
        val title: String
        val message: String

        when (enterPageArgument) {
            PostEnterPageArgument.CreatePost -> {
                title = stringsProvider.getStringById(R.string.dialog_create_title)
                message = stringsProvider.getStringById(R.string.dialog_create_text)
            }
            is PostEnterPageArgument.EditPost -> {
                title = stringsProvider.getStringById(R.string.dialog_edit_title)
                message = stringsProvider.getStringById(R.string.dialog_edit_text)
            }
        }

        postSideEffect(PostCreateSideEffect.ShowExitDialog(title, message))
    }

    fun backConfirmed() = intent {
        listener.exitScreen()
    }

    fun loadCategories() = intent {
        reduce { this.state.copy(categoryState = ListState.Loading) }
        try {
            val categories = postCreateInteractor.getCategories()
            reduce { this.state.copy(categoryState = ListState.Success(categories)) }
        } catch (e: Exception) {
            Timber.e(e)
            reduce { this.state.copy(categoryState = ListState.Error(e)) }
        }
    }

    override fun submitPost(title: String, content: String) = intent {
        if (title.isBlank()) {
            postSideEffect(PostCreateSideEffect.EmptyTitle)
            return@intent
        }
        if (content.isBlank()) {
            postSideEffect(PostCreateSideEffect.EmptyContent)
            return@intent
        }

        when (enterPageArgument) {
            PostEnterPageArgument.CreatePost -> createPost(title, content)
            is PostEnterPageArgument.EditPost -> editPost(title, content)
        }
    }

    private fun createPost(title: String, content: String) = intent {
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

    private fun editPost(title: String, content: String) = intent {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                reduce { PostCreateState(isLoading = true) }
                val postEditResult = postCreateInteractor.editPost(title, content)
                reduce { PostCreateState(isLoading = false) }

                if (postEditResult != null) {
                    listener.exitScreen()
                    coroutineScope {
                        optionalResultHandler?.handleEditResult(postEditResult)
                    }
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

    fun restoreData() = intent {
        when (enterPageArgument) {
            PostEnterPageArgument.CreatePost -> restoreDraft()
            is PostEnterPageArgument.EditPost -> {
                postSideEffect(
                    PostCreateSideEffect.RestoreDraft(
                        enterPageArgument.title,
                        enterPageArgument.contents
                    )
                )
            }
        }
    }

    private fun restoreDraft() = intent {
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