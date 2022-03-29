package com.gmkornilov.userpage.brick_navigation

sealed class UserPageArgument(open val id: String) {
    data class LoadHeader(override val id: String) : UserPageArgument(id)

    data class ReadyHeader(override val id: String, val username: String, val avatarUrl: String?) :
        UserPageArgument(id)
}