package com.gmkornilov.mainpage.mainpage

import com.gmkornilov.post.Post

internal interface MainPageEvents {
    fun selectTimeRange(postTimeRange: PostTimeRange)

    fun openPost(post: Post)

    fun likePost(post: Post)

    fun dislikePost(post: Post)

    fun bookmarkPost(post: Post)

    companion object {
        val MOCK = object : MainPageEvents {
            override fun selectTimeRange(postTimeRange: PostTimeRange) = Unit
            override fun openPost(post: Post) = Unit
            override fun likePost(post: Post) = Unit
            override fun dislikePost(post: Post) = Unit
            override fun bookmarkPost(post: Post) = Unit
        }
    }
}