package com.gmkornilov.authorization.feature_flow

import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.model.PostiumUser
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.authorization.home.HomeScreenFactory
import com.gmkornilov.authorization.home.domain.HomeFlowEvents
import com.gmkornilov.authorization.registration.RegistrationScreenFactory
import com.gmkornilov.authorization.registration.domain.RegistrationFlowEvents
import javax.inject.Inject

class AuthorizationFlowInteractor @Inject constructor(
    private val router: TreeRouter,
    private val homeScreenFactory: HomeScreenFactory,
    private val registrationScreenFactory: RegistrationScreenFactory,
    private val userResultHandler: UserResultHandler,
): HomeFlowEvents, RegistrationFlowEvents {
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
        router.addChild(registrationScreenFactory.build())
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