package com.gmkornilov.userpage.view

internal interface UserPageEvents {
    fun tabSelected(tab: Tab)

    companion object : UserPageEvents {
        override fun tabSelected(tab: Tab) = Unit
    }
}