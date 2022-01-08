package com.gmkornilov.di

import com.gmkornilov.authorization.feature_api.AuthorizationFlowFeature
import com.gmkornilov.feature_api.FeatureApi
import com.gmkornilov.mainpage.feature_api.MainpageFeature
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {
    @Binds
    @IntoSet
    fun authorizationFlowFeature(homeFeatureApi: AuthorizationFlowFeature): FeatureApi

    @Binds
    @IntoSet
    fun mainPageFeature(homeFeatureApi: MainpageFeature): FeatureApi
}