package com.gmkornilov.most_popular.ui

import com.gmkornilov.post.Post
import com.gmkornilov.view_model.BaseViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.viewmodel.container

class MostPopularViewModel : BaseViewModel<MostPopularState, MostPopularSideEffect>() {
    override val container: Container<MostPopularState, MostPopularSideEffect> =
        container(MostPopularState.EMPTY)

    fun openPost(post: Post) = intent {

    }
}