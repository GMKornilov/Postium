package com.gmkornilov.mainpage.di

import com.gmkornilov.mainpage.feature_api.MainpageFeature
import com.gmkornilov.mainpage.feature_api.MainpageFeatureImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FeatureModule {
    @Binds
    fun mainpageFlowFeature(mainpageFeature: MainpageFeatureImpl): MainpageFeature
}