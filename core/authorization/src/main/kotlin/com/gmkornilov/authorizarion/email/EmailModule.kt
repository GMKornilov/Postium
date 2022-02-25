package com.gmkornilov.authorizarion.email

import dagger.Binds
import dagger.Module

@Module
interface EmailModule {
    @Binds
    fun emailAuthInteractor(emailAuthInteractorImpl: EmailAuthInteractorImpl): EmailAuthInteractor
}