package com.gmkornilov.authorizarion.google

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface GoogleModule {
    @Binds
    @Singleton
    fun googleAuthInteractor(googleAuthInteractor: GoogleAuthInteractorImpl): GoogleAuthInteractor
}