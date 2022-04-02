package com.gmkornilov.post_categories.categories_list.domain

import com.gmkornilov.categories.repository.CategoriesRepository
import javax.inject.Inject

internal class CategoriesListInteractor @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
) {
    suspend fun loadCategories() = categoriesRepository.getCategories()
}