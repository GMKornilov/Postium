package com.gmkornilov.activity_utils

import androidx.activity.ComponentActivity
import javax.inject.Inject

class ActivityHelper @Inject constructor() {
    private var activity: ComponentActivity? = null

    val activityResultRegistry = activity?.activityResultRegistry

    val activityResultRegistryOwner = activity

    fun setActivity(activity: ComponentActivity) {
        this.activity = activity
    }

    fun resetActivity() {
        this.activity = null
    }
}