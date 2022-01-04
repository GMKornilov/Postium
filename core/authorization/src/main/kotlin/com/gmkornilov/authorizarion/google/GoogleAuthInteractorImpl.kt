package com.gmkornilov.authorizarion.google

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.secrets.SecretsProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GoogleAuthInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authInteractor: AuthInteractor,
    secretsProvider: SecretsProvider,
) : GoogleAuthInteractor {
    private val googleApiToken = secretsProvider.googleApiKey

    private val googleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(googleApiToken)
            .requestEmail()
            .build()

    override fun getSignInIntent(): Intent {
        val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
        return googleSignInClient.signInIntent
    }

    @ExperimentalCoroutinesApi
    override fun signIn(result: ActivityResult): Flow<Boolean> {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        return try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            authInteractor.signInWithCredential(credential)
        } catch (e: ApiException) {
            flowOf(false)
        }
    }
}