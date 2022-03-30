package com.gmkornilov.postcreatepage.view

import androidx.annotation.StringRes
import com.gmkornilov.postcreatepage.R

internal data class PostCreateState (
    val isLoading: Boolean = false,
)

enum class Tab(
    @StringRes val headerRes: Int,
) {
    EDITOR(R.string.editor_tab),
    PREVIEW(R.string.preview_tab),
}