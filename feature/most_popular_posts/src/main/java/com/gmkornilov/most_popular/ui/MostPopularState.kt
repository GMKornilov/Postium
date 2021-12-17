package com.gmkornilov.most_popular.ui

import com.gmkornilov.post.Post

data class MostPopularState(
    val posts: List<Post> = emptyList()
) {
    companion object {
        val EMPTY = MostPopularState()
    }
}