package com.gmkornilov.userpage.brick_navigation

data class UserPageArgument(
    val id: String,
    val username: String,
    val avatarUrl: String? = null,
)