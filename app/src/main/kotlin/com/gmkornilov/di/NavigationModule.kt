package com.gmkornilov.di

import com.alphicc.brick.TreeRouter
import dagger.Module
import dagger.Provides

@Module
interface NavigationModule {
    companion object {
        @Provides
        fun router(): TreeRouter {
            return TreeRouter.new()
        }
    }
}