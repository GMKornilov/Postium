package com.gmkornilov.authorizarion.data

import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Module
interface AuthModule {
    @Binds
    @Singleton
    fun authInteractor(authInteractorImpl: AuthInteractorImpl): AuthInteractor

    companion object {
        @ExperimentalCoroutinesApi
        @Provides
        @Singleton
        internal fun interactorImpl(firebaseAuth: FirebaseAuth): AuthInteractorImpl {
            return AuthInteractorImpl.createInstance(firebaseAuth)
        }
    }
}