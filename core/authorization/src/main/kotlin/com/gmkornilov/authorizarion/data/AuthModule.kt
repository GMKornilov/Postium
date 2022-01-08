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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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