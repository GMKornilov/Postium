package com.gmkornilov.activity_utils

import androidx.activity.ComponentActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityHelper @Inject constructor() {
    private var activity: ComponentActivity? = null

    val activityResultRegistry get() =  activity?.activityResultRegistry

    val activityResultRegistryOwner get() = activity

    fun setActivity(activity: ComponentActivity) {
        this.activity = activity
    }

    fun resetActivity() {
        this.activity = null
    }
}