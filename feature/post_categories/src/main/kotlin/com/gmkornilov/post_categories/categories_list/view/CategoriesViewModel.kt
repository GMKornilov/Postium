package com.gmkornilov.post_categories.categories_list.view

import com.gmkornilov.categories.model.Category
import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.post_categories.categories_list.domain.CategoriesListInteractor
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

internal class CategoriesViewModel @Inject constructor(
    private val categoriesListInteractor: CategoriesListInteractor,
    private val listener: CategoriesListener,
) : BaseViewModel<CategoriesState, Unit>(), CategoriesEvents {
    override fun getBaseState(): CategoriesState {
        return CategoriesState()
    }

    fun loadData(isRefresh: Boolean = false) = intent {
        if (isRefresh) {
            reduce { this.state.copy(isRefreshing = true) }
        } else {
            reduce { this.state.copy(listState = ListState.Loading) }
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categories = categoriesListInteractor.loadCategories()
                reduce {
                    CategoriesState(
                        listState = ListState.Success(categories),
                        isRefreshing = false,
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { CategoriesState(listState = ListState.Error(e), isRefreshing = false) }
            }
        }
    }

    override fun openCategory(category: Category) {
        listener.openCategory(category)
    }

    override fun refreshData() {
        loadData(true)
    }
}

interface CategoriesListener {
    fun openCategory(category: Category)
}