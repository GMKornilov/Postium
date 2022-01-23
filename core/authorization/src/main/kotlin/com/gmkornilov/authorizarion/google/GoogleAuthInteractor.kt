package com.gmkornilov.authorizarion.google

import android.content.Intent
import androidx.activity.result.ActivityResult
import com.gmkornilov.authorizarion.data.SignInResult
import kotlinx.coroutines.ExperimentalCoroutinesApi

interface GoogleAuthInteractor {
    fun getSignInIntent(): Intent

    @ExperimentalCoroutinesApi
    suspend fun signIn(result: ActivityResult): SignInResult
}