package com.gmkornilov.postpage.view

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.commons.buttons.BookmarkButton
import com.gmkornilov.design.commons.buttons.DislikeButton
import com.gmkornilov.design.commons.buttons.LikeButton
import com.gmkornilov.design.components.ErrorStateContainer
import com.gmkornilov.design.components.UserAvatar
import com.gmkornilov.design.modifiers.bottomBorder
import com.gmkornilov.design.modifiers.topBorder
import com.gmkornilov.design.theme.PostiumTheme
import com.gmkornilov.post.model.PostBookmarkStatus
import com.gmkornilov.post.model.PostLikeStatus
import com.gmkornilov.postpage.R
import com.gmkornilov.postpage.brick_navigation.PostPageArgument
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material.MaterialRichText

@Composable
internal fun Postpage(
    viewModel: PostpageViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.container.stateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        if (state.contentState is ContentState.None) {
            viewModel.loadContent()
        }
    }

    PostpageWithState(state = state, postpageEvents = viewModel, modifier = modifier)
}


@Composable
private fun PostpageWithState(
    state: PostpageState,
    postpageEvents: PostpageEvents,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.background(MaterialTheme.colors.surface)) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(state.isRefresh),
            onRefresh = { postpageEvents.refreshData() },
            modifier = Modifier.weight(1f),
        ) {
            LazyColumn {
                item {
                    PostHeader(
                        title = state.argument.title,
                        username = state.argument.username,
                        avatarUrl = state.argument.avatarUrl,
                        postpageEvents = postpageEvents,
                        modifier = Modifier.bottomBorder(1.dp, 16.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.size(8.dp))
                }

                item {
                    when (state.contentState) {
                        is ContentState.Error -> ContentError(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .weight(1f)
                                .topBorder(1.dp, 16.dp)
                                .verticalScroll(rememberScrollState())
                        )
                        is ContentState.Loading -> ContentLoading(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .weight(1f)
                                .topBorder(1.dp, 16.dp)
                                .verticalScroll(rememberScrollState())
                        )
                        is ContentState.Success -> ContentSuccess(
                            contentState = state.contentState,
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .topBorder(1.dp, 16.dp)
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                        )
                        ContentState.None -> {}
                    }
                }
            }
        }

        Divider()

        PostFooter(
            isUpChecked = state.argument.likeStatus.isLiked,
            isDownChecked = state.argument.likeStatus.isDisliked,
            isBookmarkChecked = state.argument.bookmarkStatus.isBookmarked,
            onUpClicked = { postpageEvents.likePost() },
            onDownClicked = { postpageEvents.dislikePost() },
            onBookmarkClicked = { postpageEvents.bookmarkPost() },
            onOpenCommentsClicked = postpageEvents::openComments,
            modifier = Modifier.height(48.dp)
        )
    }
}

@Composable
private fun ContentLoading(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        CircularProgressIndicator(modifier = Modifier.align(Center))
    }
}

@Composable
private fun ContentError(
    modifier: Modifier = Modifier,
) {
    ErrorStateContainer(
        errorMessage = stringResource(R.string.post_content_error),
        modifier = modifier
    )
}

@Composable
private fun ContentSuccess(
    contentState: ContentState.Success,
    modifier: Modifier = Modifier,
) {
    if (LocalInspectionMode.current) {
        Text(contentState.content, color = MaterialTheme.colors.onSurface, modifier = modifier)
    } else {
        MaterialRichText(modifier = modifier) {
            Markdown(content = contentState.content)
        }
    }
}

@Composable
private fun PostHeader(
    title: String,
    username: String,
    avatarUrl: String?,
    postpageEvents: PostpageEvents,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .height(48.dp)
                .clickable { postpageEvents.openProfile() }) {
            avatarUrl?.let {
                UserAvatar(
                    avatarUrl = avatarUrl,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            val startPadding = if (avatarUrl == null) 16.dp else 12.dp
            Text(
                stringResource(R.string.by_title, username),
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(start = startPadding)
            )
        }

        Text(
            title,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun PostFooter(
    isUpChecked: Boolean,
    isDownChecked: Boolean,
    isBookmarkChecked: Boolean,
    onUpClicked: (Boolean) -> Unit,
    onDownClicked: (Boolean) -> Unit,
    onBookmarkClicked: (Boolean) -> Unit,
    onOpenCommentsClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onOpenCommentsClicked) {
            Icon(
                imageVector = Icons.Outlined.Chat,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        LikeButton(isChecked = isUpChecked, onCheckedChange = onUpClicked)

        DislikeButton(isChecked = isDownChecked, onCheckedChange = onDownClicked)

        BookmarkButton(isChecked = isBookmarkChecked, onCheckedChange = onBookmarkClicked)
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
    name = "Loading preview light",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun LoadingPreviewLight() {
    LoadingPreview()
}

@Preview(
    name = "Loading preview dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun LoadingPreviewDark() {
    LoadingPreview()
}

@Preview(
    name = "Short preview"
)
@Composable
private fun ShortPreview() {
    val argument = PostPageArgument(
        title = "test title",
        username = "test username",
        avatarUrl = "",
        id = "",
        userId = "",
        likeStatus = PostLikeStatus.LIKED,
        bookmarkStatus = PostBookmarkStatus.BOOKMARKED
    )
    val content = "Short content"

    val state = PostpageState(argument, ContentState.Success(content))

    PreviewWithState(state = state)
}

@Composable
private fun SuccessPreview() {
    val argument = PostPageArgument(
        title = "test title",
        username = "test username",
        avatarUrl = "",
        id = "",
        userId = "",
        likeStatus = PostLikeStatus.LIKED,
        bookmarkStatus = PostBookmarkStatus.BOOKMARKED
    )
    val content = LoremIpsum().values.joinToString()

    val state = PostpageState(argument, ContentState.Success(content))

    PreviewWithState(state = state)
}

@Composable
private fun LoadingPreview() {
    val argument = PostPageArgument(
        title = "test title",
        username = "test username",
        avatarUrl = "",
        userId = "",
        id = "",
        likeStatus = PostLikeStatus.DISLIKED,
        bookmarkStatus = PostBookmarkStatus.NOT_BOOKMARKED
    )

    val state = PostpageState(argument, ContentState.Loading)

    PreviewWithState(state = state)
}

@Composable
private fun PreviewWithState(state: PostpageState) {
    PostiumTheme {
        PostpageWithState(
            state = state,
            postpageEvents = PostpageEvents.MOCK,
            modifier = Modifier.fillMaxSize()
        )
    }
}