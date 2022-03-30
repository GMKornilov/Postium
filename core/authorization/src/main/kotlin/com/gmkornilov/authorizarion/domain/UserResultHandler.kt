package com.gmkornilov.authorizarion.domain

import com.gmkornilov.authorizarion.model.PostiumUser

fun interface UserResultHandler {
    fun handleResult(user: PostiumUser)
}