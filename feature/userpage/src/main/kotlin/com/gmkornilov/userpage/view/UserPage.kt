package com.gmkornilov.userpage.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.components.LocalAvatarSize
import com.gmkornilov.design.components.UserAvatar
import com.gmkornilov.design.theme.PostiumTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun UserPage(
    viewModel: UserPageViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.container.stateFlow.collectAsState()

    UserPageWithState(state = state, userPageEvents = viewModel, modifier = modifier)
}

@ExperimentalPagerApi
@Composable
private fun UserPageWithState(
    state: UserPageState,
    userPageEvents: UserPageEvents,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.background(MaterialTheme.colors.surface)) {
        UserHeader(
            state = state,
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
    state: UserPageState,
    userPageEvents: UserPageEvents,
    modifier: Modifier = Modifier,
) {
    val avatarUrl: String? = ""
    val username: String? = "test"
    Row(verticalAlignment = Alignment.Bottom, modifier = modifier) {
        avatarUrl?.let {
            CompositionLocalProvider(LocalAvatarSize provides 64.dp) {
                UserAvatar(
                    avatarUrl = avatarUrl,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        username?.let {
            val startPadding = if (avatarUrl == null) 16.dp else 12.dp
            Text(
                username,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(start = startPadding),
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
private fun UserContent(
    state: UserPageState,
    userPageEvents: UserPageEvents,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState()
    
    val pages = listOf("POSTS", "BOOKMARKS")

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }
        ) {
            pages.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = {  },
                )
            }
        }

        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            Text(text = pages[page])
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun PreviewSuccess() {
    val state = UserPageState()

    PreviewWithState(state = state, modifier = Modifier.fillMaxSize())
}

@ExperimentalPagerApi
@Composable
private fun PreviewWithState(
    state: UserPageState,
    modifier: Modifier = Modifier,
) {
    PostiumTheme {
        UserPageWithState(state = state, userPageEvents = UserPageEvents.MOCK, modifier = modifier)
    }
}