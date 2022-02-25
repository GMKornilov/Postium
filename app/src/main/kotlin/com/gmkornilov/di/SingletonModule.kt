package com.gmkornilov.di

import com.gmkornilov.activity_utils.ActivityHelper
import com.gmkornilov.authorizarion.data.AuthModule
import com.gmkornilov.authorizarion.email.EmailModule
import com.gmkornilov.authorizarion.facebook.FacebookModule
import com.gmkornilov.authorizarion.firebase.FirebaseModule
import com.gmkornilov.authorizarion.google.GoogleModule
import com.gmkornilov.secrets.SecretsModule
import dagger.Module
import javax.inject.Singleton

@Module(
    includes = [
        DepsModule::class,
        AuthModule::class,
        EmailModule::class,
        FacebookModule::class,
        FirebaseModule::class,
        GoogleModule::class,
        SecretsModule::class,
        NavigationModule::class,
    ]
)
interface SingletonModule {
    companion object {
        @Singleton
        fun activityHelper() = ActivityHelper()
    }
}