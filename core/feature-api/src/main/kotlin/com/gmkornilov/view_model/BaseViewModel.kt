package com.gmkornilov.view_model

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost

abstract class BaseViewModel<State : Any, SideEffect : Any> : ViewModel(),
    ContainerHost<State, SideEffect>