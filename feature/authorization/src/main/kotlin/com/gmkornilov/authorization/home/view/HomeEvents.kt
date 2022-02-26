package com.gmkornilov.authorization.home.view

import androidx.activity.result.ActivityResult
import kotlinx.coroutines.ExperimentalCoroutinesApi

interface HomeEvents {
    @ExperimentalCoroutinesApi
    fun credentialsSignIn(login: String, password: String)

    fun register()

    fun passwordRestoration()

    fun googleSignIn()

    @ExperimentalCoroutinesApi
    fun handleGoogleSignInResult(activityResult: ActivityResult)

    fun vkSignIn()

    @ExperimentalCoroutinesApi
    fun facebookSignIn()

    companion object {
        @ExperimentalCoroutinesApi
        val MOCK = object : HomeEvents {
            override fun credentialsSignIn(login: String, password: String) {
            }

            override fun register() {
            }

            override fun passwordRestoration() {
            }

            override fun googleSignIn() {
            }

            override fun handleGoogleSignInResult(activityResult: ActivityResult) {
            }

            override fun vkSignIn() {
            }

            override fun facebookSignIn() {
            }

        }
    }
}