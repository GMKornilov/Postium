package com.gmkornilov.post_categories.categories_posts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.brick_navigation.ScreenFactory
import com.gmkornilov.categories.model.Category
import com.gmkornilov.categories.repository.CategoriesRepository
import com.gmkornilov.post.repository.PostRepository
import com.gmkornilov.post_categories.categories_posts.domain.CategoryPostLoader
import com.gmkornilov.post_categories.categories_posts.view.CategoryPostsList
import com.gmkornilov.post_categories.categories_posts.view.CategoryPostsViewModel
import com.gmkornilov.post_list.view.PostsListListener
import com.gmkornilov.source.FirebasePostSource
import com.gmkornilov.view_model.BaseViewModel
import dagger.Binds
import dagger.BindsInstance
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Scope

private const val CATEGORY_POSTS_KEY = "category_posts"

class CategoriesPostsScreenFactory @Inject constructor(
    override val dependency: Deps,
) : DependencyProvider<CategoriesPostsScreenFactory.Deps> {
    private inner class Factory(
        private val listener: PostsListListener,
        private val category: Category,
    ) : ScreenFactory() {
        override fun buildKey(): String {
            return "${CATEGORY_POSTS_KEY}_${category.id}_${category.name}"
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer,
        ): BaseViewModel<*, *> {
            val component = DaggerCategoriesPostsScreenFactory_Component.builder()
                .deps(dependency)
                .category(category)
                .listener(listener)
                .build()
            return component.viewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<CategoryPostsViewModel>()

            CategoryPostsList(viewModel, Modifier.fillMaxSize())
        }

    }

    fun build(listener: PostsListListener, category: Category, prevPath: String): Screen<*> {
        return Factory(listener, category).build(prevPath)
    }

    @Scope
    annotation class CategoriesPostsScope

    interface Deps : Dependency {
        val postsRepository: PostRepository

        val authInteractor: AuthInteractor
        val firebasePostSource: FirebasePostSource
        val categoryRepository: CategoriesRepository
    }

    @CategoriesPostsScope
    @dagger.Component(dependencies = [Deps::class], modules = [Module::class])
    internal interface Component {
        val viewModel: CategoryPostsViewModel

        @dagger.Component.Builder
        interface Builder {
            fun deps(dependency: Deps): Builder

            @BindsInstance
            fun category(category: Category): Builder

            @BindsInstance
            fun listener(listener: PostsListListener): Builder

            fun build(): Component
        }
    }

    @dagger.Module
    internal interface Module {
        @Binds
        @CategoriesPostsScope
        fun bindPostLoader(categoryPostLoader: CategoryPostLoader): PostRepository.PostLoader
    }
}