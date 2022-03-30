package com.gmkornilov.postcreatepage.view

interface PostCreateEvents {

    fun createPost(title: String, content: String)

    companion object : PostCreateEvents {
        override fun createPost(title: String, content: String) = Unit
    }
}