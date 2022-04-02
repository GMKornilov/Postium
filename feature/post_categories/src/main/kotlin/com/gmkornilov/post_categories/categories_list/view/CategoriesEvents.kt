package com.gmkornilov.post_categories.categories_list.view

import com.gmkornilov.categories.model.Category

internal interface CategoriesEvents {
    fun openCategory(category: Category)

    fun refreshData()

    companion object: CategoriesEvents {
        override fun openCategory(category: Category) = Unit
        override fun refreshData() = Unit
    }
}