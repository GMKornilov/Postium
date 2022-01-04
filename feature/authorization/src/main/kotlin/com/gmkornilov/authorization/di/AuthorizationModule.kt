package com.gmkornilov.authorization.di

import com.gmkornilov.authorization.feature_api.AuthorizationFlowFeature
import com.gmkornilov.authorization.feature_api.AuthorizationFlowFeatureImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AuthorizationModule {
    @Binds
    fun authorizationFlowFeature(authorizationFlowFeature: AuthorizationFlowFeatureImpl): AuthorizationFlowFeature
}