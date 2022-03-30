package com.gmkornilov.brick_navigation

import androidx.compose.runtime.Composable
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.flow.SharedFlow

abstract class ScreenFactory {
    fun build(prevPath: String): Screen<*> {
        return BaseScreen(
            "$prevPath/${buildKey()}",
            onCreate = this::onCreate,
            onDestroy = this::onDestroy,
            content = { this.Content(arg = it) },
        )
    }

    abstract fun buildKey(): String

    abstract fun onCreate(flow: SharedFlow<DataContainer>, arg: DataContainer): BaseViewModel<*, *>

    open fun onDestroy(arg: DataContainer) {
    }

    @Composable
    abstract fun Content(arg: DataContainer)
}