package com.gmkornilov.authorization.home

import android.content.Intent

sealed class HomeSideEffect {
    data class GoogleSignIn(val intent: Intent): HomeSideEffect()
}
