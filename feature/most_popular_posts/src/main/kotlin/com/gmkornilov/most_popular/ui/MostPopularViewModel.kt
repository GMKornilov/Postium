package com.gmkornilov.most_popular.ui

import com.gmkornilov.model.Post
import com.gmkornilov.view_model.BaseViewModel
import org.orbitmvi.orbit.syntax.simple.intent

class MostPopularViewModel : BaseViewModel<MostPopularState, MostPopularSideEffect>() {
    override fun getBaseState(): MostPopularState = MostPopularState.EMPTY

    fun openPost(post: Post) = intent {

    }
}