package com.gmkornilov.postcreatepage.view

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.gmkornilov.categories.model.Category
import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.postcreatepage.R
import com.gmkornilov.postcreatepage.domain.PostCreateCategory
import compose.icons.TablerIcons
import compose.icons.tablericons.Eye
import compose.icons.tablericons.LayoutGrid
import compose.icons.tablericons.Pencil

internal data class PostCreateState (
    val isLoading: Boolean = false,
    val categoryState: ListState<PostCreateCategory> = ListState.None,
)

internal enum class Tab(
    @StringRes val headerRes: Int,
    val iconVector: ImageVector,
) {
    EDITOR(R.string.editor_tab, TablerIcons.Pencil),
    PREVIEW(R.string.preview_tab, TablerIcons.Eye),
    CATEGORIES(R.string.categories_tab, TablerIcons.LayoutGrid),
}