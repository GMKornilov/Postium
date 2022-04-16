package com.gmkornilov.userpage.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.components.EmptyStateContainer
import com.gmkornilov.design.components.ErrorStateContainer
import com.gmkornilov.design.components.LocalAvatarSize
import com.gmkornilov.design.components.UserAvatar
import com.gmkornilov.design.theme.PostiumTheme
import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.post.model.PostPreviewData
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import compose.icons.TablerIcons
import compose.icons.tablericons.Plus
import kotlinx.coroutines.launch

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
    Box(modifier = modifier.background(MaterialTheme.colors.background)) {
        Column(modifier = modifier.fillMaxSize()) {
            UserHeader(
                state = state.headerState,
                userPageEvents = userPageEvents,
            )

            UserContent(
                state = state,
                userPageEvents = userPageEvents,
            )
        }

        if (state.createPostButtonVisible) {
            FloatingActionButton(
                onClick = { userPageEvents.createPost() },
                backgroundColor = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp)
            ) {
                Icon(TablerIcons.Plus, null)
            }
        }
    }
}

@Composable
private fun UserHeader(
    state: HeaderState,
    userPageEvents: UserPageEvents,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(state) {
        if (state.needLoading) {
            userPageEvents.loadHeader()
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(top = 12.dp, bottom = 12.dp)
    ) {
        state.avatarUrl?.let {
            CompositionLocalProvider(LocalAvatarSize provides 64.dp) {
                UserAvatar(
                    avatarUrl = state.avatarUrl,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .placeholder(
                            visible = state.needLoading,
                            highlight = PlaceholderHighlight.shimmer(),
                        )
                )
            }
        }

        val startPadding = if (state.avatarUrl == null) 16.dp else 12.dp
        Text(
            state.username,
            color = MaterialTheme.colors.onSecondary,
            style = MaterialTheme.typography.h5,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(start = startPadding, end = 16.dp, bottom = 8.dp)
                .placeholder(
                    visible = state.needLoading,
                    highlight = PlaceholderHighlight.shimmer(),
                ),
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
    val scope = rememberCoroutineScope()

    val pages = Tab.values()

    LaunchedEffect(pagerState.currentPage) {
        val tab = pages[pagerState.currentPage]
        userPageEvents.tabSelected(tab)
    }

    Column(modifier = modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            },
            backgroundColor = MaterialTheme.colors.secondary,
            edgePadding = 16.dp
        ) {
            pages.forEachIndexed { index, tab ->
                LeadingIconTab(
                    text = { Text(stringResource(tab.headerRes).toUpperCase(Locale.current)) },
                    icon = { Icon(tab.iconVector, null) },
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
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
            SwipeRefresh(
                state = rememberSwipeRefreshState(state.isRefresh),
                onRefresh = { userPageEvents.refreshData() }
            ) {
                when (val tabState = state.tabStates.getValue(tab)) {
                    is ListState.Error -> ErrorState(
                        tab,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(
                                rememberScrollState()
                            )
                    )
                    ListState.Loading -> LoadingState(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    )
                    is ListState.Success -> if (tabState.contents.isNotEmpty()) {
                        SuccessState(
                            tabListItem = tabState.contents,
                            modifier = contentModifier,
                        )
                    } else {
                        EmptyState(
                            tab,
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        )
                    }
                    ListState.None -> {}
                }
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
private fun ErrorState(tab: Tab, modifier: Modifier = Modifier) {
    ErrorStateContainer(errorMessage = stringResource(tab.errorRes), modifier = modifier)
}

@Composable
private fun EmptyState(tab: Tab, modifier: Modifier = Modifier) {
    EmptyStateContainer(emptyStateMessage = stringResource(tab.emptyRes), modifier = modifier)
}

@ExperimentalFoundationApi
@Composable
private fun SuccessState(
    tabListItem: List<TabListItem>,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()

    LazyColumn(state = state, modifier = modifier.background(MaterialTheme.colors.background)) {
        items(tabListItem, key = { tabListItem: TabListItem -> tabListItem.id }) { item ->
            item.Layout(Modifier)
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Preview
@Composable
private fun PreviewSuccess() {
    val headerState = HeaderState(username = "очень очень очень очень очень", avatarUrl = "")

    val state = UserPageState(headerState, createPostButtonVisible = true)

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