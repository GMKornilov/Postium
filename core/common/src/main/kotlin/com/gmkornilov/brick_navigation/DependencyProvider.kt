package com.gmkornilov.brick_navigation

interface Dependency

interface DependencyProvider<D: Dependency> {
    val dependency: D
}