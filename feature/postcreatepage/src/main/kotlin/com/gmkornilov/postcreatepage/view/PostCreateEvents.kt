package com.gmkornilov.postcreatepage.view

import com.gmkornilov.postcreatepage.domain.PostCreateCategory

interface PostCreateEvents {

    fun submitPost(title: String, content: String)

    fun markPost(isMarked: Boolean, category: PostCreateCategory)

    companion object : PostCreateEvents {
        override fun submitPost(title: String, content: String) = Unit
        override fun markPost(isMarked: Boolean, category: PostCreateCategory) = Unit
    }
}