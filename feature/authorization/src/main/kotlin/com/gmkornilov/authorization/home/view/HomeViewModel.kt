package com.gmkornilov.authorization.home.view

import androidx.activity.result.ActivityResult
import com.gmkornilov.authorizarion.data.SignInResult
import com.gmkornilov.authorizarion.email.EmailAuthInteractor
import com.gmkornilov.authorizarion.email.EmailAuthResult
import com.gmkornilov.authorizarion.facebook.FacebookAuthInteractor
import com.gmkornilov.authorizarion.facebook.FacebookAuthStatus
import com.gmkornilov.authorizarion.google.GoogleAuthInteractor
import com.gmkornilov.authorizarion.model.PostiumUser
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.authorization.home.domain.HomeFlowEvents
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

internal class HomeViewModel @Inject constructor(
    private val googleAuthInteractor: GoogleAuthInteractor,
    private val facebookAuthInteractor: FacebookAuthInteractor,
    private val emailAuthInteractor: EmailAuthInteractor,
    private val homeFlowEvents: HomeFlowEvents,
) : BaseViewModel<HomeState, HomeSideEffect>(), HomeEvents {
    override fun getBaseState(): HomeState = HomeState.DEFAULT

    @ExperimentalCoroutinesApi
    override fun credentialsSignIn(login: String, password: String) = intent {
        if (login.isEmpty() || password.isEmpty()) {
            return@intent
        }

        viewModelScope.launch {
            reduce { HomeState.Loading }
            val result = emailAuthInteractor.signIn(login, password)
            reduce {
                when (result) {
                    is EmailAuthResult.Success -> {
                        handleSuccessfulResult(result.postiumUser, false)
                        HomeState.None
                    }
                    EmailAuthResult.UserDoesntExist -> HomeState.UserDoesntExist
                    EmailAuthResult.WrongPassword -> HomeState.WrongPassword
                }
            }
        }
    }

    override fun register() = homeFlowEvents.registerClicked()

    override fun passwordRestoration() = homeFlowEvents.passwordRestorationClicked()

    override fun googleSignIn() = intent {
        val signInIntent = googleAuthInteractor.getSignInIntent()

        postSideEffect(HomeSideEffect.GoogleSignIn(signInIntent))
    }

    @ExperimentalCoroutinesApi
    override fun handleGoogleSignInResult(activityResult: ActivityResult) = intent {
        viewModelScope.launch {
            googleAuthInteractor.signIn(activityResult)
        }
    }

    override fun vkSignIn() = intent {

    }

    @ExperimentalCoroutinesApi
    override fun facebookSignIn() = intent {
        viewModelScope.launch {
            when (val facebookAuthStatus = facebookAuthInteractor.signIn()) {
                FacebookAuthStatus.AuthCancelled -> {
                }
                FacebookAuthStatus.AuthError -> {
                    postSideEffect(HomeSideEffect.LoginError)
                }
                is FacebookAuthStatus.AuthSuccessful -> {
                    when (val result = facebookAuthInteractor.passToken(facebookAuthStatus.token)) {
                        is SignInResult.ExistingUser -> handleSuccessfulResult(result.user, false)
                        is SignInResult.NewUser -> handleSuccessfulResult(result.user, true)
                    }
                }
            }
        }
    }

    private fun handleSuccessfulResult(postiumUser: PostiumUser, isNew: Boolean) {
        homeFlowEvents.successfulAuthorization(postiumUser, isNew)
    }
}