package com.gmkornilov.most_popular.ui

import com.gmkornilov.model.Post

data class MostPopularState(
    val posts: List<Post> = emptyList()
) {
    companion object {
        val EMPTY = MostPopularState()
    }
}