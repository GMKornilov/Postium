package com.gmkornilov.mainpage.mainpage

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.commons.posts.PostPreview
import com.gmkornilov.design.data.CornerType
import com.gmkornilov.design.theme.PostiumTheme
import com.gmkornilov.mainpage.R
import com.gmkornilov.mainpage.model.PostPreviewData
import com.gmkornilov.mainpage.model.PostPreviewLikeStatus

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun Mainpage(
    mainPageViewModel: MainPageViewModel,
    modifier: Modifier,
) {
    val state by mainPageViewModel.container.stateFlow.collectAsState()

    LaunchedEffect(mainPageViewModel, state.currentRange) {
        if (state.currentPageState() == PostsState.None) {
            mainPageViewModel.loadAllPosts()
        }
    }

    MainpageWithState(mainPageEvents = mainPageViewModel, state = state, modifier = modifier)
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
private fun MainpageWithState(
    mainPageEvents: MainPageEvents,
    state: MainPageState,
    modifier: Modifier = Modifier,
) {
    val currentRange = state.currentRange

    val postsState = state.currentPageState()

    var menuExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.background(MaterialTheme.colors.background)) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)
        ) {
            Text(
                text = stringResource(id = R.string.main_page_title),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 8.dp)
                    .align(CenterVertically),
            )

            Spacer(Modifier.weight(1f))

            Box(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
                Row(modifier = Modifier.clickable { menuExpanded = !menuExpanded }) {
                    Text(
                        stringResource(id = currentRange.titleRes),
                        modifier = Modifier.align(CenterVertically)
                    )

                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            null,
                            modifier = Modifier.rotate(if (menuExpanded) 180f else 360f),
                            tint = MaterialTheme.colors.onSurface,
                        )
                    }
                }
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    PostTimeRange.values().forEach { postTimeRange ->
                        DropdownMenuItem(
                            onClick = {
                                mainPageEvents.selectTimeRange(postTimeRange)
                                menuExpanded = false
                            }
                        ) {
                            Text(text = stringResource(id = postTimeRange.titleRes))
                        }
                    }
                }
            }
        }

        when (postsState) {
            is PostsState.Loading -> LoadingState()
            is PostsState.Error -> ErrorState()
            is PostsState.Success -> SuccessState(
                mainPageEvents = mainPageEvents,
                posts = postsState.items
            )
            else -> {}
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
        CircularProgressIndicator(modifier = Modifier.align(Center))
    }
}

@Composable
private fun ErrorState(modifier: Modifier = Modifier) {

}

@ExperimentalFoundationApi
@Composable
private fun SuccessState(
    mainPageEvents: MainPageEvents,
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

            var isBookmarked by remember { mutableStateOf(false) }

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
                userName = "",
                avatarUrl = null,
                isUpChecked = item.likeStatus.isLiked,
                isDownChecked = item.likeStatus.isDisliked,
                isBookmarkChecked = false,
                cornerType = cornerType,
                modifier = Modifier.padding(bottom = bottomPadding),
                onCardClick = {},
                upClicked = { mainPageEvents.likePost(item) },
                downClicked = { mainPageEvents.dislikePost(item) },
                boolmarkClicked = { isBookmarked = !isBookmarked }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_MASK,
)
@Composable
fun SuccessPreview() {
    PostiumTheme {
        val posts = listOf(
            PostPreviewData("1", "First title", PostPreviewLikeStatus.NONE),
            PostPreviewData("2", "Second title", PostPreviewLikeStatus.NONE),
            PostPreviewData("3", "Third title", PostPreviewLikeStatus.NONE),
        )

        val state = MainPageState(
            allTimeState = PostsState.Success(posts),
            currentRange = PostTimeRange.ALL_TIME
        )

        MainpageWithState(
            mainPageEvents = MainPageEvents.MOCK,
            state = state,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_MASK,
)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun SuccessPreviewLong() {
    PostiumTheme {
        val posts = listOf(
            PostPreviewData("1", "First title", PostPreviewLikeStatus.NONE),
            PostPreviewData("2", "Second title", PostPreviewLikeStatus.LIKED),
            PostPreviewData("3", "Third title", PostPreviewLikeStatus.DISLIKED),
            PostPreviewData("4", "Third title", PostPreviewLikeStatus.NONE),
            PostPreviewData("5", "Third title", PostPreviewLikeStatus.NONE),
            PostPreviewData("6", "Third title", PostPreviewLikeStatus.NONE),
            PostPreviewData("7", "Third title", PostPreviewLikeStatus.NONE),
            PostPreviewData("8", "Third title", PostPreviewLikeStatus.NONE),
            PostPreviewData("9", "Third title", PostPreviewLikeStatus.NONE),
            PostPreviewData("10", "Third title", PostPreviewLikeStatus.NONE),
            PostPreviewData("11", "Third title", PostPreviewLikeStatus.NONE),
        )


        val state = MainPageState(
            allTimeState = PostsState.Success(posts),
            currentRange = PostTimeRange.ALL_TIME
        )

        MainpageWithState(
            mainPageEvents = MainPageEvents.MOCK,
            state = state,
            modifier = Modifier.fillMaxSize()
        )
    }
}