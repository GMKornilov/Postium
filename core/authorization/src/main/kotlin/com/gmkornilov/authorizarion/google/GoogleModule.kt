package com.gmkornilov.authorizarion.google

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface GoogleModule {
    @Binds
    @Singleton
    fun googleAuthInteractor(googleAuthInteractor: GoogleAuthInteractorImpl): GoogleAuthInteractor
}