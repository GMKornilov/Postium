package com.gmkornilov.authorization.user_form.domain

import com.gmkornilov.authorizarion.model.PostiumUser

interface UserFormFlowEvents {
    fun userFormBack(user: PostiumUser)
}