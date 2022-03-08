package com.gmkornilov.authorization.home.view

import android.content.Intent

internal sealed class HomeSideEffect {
    data class GoogleSignIn(val intent: Intent): HomeSideEffect()

    object LoginError: HomeSideEffect()
}
