package com.gmkornilov.authorizarion.google

import android.content.Intent
import androidx.activity.result.ActivityResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface GoogleAuthInteractor {
    fun getSignInIntent(): Intent

    @ExperimentalCoroutinesApi
    fun signIn(result: ActivityResult): Flow<Boolean>
}