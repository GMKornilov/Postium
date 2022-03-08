package com.gmkornilov.authorization.home.domain

import com.gmkornilov.authorizarion.model.PostiumUser

internal interface HomeFlowEvents {
    fun registerClicked()

    fun passwordRestorationClicked()

    fun successfulAuthorization(user: PostiumUser?)

    fun rootBackClicked()
}