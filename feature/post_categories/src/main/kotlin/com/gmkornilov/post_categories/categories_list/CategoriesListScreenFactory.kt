package com.gmkornilov.post_categories.categories_list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.brick_navigation.ScreenFactory
import com.gmkornilov.categories.repository.CategoriesRepository
import com.gmkornilov.post_categories.categories_list.view.CategoriesList
import com.gmkornilov.post_categories.categories_list.view.CategoriesListener
import com.gmkornilov.post_categories.categories_list.view.CategoriesViewModel
import com.gmkornilov.view_model.BaseViewModel
import dagger.BindsInstance
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Scope

private const val CATEGORIES_KEY = "categories"

class CategoriesListScreenFactory @Inject constructor(
    override val dependency: Deps
): DependencyProvider<CategoriesListScreenFactory.Deps> {
    private inner class Factory(private val listener: CategoriesListener): ScreenFactory() {
        override fun buildKey(): String {
            return CATEGORIES_KEY
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer
        ): BaseViewModel<*, *> {
            val component = DaggerCategoriesListScreenFactory_Component
                .builder()
                .deps(dependency)
                .listener(listener)
                .build()
            return component.viewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<CategoriesViewModel>()

            CategoriesList(viewModel = viewModel, modifier = Modifier.fillMaxSize())
        }

    }

    @Scope
    annotation class CategoriesListScope

    fun build(listener: CategoriesListener, prevPath: String): Screen<*> {
        return Factory(listener).build(prevPath)
    }

    interface Deps: Dependency {
        val categoriesRepository: CategoriesRepository
    }

    @CategoriesListScope
    @dagger.Component(dependencies = [Deps::class])
    internal interface Component {
        val viewModel: CategoriesViewModel

        @dagger.Component.Builder
        interface Builder {
            fun deps(dependency: Deps): Builder

            @BindsInstance
            fun listener(listener: CategoriesListener): Builder

            fun build(): Component
        }
    }
}