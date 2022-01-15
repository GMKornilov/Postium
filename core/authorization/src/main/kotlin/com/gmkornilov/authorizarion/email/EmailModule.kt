package com.gmkornilov.authorizarion.email

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface EmailModule {
    @Binds
    fun emailAuthInteractor(emailAuthInteractorImpl: EmailAuthInteractorImpl): EmailAuthInteractor
}