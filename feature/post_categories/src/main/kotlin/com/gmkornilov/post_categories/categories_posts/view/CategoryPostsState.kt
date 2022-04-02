package com.gmkornilov.post_categories.categories_posts.view

import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.post.model.PostPreviewData

data class CategoryPostsState(
    val listState: ListState<PostPreviewData> = ListState.None,
    val categoryName: String = "",
    val isRefreshing: Boolean = false,
)