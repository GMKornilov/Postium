package com.gmkornilov.postium

import android.app.Application
import android.content.Context
import com.gmkornilov.context.ApplicationContext
import com.gmkornilov.di.SingletonDeps
import com.gmkornilov.di.SingletonModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

class MainApplication : Application() {
    @Singleton
    @Component(modules = [SingletonModule::class])
    interface MainComponent: SingletonDeps {
        fun inject(mainActivity: MainActivity)

        @Component.Builder
        interface Builder {
            @BindsInstance
            fun applicationContext(@ApplicationContext context: Context): Builder

            fun build(): MainComponent
        }
    }

    val component by lazy {
        DaggerMainApplication_MainComponent.builder()
            .applicationContext(this)
            .build()
    }
}