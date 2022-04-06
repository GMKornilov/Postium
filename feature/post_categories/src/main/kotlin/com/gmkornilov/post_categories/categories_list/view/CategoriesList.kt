package com.gmkornilov.post_categories.categories_list.view

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmkornilov.categories.model.Category
import com.gmkornilov.design.components.Chip
import com.gmkornilov.design.components.EmptyStateContainer
import com.gmkornilov.design.components.ErrorStateContainer
import com.gmkornilov.design.theme.PostiumTheme
import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.post_categories.R
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
internal fun CategoriesList(
    viewModel: CategoriesViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.container.stateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.loadData()
    }

    CategoriesWithState(state = state, categoriesEvents = viewModel, modifier = modifier)
}

@Composable
private fun CategoriesWithState(
    state: CategoriesState,
    categoriesEvents: CategoriesEvents,
    modifier: Modifier = Modifier,
) {
    SwipeRefresh(
        rememberSwipeRefreshState(state.isRefreshing),
        onRefresh = { categoriesEvents.refreshData() }
    ) {
        val contentModifier = modifier.verticalScroll(rememberScrollState())
        when (state.listState) {
            is ListState.Error -> ErrorState(contentModifier)
            ListState.Loading -> LoadingState(contentModifier)
            is ListState.Success -> if (state.listState.contents.isEmpty()) {
                EmptyState(contentModifier)
            } else {
                SuccessState(
                    state = state.listState.contents,
                    categoriesEvents = categoriesEvents,
                    modifier = contentModifier
                )
            }
            ListState.None -> {}
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(MaterialTheme.colors.surface)
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun ErrorState(modifier: Modifier = Modifier) {
    ErrorStateContainer(
        errorMessage = stringResource(id = R.string.categories_error),
        modifier = modifier,
    )
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    EmptyStateContainer(
        emptyStateMessage = stringResource(id = R.string.categories_empty),
        modifier = modifier
    )
}

@Composable
private fun SuccessState(
    state: List<Category>,
    categoriesEvents: CategoriesEvents,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(MaterialTheme.colors.background),
    ) {
        Text(
            stringResource(R.string.categories_title),
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(MaterialTheme.colors.primary)
                .padding(top = 8.dp, bottom = 8.dp)
        )

        FlowRow(
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 12.dp,
            mainAxisAlignment = FlowMainAxisAlignment.Start,
            modifier = Modifier.padding(top = 12.dp, start = 8.dp, end = 8.dp)
        ) {
            state.forEach { category ->
                key(category.id) {
                    Chip(
                        text = category.name,
                        onCLick = { categoriesEvents.openCategory(category) }
                    )
                }
            }
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewSuccessDark() {
    PreviewSuccess()
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun PreviewSuccessLight() {
    PreviewSuccess()
}

@Composable
private fun PreviewSuccess() {
    val baseTexts = listOf("test", "sport", "it", "beauty")
    val texts = MutableList(60) { baseTexts[it % baseTexts.size] }

    val categories = texts.mapIndexed { index, text ->
        Category(index.toString(), text)
    }

    val state = CategoriesState(ListState.Success(categories))
    PreviewWithState(state = state)
}

@Composable
private fun PreviewWithState(state: CategoriesState) {
    PostiumTheme {
        CategoriesWithState(
            state = state,
            categoriesEvents = CategoriesEvents,
            modifier = Modifier.fillMaxSize()
        )
    }
}