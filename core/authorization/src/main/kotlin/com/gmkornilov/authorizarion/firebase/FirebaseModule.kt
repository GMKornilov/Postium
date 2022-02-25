package com.gmkornilov.authorizarion.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides

@Module
interface FirebaseModule {
    companion object {
        @Provides
        fun firebaseAuth(): FirebaseAuth {
            return Firebase.auth
        }
    }
}