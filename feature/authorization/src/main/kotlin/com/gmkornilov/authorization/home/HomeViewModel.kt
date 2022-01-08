package com.gmkornilov.authorization.home

import androidx.activity.result.ActivityResult
import androidx.lifecycle.viewModelScope
import com.gmkornilov.authorizarion.email.EmailAuthInteractor
import com.gmkornilov.authorizarion.facebook.FacebookAuthInteractor
import com.gmkornilov.authorizarion.facebook.FacebookAuthStatus
import com.gmkornilov.authorizarion.google.GoogleAuthInteractor
import com.gmkornilov.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
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
            emailAuthInteractor.signIn(login, password).collect {
                reduce { state.copy(isLoading = false) }
                if (!it) {
                    postSideEffect(HomeSideEffect.LoginError)
                }
            }
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
            googleAuthInteractor.signIn(activityResult).collect {
                if (!it) {
                    postSideEffect(HomeSideEffect.LoginError)
                }
            }
        }
    }

    override fun vkSignIn() = intent {

    }

    @ExperimentalCoroutinesApi
    override fun facebookSignIn() = intent {
        viewModelScope.launch {
            facebookAuthInteractor.signIn()
                .flatMapConcat {
                    when (it) {
                        FacebookAuthStatus.AuthCancelled -> flowOf(false)
                        FacebookAuthStatus.AuthError -> flowOf(false)
                        is FacebookAuthStatus.AuthSuccessful -> facebookAuthInteractor.passToken(it.token)
                    }
                }.collect {
                    if (!it) {
                        postSideEffect(HomeSideEffect.LoginError)
                    }
                }
        }
    }


}