package com.gmkornilov.secrets

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface SecretsModule {
    @Binds
    fun secretsProvider(secretsProviderImpl: SecretsProviderImpl): SecretsProvider
}