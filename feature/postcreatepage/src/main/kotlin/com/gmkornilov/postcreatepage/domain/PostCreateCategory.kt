package com.gmkornilov.postcreatepage.domain

import com.gmkornilov.categories.model.Category

data class PostCreateCategory(
    val category: Category,
    val isMarked: Boolean,
)