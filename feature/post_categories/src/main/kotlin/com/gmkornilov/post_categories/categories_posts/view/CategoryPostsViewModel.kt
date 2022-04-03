package com.gmkornilov.post_categories.categories_posts.view

import com.gmkornilov.categories.model.Category
import com.gmkornilov.post_list.view.PostListEvents
import com.gmkornilov.post_list.view.PostListViewModel
import com.gmkornilov.view_model.BaseViewModel
import javax.inject.Inject

internal class CategoryPostsViewModel @Inject constructor(
    category: Category,
    val listViewModel: PostListViewModel,
): PostListEvents by listViewModel, BaseViewModel<Unit, Unit>() {
    val categoryName = category.name

    override fun getBaseState() = Unit

    override fun onDestroy() {
        super.onDestroy()
        listViewModel.onDestroy()
    }
}