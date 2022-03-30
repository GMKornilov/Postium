package com.gmkornilov.postpage.view

internal interface PostpageEvents {
    fun openComments()

    fun openProfile()

    fun likePost()

    fun dislikePost()

    fun bookmarkPost()

    companion object {
        val MOCK = object : PostpageEvents {
            override fun openComments() = Unit
            override fun openProfile() = Unit
            override fun likePost() = Unit
            override fun dislikePost() = Unit
            override fun bookmarkPost() = Unit
        }
    }
}