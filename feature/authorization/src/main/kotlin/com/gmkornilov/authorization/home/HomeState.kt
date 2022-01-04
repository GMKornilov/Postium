package com.gmkornilov.authorization.home

sealed class HomeState {
    object None: HomeState()

    object WrongAuthorization: HomeState()
}