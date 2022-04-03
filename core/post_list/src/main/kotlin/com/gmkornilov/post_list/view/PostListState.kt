package com.gmkornilov.post_list.view

import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.post.model.PostPreviewData

data class PostListState(
    val isRefreshing: Boolean = false,
    val listState: ListState<PostPreviewData> = ListState.None,
)