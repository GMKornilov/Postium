package com.gmkornilov.postpage.postpage

internal interface PostpageEvents {
    fun openComments()

    fun likePost()

    fun dislikePost()

    fun bookmarkPost()

    companion object {
        val MOCK = object : PostpageEvents {
            override fun openComments() = Unit
            override fun likePost() = Unit
            override fun dislikePost() = Unit
            override fun bookmarkPost() = Unit
        }
    }
}