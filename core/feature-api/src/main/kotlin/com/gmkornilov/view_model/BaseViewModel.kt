package com.gmkornilov.view_model

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

abstract class BaseViewModel<State : Any, SideEffect : Any> : ViewModel(),
    ContainerHost<State, SideEffect> {
    abstract fun getBaseState(): State

    override val container: Container<State, SideEffect> = container(getBaseState())
}