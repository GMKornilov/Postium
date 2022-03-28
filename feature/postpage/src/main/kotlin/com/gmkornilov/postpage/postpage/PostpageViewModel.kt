package com.gmkornilov.postpage.postpage

import com.gmkornilov.postpage.brick_navigation.PostPageArgument
import com.gmkornilov.view_model.BaseViewModel
import javax.inject.Inject

internal class PostpageViewModel @Inject constructor(
    private val postPageArgument: PostPageArgument,
): BaseViewModel<PostpageState, Unit>(), PostpageEvents {
    override fun getBaseState(): PostpageState {
        return PostpageState.None(postPageArgument)
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