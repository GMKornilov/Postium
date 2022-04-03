package com.gmkornilov.di

import com.alphicc.brick.TreeRouter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface NavigationModule {
    companion object {
        @Singleton
        @Provides
        fun router(): TreeRouter {
            return TreeRouter.new()
        }
    }
}