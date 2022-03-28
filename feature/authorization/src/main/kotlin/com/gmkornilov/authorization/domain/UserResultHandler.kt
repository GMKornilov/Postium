package com.gmkornilov.authorization.domain

import com.gmkornilov.authorizarion.model.PostiumUser

fun interface UserResultHandler {
    fun handleResult(user: PostiumUser)
}