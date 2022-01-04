package com.gmkornilov.authorizarion.utils

import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
interface UtilsModule {
    companion object {
        @Provides
        fun activityResultRegistry(@ActivityContext context: Context): ActivityResultRegistry {
            return (context as? AppCompatActivity)?.activityResultRegistry!!
        }
    }
}