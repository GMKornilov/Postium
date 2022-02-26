package com.gmkornilov.view_model

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

abstract class BaseViewModel<State : Any, SideEffect : Any> : ViewModel(),
    ContainerHost<State, SideEffect> {
    val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    abstract fun getBaseState(): State

    override val container: Container<State, SideEffect> by lazy {
        container(getBaseState())
    }

    protected fun forceUpdateState(newState: State) = intent {
        reduce {
            newState
        }
    }

    @CallSuper
    open fun onDestroy() {
        viewModelScope.cancel()
    }
}