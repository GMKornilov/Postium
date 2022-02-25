package com.gmkornilov.authorizarion.facebook

import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
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
