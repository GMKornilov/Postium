package com.gmkornilov.postcreatepage.view

import com.gmkornilov.view_model.BaseViewModel
import javax.inject.Inject

internal class PostCreateViewModel @Inject constructor(

) : BaseViewModel<PostCreateState, PostCreateSideEffect>(), PostCreateEvents {
    override fun getBaseState(): PostCreateState {
        return PostCreateState()
    }
}