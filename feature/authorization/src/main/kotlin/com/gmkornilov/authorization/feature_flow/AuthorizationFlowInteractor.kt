package com.gmkornilov.authorization.feature_flow

import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.domain.UserResultHandler
import com.gmkornilov.authorizarion.model.PostiumUser
import com.gmkornilov.authorization.home.HomeScreenFactory
import com.gmkornilov.authorization.home.domain.HomeFlowEvents
import com.gmkornilov.authorization.password_restoration.PasswordRestorationScreenFactory
import com.gmkornilov.authorization.password_restoration.domain.PasswordRestorationFlowEvents
import com.gmkornilov.authorization.registration.RegistrationScreenFactory
import com.gmkornilov.authorization.registration.domain.RegistrationFlowEvents
import com.gmkornilov.authorization.user_form.UserFormScreenFactory
import com.gmkornilov.authorization.user_form.domain.UserFormFlowEvents
import com.gmkornilov.brick_navigation.dropLastScreen
import com.gmkornilov.user.model.User
import com.gmkornilov.user.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class AuthorizationFlowInteractor @Inject constructor(
    private val router: TreeRouter,
    private val userRepository: UserRepository,
    private val homeScreenFactory: HomeScreenFactory,
    private val registrationScreenFactory: RegistrationScreenFactory,
    private val userFormScreenFactory: UserFormScreenFactory,
    private val passwordRestorationScreenFactory: PasswordRestorationScreenFactory,
    private val userResultHandler: UserResultHandler,
) : HomeFlowEvents, RegistrationFlowEvents, UserFormFlowEvents, PasswordRestorationFlowEvents {
    private val registrationScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var authorizationStep = AuthorizationStep.NONE

    private val currentKey
        get() = router.screen.value?.key.orEmpty()

    private val startScreen by lazy {
        homeScreenFactory.build(userResultHandler)
    }

    fun startAuthorizationFlow() {
        if (authorizationStep != AuthorizationStep.NONE) {
            return
        }

        authorizationStep = AuthorizationStep.LOGIN
        router.addScreen(startScreen)
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
        router.addScreen(registrationScreenFactory.build(currentKey))
    }

    override fun passwordRestorationClicked() {
        authorizationStep = AuthorizationStep.FORGOT_PASSWORD
        router.addScreen(passwordRestorationScreenFactory.build(currentKey))
    }

    override fun successfulAuthorization(user: PostiumUser, isNew: Boolean) {
        if (isNew) {
            handleNewUser(user)
        } else {
            router.backToScreen(startScreen.key)
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
        registrationScope.launch {
            try {
                userRepository.createUser(
                    user.getUid(),
                    User(name = user.getDisplayName().orEmpty(), user.getProfilePhotoUrl())
                )
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        router.backToScreen(startScreen.key)
        authorizationStep = AuthorizationStep.USER_FORM
        router.replaceScreen(userFormScreenFactory.build(user, currentKey.dropLastScreen()))
    }

    override fun userFormBack(user: PostiumUser) {
        authorizationStep = AuthorizationStep.NONE
        router.backScreen()
        userResultHandler.handleResult(user)
    }

    override fun backToHomeScreen() {
        authorizationStep = AuthorizationStep.LOGIN
        router.backToScreen(startScreen.key)
    }
}