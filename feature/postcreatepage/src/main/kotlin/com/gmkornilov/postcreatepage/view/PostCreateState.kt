package com.gmkornilov.postcreatepage.view

import androidx.annotation.StringRes
import com.gmkornilov.categories.model.Category
import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.postcreatepage.R
import com.gmkornilov.postcreatepage.domain.PostCreateCategory

internal data class PostCreateState (
    val isLoading: Boolean = false,
    val categoryState: ListState<PostCreateCategory> = ListState.None,
)

enum class Tab(
    @StringRes val headerRes: Int,
) {
    EDITOR(R.string.editor_tab),
    PREVIEW(R.string.preview_tab),
    CATEGORIES(R.string.categories_tab),
}