package com.gmkornilov.secrets

import dagger.Binds
import dagger.Module

@Module
interface SecretsModule {
    @Binds
    fun secretsProvider(secretsProviderImpl: SecretsProviderImpl): SecretsProvider
}