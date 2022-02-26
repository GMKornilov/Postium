package com.gmkornilov.authorization.home.view

import android.content.Intent

sealed class HomeSideEffect {
    data class GoogleSignIn(val intent: Intent): HomeSideEffect()

    object LoginError: HomeSideEffect()
}
