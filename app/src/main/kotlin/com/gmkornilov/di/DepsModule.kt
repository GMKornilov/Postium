package com.gmkornilov.di

import com.gmkornilov.postium.MainApplication
import com.gmkornilov.root_screen.RootScreenFactory
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DepsModule {
    @Singleton
    @Binds
    fun bindSingletonDeps(mainApplicationComponent: MainApplication.MainComponent): SingletonDeps

    @Singleton
    @Binds
    fun bindRootDeps(singletonDeps: SingletonDeps): RootScreenFactory.Deps
}