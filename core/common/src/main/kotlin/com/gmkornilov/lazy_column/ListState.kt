package com.gmkornilov.lazy_column

sealed class ListState<in T: Any> {
    object None: ListState<Any>()

    object Loading: ListState<Any>()

    data class Error(val e: Exception): ListState<Any>()

    data class Success<T: Any>(val contents: List<T>): ListState<T>()
}