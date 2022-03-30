package com.gmkornilov.brick_navigation

import androidx.compose.runtime.Composable
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.flow.SharedFlow

inline fun <reified D : BaseViewModel<*, *>> BaseScreen(
    key: String,
    noinline onCreate: ((SharedFlow<DataContainer>, DataContainer) -> D)? = null,
    noinline onDestroy: ((DataContainer) -> Unit)? = null,
    noinline content: @Composable (DataContainer) -> Unit,
): Screen<D> {
    val screenOnDestroy: (DataContainer) -> Unit = {
        val viewModel = it.get<D>()
        viewModel.onDestroy()

        onDestroy?.invoke(it)
    }

    return Screen(key, onCreate, screenOnDestroy, content)
}

fun String.dropLastScreen() = this.split("/").dropLast(1).joinToString("/")