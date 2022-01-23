package com.gmkornilov.authorization.home

sealed class HomeState {
    object None: HomeState()

    object Loading: HomeState()

    object UserDoesntExist: HomeState()

    object WrongPassword: HomeState()

    companion object {
        val DEFAULT = HomeState.None
    }
}