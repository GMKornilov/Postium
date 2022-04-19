package com.gmkornilov.postcreatepage.brick_navigation

sealed class PostEnterPageArgument {
    object CreatePost: PostEnterPageArgument()

    data class EditPost(
        val postId: String,
        val title: String,
        val contents: String,
    ): PostEnterPageArgument()
}