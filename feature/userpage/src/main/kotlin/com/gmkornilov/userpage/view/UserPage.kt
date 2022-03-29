package com.gmkornilov.userpage.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.commons.posts.PostPreview
import com.gmkornilov.design.components.LocalAvatarSize
import com.gmkornilov.design.components.UserAvatar
import com.gmkornilov.design.data.CornerType
import com.gmkornilov.design.theme.PostiumTheme
import com.gmkornilov.letIf
import com.gmkornilov.post.model.PostPreviewData
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
internal fun UserPage(
    viewModel: UserPageViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.container.stateFlow.collectAsState()

    UserPageWithState(state = state, userPageEvents = viewModel, modifier = modifier)
}

@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
private fun UserPageWithState(
    state: UserPageState,
    userPageEvents: UserPageEvents,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.background(MaterialTheme.colors.surface)) {
        UserHeader(
            state = state.headerState,
            userPageEvents = userPageEvents,
            modifier = Modifier.padding(top = 16.dp)
        )

        UserContent(
            state = state,
            userPageEvents = userPageEvents,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun UserHeader(
    state: HeaderState,
    userPageEvents: UserPageEvents,
    modifier: Modifier = Modifier,
) {
    Row(verticalAlignment = Alignment.Bottom, modifier = modifier) {
        state.avatarUrl?.let {
            CompositionLocalProvider(LocalAvatarSize provides 64.dp) {
                UserAvatar(
                    avatarUrl = state.avatarUrl,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        val startPadding = if (state.avatarUrl == null) 16.dp else 12.dp
        Text(
            state.username,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(start = startPadding),
        )
    }
}

@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
private fun UserContent(
    state: UserPageState,
    userPageEvents: UserPageEvents,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState()

    val pages = Tab.values()

    LaunchedEffect(pagerState.currentPage) {
        val tab = pages[pagerState.currentPage]
        userPageEvents.tabSelected(tab)
    }

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }
        ) {
            pages.forEachIndexed { index, tab ->
                Tab(
                    text = { Text(stringResource(tab.headerRes).toUpperCase(Locale.current)) },
                    selected = pagerState.currentPage == index,
                    onClick = { },
                )
            }
        }

        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageNumber ->
            val contentModifier = Modifier.fillMaxSize()
            val tab = pages[pageNumber]
            when (val tabState = state.tabStates.getValue(tab)) {
                is TabState.Error -> ErrorState()
                TabState.Loading -> LoadingState()
                is TabState.Success -> if (tabState.posts.isNotEmpty()) {
                    SuccessState(
                        posts = tabState.posts,
                        userPageEvents = userPageEvents,
                        modifier = contentModifier
                    )
                } else {
                    EmptyState()
                }
                TabState.None -> {}
            }
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun ErrorState(modifier: Modifier = Modifier) {

}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {

}

@ExperimentalFoundationApi
@Composable
private fun SuccessState(
    userPageEvents: UserPageEvents,
    posts: List<PostPreviewData>,
    modifier: Modifier = Modifier
) {
    val state = rememberLazyListState()

    LazyColumn(state = state, modifier = modifier.background(MaterialTheme.colors.background)) {
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
                onCardClick = { userPageEvents.openPost(item) },
                upClicked = { userPageEvents.likePost(item) },
                downClicked = { userPageEvents.dislikePost(item) },
                boolmarkClicked = { userPageEvents.bookmarkPost(item) },
//                userProfileClicked = { userPageEvents.openProfile(item) },
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Preview
@Composable
private fun PreviewSuccess() {
    val headerState = HeaderState(username = "test", avatarUrl = "")

    val state = UserPageState(headerState)

    PreviewWithState(state = state, modifier = Modifier.fillMaxSize())
}

@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
private fun PreviewWithState(
    state: UserPageState,
    modifier: Modifier = Modifier,
) {
    PostiumTheme {
        UserPageWithState(state = state, userPageEvents = UserPageEvents, modifier = modifier)
    }
}