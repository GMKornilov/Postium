package com.gmkornilov.authorization.home

import androidx.activity.result.ActivityResult
import androidx.lifecycle.viewModelScope
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.facebook.FacebookAuthInteractor
import com.gmkornilov.authorizarion.facebook.FacebookAuthStatus
import com.gmkornilov.authorizarion.google.GoogleAuthInteractor
import com.gmkornilov.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val googleAuthInteractor: GoogleAuthInteractor,
    private val facebookAuthInteractor: FacebookAuthInteractor
) : BaseViewModel<HomeState, HomeSideEffect>(), HomeEvents {
    override fun getBaseState(): HomeState = HomeState.None

    @ExperimentalCoroutinesApi
    override fun credentialsSignIn(login: String, password: String) = intent {

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
    override fun handleGoogleSignInResult(activityResult: ActivityResult) {
        viewModelScope.launch {
            googleAuthInteractor.signIn(activityResult).collect {

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
            }
        }
    }


    override fun appleSignIn() = intent {

    }
}