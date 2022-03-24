package com.gmkornilov.mainpage.mainpage

internal interface MainPageEvents {
    fun selectTimeRange(postTimeRange: PostTimeRange)

    companion object {
        val MOCK = object : MainPageEvents {
            override fun selectTimeRange(postTimeRange: PostTimeRange) = Unit
        }
    }
}