package com.gmkornilov.postcreatepage.domain

import com.gmkornilov.postcreatepage.brick_navigation.PostEditResult

fun interface PostEditResultHandler {
    fun handleEditResult(postEditResult: PostEditResult)
}