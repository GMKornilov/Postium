package com.gmkornilov.authorization.registration.domain

import com.gmkornilov.authorizarion.model.PostiumUser

internal interface RegistrationFlowEvents {
    fun successfulRegistration(postiumUser: PostiumUser)
}