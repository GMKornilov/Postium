package com.gmkornilov.di

import com.alphicc.brick.TreeRouter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {
    companion object {
        @Provides
        fun router(): TreeRouter {
            return TreeRouter.new()
        }
    }
}