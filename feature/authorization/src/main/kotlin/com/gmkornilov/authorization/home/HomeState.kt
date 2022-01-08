package com.gmkornilov.authorization.home

data class HomeState(val isLoading: Boolean) {
    companion object {
        val DEFAULT = HomeState(isLoading = false)
    }
}