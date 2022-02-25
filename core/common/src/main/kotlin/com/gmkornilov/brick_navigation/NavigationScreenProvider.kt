package com.gmkornilov.brick_navigation

interface Dependency

interface NavigationScreenProvider<D: Dependency> {
    val dependency: D
}