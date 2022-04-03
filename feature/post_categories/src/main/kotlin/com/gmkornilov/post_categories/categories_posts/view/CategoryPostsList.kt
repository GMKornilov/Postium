package com.gmkornilov.post_categories.categories_posts.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmkornilov.post_categories.R
import com.gmkornilov.post_list.view.LocalEmptyStateMessage
import com.gmkornilov.post_list.view.LocalErrorStateMessage
import com.gmkornilov.post_list.view.PostList

@Composable
internal fun CategoryPostsList(
    viewModel: CategoryPostsViewModel,
    modifier: Modifier = Modifier,
) {
    val listViewModel = viewModel.listViewModel

    CompositionLocalProvider(
        LocalErrorStateMessage provides stringResource(R.string.post_categories_error),
        LocalEmptyStateMessage provides stringResource(R.string.post_categories_empty),
    ) {
        Column(modifier = modifier.background(MaterialTheme.colors.background)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface)
                    .padding(top = 8.dp, bottom = 8.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = viewModel.categoryName,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .align(Alignment.CenterVertically),
                )
            }

            PostList(viewModel = listViewModel, modifier = modifier)
        }
    }
}
