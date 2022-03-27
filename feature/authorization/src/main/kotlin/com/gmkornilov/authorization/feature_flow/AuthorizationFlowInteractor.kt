package com.gmkornilov.authorization.feature_flow

import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.model.PostiumUser
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.authorization.home.HomeScreenFactory
import com.gmkornilov.authorization.home.domain.HomeFlowEvents
import com.gmkornilov.authorization.registration.RegistrationScreenFactory
import com.gmkornilov.authorization.registration.domain.RegistrationFlowEvents
import com.gmkornilov.authorization.user_form.UserFormScreenFactory
import com.gmkornilov.authorization.user_form.domain.UserFormFlowEvents
import javax.inject.Inject

internal class AuthorizationFlowInteractor @Inject constructor(
    private val router: TreeRouter,
    private val homeScreenFactory: HomeScreenFactory,
    private val registrationScreenFactory: RegistrationScreenFactory,
    private val userFormScreenFactory: UserFormScreenFactory,
    private val userResultHandler: UserResultHandler,
): HomeFlowEvents, RegistrationFlowEvents, UserFormFlowEvents {
    private var authorizationStep = AuthorizationStep.NONE

    fun startAuthorizationFlow() {
        if (authorizationStep != AuthorizationStep.NONE) {
            return
        }

        authorizationStep = AuthorizationStep.LOGIN
        router.addScreen(homeScreenFactory.build(), userResultHandler)
    }

    private enum class AuthorizationStep {
        NONE,
        LOGIN,
        REGISTRATION,
        FORGOT_PASSWORD,
        USER_FORM
    }

    override fun registerClicked() {
        authorizationStep = AuthorizationStep.REGISTRATION
        router.addScreen(registrationScreenFactory.build())
    }

    override fun passwordRestorationClicked() {
        authorizationStep = AuthorizationStep.FORGOT_PASSWORD
        // TODO: router navigate to password registration screen
    }

    override fun successfulAuthorization(user: PostiumUser, isNew: Boolean) {
        if (isNew) {
            handleNewUser(user)
        } else {
            router.backToScreen(homeScreenFactory.screenKey)
            router.backScreen()
            userResultHandler.handleResult(user)
        }
    }

    override fun rootBackClicked() {
        router.backScreen()
    }

    override fun successfulRegistration(postiumUser: PostiumUser) {
        handleNewUser(postiumUser)
    }

    private fun handleNewUser(user: PostiumUser) {
        router.backToScreen(homeScreenFactory.screenKey)
        authorizationStep = AuthorizationStep.USER_FORM
        router.replaceScreen(userFormScreenFactory.build(), user)
    }

    override fun userFormBack(user: PostiumUser) {
        authorizationStep = AuthorizationStep.NONE
        router.backScreen()
        userResultHandler.handleResult(user)
    }
}