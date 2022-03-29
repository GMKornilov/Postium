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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.*
import com.gmkornilov.design.commons.posts.PostPreview
import com.gmkornilov.design.data.CornerType
import com.gmkornilov.design.theme.PostiumTheme
import com.gmkornilov.letIf
import com.gmkornilov.mainpage.R
import com.gmkornilov.post.model.PostBookmarkStatus
import com.gmkornilov.post.model.PostLikeStatus
import com.gmkornilov.post.model.PostPreviewData

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
                .padding(top = 8.dp, bottom = 8.dp)
                .height(48.dp)
        ) {
            Text(
                text = stringResource(id = R.string.main_page_title),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .align(Alignment.CenterVertically),
            )

            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp)
            ) {
                Row(modifier = Modifier.clickable { menuExpanded = !menuExpanded }) {
                    Text(
                        stringResource(id = currentRange.titleRes),
                        color = MaterialTheme.colors.onSurface,
                    )

                    Icon(
                        Icons.Filled.ArrowDropDown,
                        null,
                        modifier = Modifier.rotate(if (menuExpanded) 180f else 360f),
                        tint = MaterialTheme.colors.onSurface,
                    )
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
            is PostsState.Success -> if(postsState.items.isNotEmpty()) {
                SuccessState(
                    mainPageEvents = mainPageEvents,
                    posts = postsState.items
                )
            } else {
                EmptyState()
            }
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
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun ErrorState(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error_animation))

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(16.dp),
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(id = R.string.load_posts_error),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.onSurface,
        )
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.not_found))

    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value = MaterialTheme.colors.onSurface.toArgb(),
            keyPath = arrayOf("**"),
        )
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(16.dp),
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            dynamicProperties = dynamicProperties,
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(id = R.string.posts_not_found),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.onSurface,
        )
    }
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
                userName = item.username.orEmpty(),
                avatarUrl = item.avatarUrl.letIf(!item.avatarUrl.isNullOrEmpty()) { it },
                isUpChecked = item.likeStatus.isLiked,
                isDownChecked = item.likeStatus.isDisliked,
                isBookmarkChecked = item.bookmarkStatus.isBookmarked,
                cornerType = cornerType,
                modifier = Modifier.padding(bottom = bottomPadding),
                onCardClick = { mainPageEvents.openPost(item) },
                upClicked = { mainPageEvents.likePost(item) },
                downClicked = { mainPageEvents.dislikePost(item) },
                boolmarkClicked = { mainPageEvents.bookmarkPost(item) },
                userProfileClicked = { mainPageEvents.openProfile(item) },
            )
        }
    }
}

@Preview(
    name = "Preview light",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun SuccessPreviewLight() {
    SuccessPreview()
}

@Preview(
    name = "Preview dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun SuccessPreviewDark() {
    SuccessPreview()
}

@Preview(
    name = "Preview error"
)
@Composable
private fun ErrorPreview() {
    PostiumTheme {
        ErrorState()
    }
}

@Preview(
    name = "Preview not found"
)
@Composable
private fun EmptyPreview() {
    PostiumTheme {
        EmptyState()
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
private fun SuccessPreview() {
    PostiumTheme {
        val posts = listOf(
            PostPreviewData(
                "1",
                "First title",
                "test",
                null,
                PostLikeStatus.NONE,
                PostBookmarkStatus.BOOKMARKED
            ),
            PostPreviewData(
                "2",
                "Second title",
                "test",
                null,
                PostLikeStatus.LIKED,
                PostBookmarkStatus.BOOKMARKED
            ),
            PostPreviewData(
                "3",
                "Third title",
                "test",
                null,
                PostLikeStatus.DISLIKED,
                PostBookmarkStatus.NOT_BOOKMARKED
            ),
            PostPreviewData(
                "4",
                "Third title",
                "test",
                null,
                PostLikeStatus.NONE,
                PostBookmarkStatus.NOT_BOOKMARKED
            ),
            PostPreviewData(
                "5",
                "Third title",
                "test",
                null,
                PostLikeStatus.NONE,
                PostBookmarkStatus.NOT_BOOKMARKED
            ),
            PostPreviewData(
                "6",
                "Third title",
                "test",
                null,
                PostLikeStatus.NONE,
                PostBookmarkStatus.NOT_BOOKMARKED
            ),
            PostPreviewData(
                "7",
                "Third title",
                "test",
                null,
                PostLikeStatus.NONE,
                PostBookmarkStatus.NOT_BOOKMARKED
            ),
            PostPreviewData(
                "8",
                "Third title",
                "test",
                null,
                PostLikeStatus.NONE,
                PostBookmarkStatus.NOT_BOOKMARKED
            ),
            PostPreviewData(
                "9",
                "Third title",
                "test",
                null,
                PostLikeStatus.NONE,
                PostBookmarkStatus.NOT_BOOKMARKED
            ),
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