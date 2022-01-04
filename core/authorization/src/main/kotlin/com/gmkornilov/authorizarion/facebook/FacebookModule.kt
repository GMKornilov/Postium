package com.gmkornilov.authorizarion.facebook

import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FacebookModule {
    @Binds
    @Singleton
    fun facebookAuthInteractor(facebookAuthInteractorImpl: FacebookAuthInteractorImpl): FacebookAuthInteractor

    companion object {
        @Provides
        @Singleton
        fun callbackManager(): CallbackManager {
            return CallbackManager.Factory.create()
        }

        @Provides
        @Singleton
        fun loginManager(): LoginManager {
            return LoginManager.getInstance()
        }
    }
}
