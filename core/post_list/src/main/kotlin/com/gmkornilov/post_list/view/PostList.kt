package com.gmkornilov.post_list.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.commons.posts.PostPreview
import com.gmkornilov.design.components.EmptyStateContainer
import com.gmkornilov.design.components.ErrorStateContainer
import com.gmkornilov.design.data.CornerType
import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.letIf
import com.gmkornilov.post.model.PostPreviewData
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

val LocalEmptyStateMessage = compositionLocalOf { "" }
val LocalErrorStateMessage = compositionLocalOf { "" }


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun PostList(
    viewModel: PostListViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.container.stateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.loadAllPosts()
    }

    CategoryPostsWithState(state = state, postsListEvents = viewModel, modifier = modifier)
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
private fun CategoryPostsWithState(
    state: PostListState,
    postsListEvents: PostListEvents,
    modifier: Modifier = Modifier,
) {
    val isRefreshing = state.isRefreshing
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { postsListEvents.refreshData() },
    ) {
        when (state.listState) {
            is ListState.Loading -> LoadingState(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
            )
            is ListState.Error -> ErrorState(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )
            is ListState.Success -> if (state.listState.contents.isNotEmpty()) {
                SuccessState(
                    postsListEvents = postsListEvents,
                    posts = state.listState.contents,
                    modifier = modifier
                )
            } else {
                EmptyState(modifier.verticalScroll(rememberScrollState()))
            }
            else -> {}
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
        errorMessage = LocalErrorStateMessage.current,
        modifier = modifier,
    )
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    EmptyStateContainer(
        emptyStateMessage = LocalEmptyStateMessage.current,
        modifier = modifier
    )
}

@ExperimentalFoundationApi
@Composable
private fun SuccessState(
    postsListEvents: PostListEvents,
    posts: List<PostPreviewData>,
    modifier: Modifier = Modifier,
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
                onCardClick = { postsListEvents.openPost(item) },
                upClicked = { postsListEvents.likePost(item) },
                downClicked = { postsListEvents.dislikePost(item) },
                boolmarkClicked = { postsListEvents.bookmarkPost(item) },
                userProfileClicked = { postsListEvents.openProfile(item) },
                playlistClicked = { postsListEvents.addToPlaylists(item) }
            )
        }
    }
}