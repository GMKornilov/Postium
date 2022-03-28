package com.gmkornilov.postpage.postpage

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.commons.buttons.BookmarkButton
import com.gmkornilov.design.commons.buttons.DislikeButton
import com.gmkornilov.design.commons.buttons.LikeButton
import com.gmkornilov.design.components.ScrollableColumn
import com.gmkornilov.design.components.UserAvatar
import com.gmkornilov.design.theme.PostiumTheme
import com.gmkornilov.postpage.R
import com.gmkornilov.postpage.brick_navigation.PostPageArgument

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
        ScrollableColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            PostHeader(
                title = state.argument.title,
                username = state.argument.username,
                avatarUrl = state.argument.avatarUrl,
            )

            Divider()

            when (state.contentState) {
                is ContentState.Error -> ContentError(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
                ContentState.Loading -> ContentLoading(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
                is ContentState.Success -> ContentSuccess(
                    contentState = state.contentState,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
                )
                ContentState.None -> {}
            }
        }

        Divider()

        PostFooter(
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

}

@Composable
private fun ContentSuccess(
    contentState: ContentState.Success,
    modifier: Modifier = Modifier,
) {
    Text(
        contentState.content,
        color = MaterialTheme.colors.onSurface,
        modifier = modifier,
    )
}

@Composable
private fun PostHeader(
    title: String,
    username: String?,
    avatarUrl: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.height(48.dp)) {
            avatarUrl?.let {
                UserAvatar(
                    avatarUrl = avatarUrl,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            username?.let {
                val startPadding = if (avatarUrl == null) 16.dp else 12.dp
                Text(
                    stringResource(R.string.by_title, username),
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(start = startPadding)
                )
            }
        }

        Text(
            title,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(top = 8.dp)
                .align(CenterHorizontally)
        )
    }
}

@Composable
private fun PostFooter(
    onUpClicked: (Boolean) -> Unit,
    onDownClicked: (Boolean) -> Unit,
    onBookmarkClicked: (Boolean) -> Unit,
    onOpenCommentsClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        val isUpChecked by remember { mutableStateOf(true) }
        val isDownChecked by remember { mutableStateOf(true) }
        val isBookmarkChecked by remember { mutableStateOf(true) }

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

@Composable
private fun SuccessPreview() {
    val argument = PostPageArgument(
        title = "test title",
        username = "test username",
        avatarUrl = "",
        id = "",
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
        id = "",
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