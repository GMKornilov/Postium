package com.gmkornilov.strings

import android.content.Context
import androidx.annotation.StringRes

class StringsProvider(private val context: Context) {
    fun getStringById(@StringRes resId: Int): String {
        return context.getString(resId)
    }
}