package com.gmkornilov.authorization.feature_flow

import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.model.PostiumUser
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.authorization.home.HomeScreenFactory
import com.gmkornilov.authorization.home.domain.HomeFlowEvents
import javax.inject.Inject

class AuthorizationFlowInteractor @Inject constructor(
    private val router: TreeRouter,
    private val homeScreenFactory: HomeScreenFactory,
    private val userResultHandler: UserResultHandler,
): HomeFlowEvents {
    private var authorizationStep = AuthorizationStep.NONE

    fun startAuthorizationFlow() {
        if (authorizationStep == AuthorizationStep.NONE) {
            return
        }

        authorizationStep = AuthorizationStep.LOGIN
        router.addScreen(homeScreenFactory.build(), userResultHandler)
    }

    private enum class AuthorizationStep {
        NONE,
        LOGIN,
        REGISTRATION,
        FORGOT_PASSWORD
    }

    override fun registerClicked() {
        authorizationStep = AuthorizationStep.REGISTRATION
        // TODO: router navigate to registration screen
    }

    override fun passwordRestorationClicked() {
        authorizationStep = AuthorizationStep.FORGOT_PASSWORD
        // TODO: router navigate to password registration screen
    }

    override fun successfulAuthorization(user: PostiumUser?) {
        userResultHandler.handleResult(user)
    }

    override fun rootBackClicked() {
        router.backScreen()
    }
}