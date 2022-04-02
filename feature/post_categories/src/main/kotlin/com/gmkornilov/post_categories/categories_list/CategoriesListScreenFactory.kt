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
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

private const val CATEGORIES_KEY = "categories"

class CategoriesListScreenFactory @Inject constructor(
    override val dependency: Deps
): DependencyProvider<CategoriesListScreenFactory.Deps> {
    private inner class Factory: ScreenFactory() {
        override fun buildKey(): String {
            return CATEGORIES_KEY
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer
        ): BaseViewModel<*, *> {
            val component = DaggerCategoriesListScreenFactory_Component.builder()
                .deps(dependency)
                .build()
            return component.viewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<CategoriesViewModel>()

            CategoriesList(viewModel = viewModel, modifier = Modifier.fillMaxSize())
        }

    }

    fun build(prevPath: String): Screen<*> {
        return Factory().build(prevPath)
    }

    interface Deps: Dependency {
        val categoriesListener: CategoriesListener

        val categoriesRepository: CategoriesRepository
    }

    @dagger.Component(dependencies = [Deps::class])
    internal interface Component {
        val viewModel: CategoriesViewModel
    }
}