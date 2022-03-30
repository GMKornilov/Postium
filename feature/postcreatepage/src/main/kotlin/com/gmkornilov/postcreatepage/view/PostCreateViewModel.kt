package com.gmkornilov.postcreatepage.view

import com.alphicc.brick.TreeRouter
import com.gmkornilov.view_model.BaseViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import javax.inject.Inject

internal class PostCreateViewModel @Inject constructor(
    private val router: TreeRouter,
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
}