package com.gmkornilov.authorizarion.data

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.data.AuthInteractorImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {
    @Binds
    fun authInteractor(authInteractorImpl: AuthInteractorImpl): AuthInteractor

    companion object {
        @ExperimentalCoroutinesApi
        @Provides
        internal fun interactorImpl(firebaseAuth: FirebaseAuth): AuthInteractorImpl {
            return AuthInteractorImpl.createInstance(firebaseAuth)
        }
    }
}