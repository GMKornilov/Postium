package com.gmkornilov.post_categories.categories_posts

import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import javax.inject.Inject

class CategoriesPostsScreenFactory @Inject constructor(
    override val dependency: Deps
): DependencyProvider<CategoriesPostsScreenFactory.Deps> {

    interface Deps: Dependency {

    }

    @dagger.Component
    interface Component {

    }
}