package com.gmkornilov.postcreatepage.brick_navigation

import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.NavigationScreenProvider
import javax.inject.Inject

class PostCreatePageScreenFactory @Inject constructor(
    override val dependency: Deps,
): NavigationScreenProvider<PostCreatePageScreenFactory.Deps> {
    interface Deps: Dependency {

    }

    @dagger.Component(
        dependencies = [Deps::class]
    )
    interface Component {

    }
}