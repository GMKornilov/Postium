package com.gmkornilov.post_categories.categories_list.view

import com.gmkornilov.categories.model.Category
import com.gmkornilov.lazy_column.ListState

internal data class CategoriesState(
    val listState: ListState<Category> = ListState.None,
    val isRefreshing: Boolean = false,
)