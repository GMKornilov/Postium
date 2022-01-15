package com.gmkornilov.authorizarion.facebook

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.gmkornilov.activity_utils.ActivityHelper
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.google.firebase.auth.FacebookAuthProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

private const val EMAIL_PERMISSION = "email"
private const val PUBLIC_PROFILE_PERMISSION = "public_profile"

internal class FacebookAuthInteractorImpl @Inject constructor(
    private val activityHelper: ActivityHelper,
    private val callbackManager: CallbackManager,
    private val loginManager: LoginManager,
    private val authInteractor: AuthInteractor
) : FacebookAuthInteractor {
    @ExperimentalCoroutinesApi
    override fun signIn() = callbackFlow {
        val callback = object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                sendBlocking(FacebookAuthStatus.AuthCancelled)
            }

            override fun onError(error: FacebookException) {
                sendBlocking(FacebookAuthStatus.AuthError)
            }

            override fun onSuccess(result: LoginResult) {
                sendBlocking(FacebookAuthStatus.AuthSuccessful(result.accessToken.token))
            }

        }

        loginManager.registerCallback(callbackManager, callback)

        activityHelper.activityResultRegistryOwner?.let {
            loginManager.logIn(
                it,
                callbackManager,
                listOf(EMAIL_PERMISSION, PUBLIC_PROFILE_PERMISSION)
            )
        }

        awaitClose { loginManager.unregisterCallback(callbackManager) }
    }

    @ExperimentalCoroutinesApi
    override fun passToken(token: String): Flow<Boolean> {
        val credential = FacebookAuthProvider.getCredential(token)

        return authInteractor.signInWithCredential(credential)
    }

}