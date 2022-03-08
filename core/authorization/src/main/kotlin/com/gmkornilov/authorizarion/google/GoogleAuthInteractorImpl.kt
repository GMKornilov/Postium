package com.gmkornilov.authorizarion.google

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.data.SignInResult
import com.gmkornilov.context.ApplicationContext
import com.gmkornilov.secrets.SecretsProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
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
    override suspend fun signIn(result: ActivityResult): SignInResult {
        val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).await()
        val idToken = account.idToken

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return authInteractor.signInWithCredential(credential)
    }
}