package com.gmkornilov.post_categories.categories_posts.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.commons.posts.PostPreview
import com.gmkornilov.design.components.EmptyStateContainer
import com.gmkornilov.design.components.ErrorStateContainer
import com.gmkornilov.design.data.CornerType
import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.letIf
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.post_categories.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun CategoryPostsList(
    viewModel: CategoryPostsViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.container.stateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.loadAllPosts()
    }

    CategoryPostsWithState(state = state, categoryPostsEvents = viewModel, modifier = modifier)
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
private fun CategoryPostsWithState(
    state: CategoryPostsState,
    categoryPostsEvents: CategoryPostsEvents,
    modifier: Modifier = Modifier,
) {
    val isRefreshing = state.isRefreshing

    Column(modifier = modifier.background(MaterialTheme.colors.background)) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)
                .padding(top = 8.dp, bottom = 8.dp)
                .height(48.dp)
        ) {
            Text(
                text = state.categoryName,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .align(Alignment.CenterVertically),
            )
        }

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { categoryPostsEvents.refreshData() },
            modifier = Modifier.fillMaxSize(),
        ) {
            when (state.listState) {
                is ListState.Loading -> LoadingState(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(
                            rememberScrollState()
                        )
                )
                is ListState.Error -> ErrorState(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                )
                is ListState.Success -> if (state.listState.contents.isNotEmpty()) {
                    SuccessState(
                        categoryPostsEvents = categoryPostsEvents,
                        posts = state.listState.contents
                    )
                } else {
                    EmptyState()
                }
                else -> {}
            }
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
        errorMessage = stringResource(id = R.string.post_categories_error),
        modifier = modifier,
    )
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    EmptyStateContainer(
        emptyStateMessage = stringResource(id = R.string.post_categories_empty),
        modifier = modifier
    )
}

@ExperimentalFoundationApi
@Composable
private fun SuccessState(
    categoryPostsEvents: CategoryPostsEvents,
    posts: List<PostPreviewData>,
    modifier: Modifier = Modifier
) {
    val state = rememberLazyListState()

    LazyColumn(state = state, modifier = modifier) {
        itemsIndexed(posts, key = { _, post -> post.id }) { index, item ->
            val isFirst = index == 0
            val isLast = index == posts.lastIndex

            val cornerType: CornerType
            val bottomPadding: Dp

            when {
                isFirst -> {
                    cornerType = CornerType.BOTTOM
                    bottomPadding = 4.dp
                }
                isLast -> {
                    cornerType = CornerType.ALL
                    bottomPadding = 0.dp
                }
                else -> {
                    cornerType = CornerType.ALL
                    bottomPadding = 4.dp
                }
            }

            PostPreview(
                title = item.title,
                userName = item.username,
                avatarUrl = item.avatarUrl.letIf(!item.avatarUrl.isNullOrEmpty()) { it },
                isUpChecked = item.likeStatus.isLiked,
                isDownChecked = item.likeStatus.isDisliked,
                isBookmarkChecked = item.bookmarkStatus.isBookmarked,
                cornerType = cornerType,
                modifier = Modifier.padding(bottom = bottomPadding),
                onCardClick = { categoryPostsEvents.openPost(item) },
                upClicked = { categoryPostsEvents.likePost(item) },
                downClicked = { categoryPostsEvents.dislikePost(item) },
                boolmarkClicked = { categoryPostsEvents.bookmarkPost(item) },
                userProfileClicked = { categoryPostsEvents.openProfile(item) },
            )
        }
    }
}