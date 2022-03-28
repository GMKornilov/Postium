package com.gmkornilov.postpage.postpage

import com.alphicc.brick.TreeRouter
import com.gmkornilov.postpage.brick_navigation.PostPageArgument
import com.gmkornilov.postpage.domain.PostPageInteractor
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

internal class PostpageViewModel @Inject constructor(
    private val postPageArgument: PostPageArgument,
    private val treeRouter: TreeRouter,
    private val postPageInteractor: PostPageInteractor,
): BaseViewModel<PostpageState, Unit>(), PostpageEvents {
    override fun getBaseState(): PostpageState {
        return PostpageState(postPageArgument, ContentState.None)
    }

    fun loadContent() = intent {
        reduce { this.state.copy(contentState = ContentState.Loading) }

        viewModelScope.launch {
            try {
                val content = postPageInteractor.loadContent(postPageArgument.id)
                reduce { this.state.copy(contentState = ContentState.Success(content)) }
            } catch(e: Exception) {
                Timber.e(e)
                reduce { this.state.copy(contentState = ContentState.Error(e)) }
            }
        }
    }

    override fun openComments() {
        TODO("Not yet implemented")
    }

    override fun likePost() {
        TODO("Not yet implemented")
    }

    override fun dislikePost() {
        TODO("Not yet implemented")
    }

    override fun bookmarkPost() {
        TODO("Not yet implemented")
    }
}