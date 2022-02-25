package com.gmkornilov.authorizarion.facebook

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.gmkornilov.activity_utils.ActivityHelper
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.data.SignInResult
import com.google.firebase.auth.FacebookAuthProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

private const val EMAIL_PERMISSION = "email"
private const val PUBLIC_PROFILE_PERMISSION = "public_profile"

class FacebookAuthInteractorImpl @Inject constructor(
    private val activityHelper: ActivityHelper,
    private val callbackManager: CallbackManager,
    private val loginManager: LoginManager,
    private val authInteractor: AuthInteractor
) : FacebookAuthInteractor {
    @ExperimentalCoroutinesApi
    override suspend fun signIn() = suspendCancellableCoroutine<FacebookAuthStatus> {
        val cancellationCallback = { _: Throwable? ->
            loginManager.unregisterCallback(callbackManager)
        }

        val callback = object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                it.resume(FacebookAuthStatus.AuthCancelled, cancellationCallback)
            }

            override fun onError(error: FacebookException) {
                it.resume(FacebookAuthStatus.AuthError, cancellationCallback)
            }

            override fun onSuccess(result: LoginResult) {
                it.resume(
                    FacebookAuthStatus.AuthSuccessful(result.accessToken.token),
                    cancellationCallback
                )
            }

        }

        loginManager.registerCallback(callbackManager, callback)

        activityHelper.activityResultRegistryOwner?.let { activity ->
            loginManager.logIn(
                activity,
                callbackManager,
                listOf(EMAIL_PERMISSION, PUBLIC_PROFILE_PERMISSION)
            )
        }

        it.invokeOnCancellation { cause -> cancellationCallback(cause) }
    }

    @ExperimentalCoroutinesApi
    override suspend fun passToken(token: String): SignInResult {
        val credential = FacebookAuthProvider.getCredential(token)

        return authInteractor.signInWithCredential(credential)
    }

}