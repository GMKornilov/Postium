package com.gmkornilov.authorization.domain

import com.gmkornilov.authorizarion.model.PostiumUser

interface UserResultHandler {
    fun handleResult(user: PostiumUser)
}