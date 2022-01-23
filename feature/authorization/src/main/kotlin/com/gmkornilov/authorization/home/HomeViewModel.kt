package com.gmkornilov.authorization.home

import androidx.activity.result.ActivityResult
import com.gmkornilov.authorizarion.email.EmailAuthInteractor
import com.gmkornilov.authorizarion.facebook.FacebookAuthInteractor
import com.gmkornilov.authorizarion.facebook.FacebookAuthStatus
import com.gmkornilov.authorizarion.google.GoogleAuthInteractor
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val googleAuthInteractor: GoogleAuthInteractor,
    private val facebookAuthInteractor: FacebookAuthInteractor,
    private val emailAuthInteractor: EmailAuthInteractor,
) : BaseViewModel<HomeState, HomeSideEffect>(), HomeEvents {
    override fun getBaseState(): HomeState = HomeState.DEFAULT

    @ExperimentalCoroutinesApi
    override fun credentialsSignIn(login: String, password: String) = intent {
        if (login.isEmpty() || password.isEmpty()) {
            return@intent
        }

        viewModelScope.launch {
            reduce { state.copy(isLoading = true) }
            val result = emailAuthInteractor.signIn(login, password)
            reduce { state.copy(isLoading = false) }
            // TODO: show something with result
        }
    }

    override fun register() = intent {

    }

    override fun passwordRestoration() = intent {

    }

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
                    val result = facebookAuthInteractor.passToken(facebookAuthStatus.token)
                    // TODO: do something with result
                }
            }
        }
    }


}