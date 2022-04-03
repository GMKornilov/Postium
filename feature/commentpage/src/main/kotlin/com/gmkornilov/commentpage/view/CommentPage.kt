package com.gmkornilov.commentpage.view

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.gmkornilov.commentpage.R
import com.gmkornilov.comments.model.CommentLikeStatus
import com.gmkornilov.comments.model.CommentPreviewData
import com.gmkornilov.design.commons.posts.Comment
import com.gmkornilov.design.components.EmptyStateContainer
import com.gmkornilov.design.components.ErrorStateContainer
import com.gmkornilov.design.modifiers.imePaddingWithBottomBar
import com.gmkornilov.design.theme.PostiumTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import compose.icons.TablerIcons
import compose.icons.tablericons.MessageCircle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CommentPage(
    viewModel: CommentPageViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.container.stateFlow.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var enteredComment by remember { mutableStateOf("") }
    val onCommentChanged = { comment: String -> enteredComment = comment }

    LaunchedEffect(viewModel) {
        viewModel.loadData()

        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is CommentPageSideEffect.ShowSnackbar -> showError(
                    it.message,
                    scope,
                    snackbarHostState
                )
                CommentPageSideEffect.ClearTextField -> enteredComment = ""
            }
        }
    }

    CommentPageWithState(
        state = state,
        commentPageEvents = viewModel,
        enteredComment = enteredComment,
        onCommentChanged = onCommentChanged,
        modifier = modifier
    )
}

private fun showError(
    message: String,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    coroutineScope.launch {
        snackbarHostState.showSnackbar(message = message)
    }
}

@ExperimentalFoundationApi
@Composable
private fun CommentPageWithState(
    state: CommentPageState,
    commentPageEvents: CommentPageEvents,
    enteredComment: String,
    onCommentChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val contentModifier = Modifier
            .weight(1f)
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)

        SwipeRefresh(
            state = rememberSwipeRefreshState(state.isRefreshing),
            onRefresh = { commentPageEvents.reloadData() },
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            when (state.listState) {
                ListState.Loading -> LoadingState(contentModifier.verticalScroll(rememberScrollState()))
                is ListState.Success -> if (state.listState.comments.isEmpty()) {
                    EmptyState(contentModifier.verticalScroll(rememberScrollState()))
                } else {
                    ContentState(
                        comments = state.listState.comments,
                        commentPageEvents = commentPageEvents,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background)
                    )
                }
                is ListState.Error -> ErrorState(contentModifier.verticalScroll(rememberScrollState()))
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)
                .height(IntrinsicSize.Min)
                .imePaddingWithBottomBar(),
        ) {
            BasicTextField(
                value = enteredComment,
                onValueChange = onCommentChanged,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f)
                    .align(Alignment.Top)
                    .fillMaxHeight(),
            ) { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(LightGray, shape = RoundedCornerShape(4.dp))
                        .padding(4.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopStart,
                ) {
                    innerTextField()
                }
            }

            Spacer(Modifier.size(4.dp))

            FloatingActionButton(
                onClick = { commentPageEvents.sendComment(enteredComment) },
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp, end = 4.dp),
            ) {
                when (state.sendCommentState) {
                    SendCommentState.Loading -> CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
                    SendCommentState.None -> Icon(
                        TablerIcons.MessageCircle,
                        null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
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
        errorMessage = stringResource(id = R.string.comments_error),
        modifier = modifier,
    )
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    EmptyStateContainer(
        emptyStateMessage = stringResource(id = R.string.comments_empty),
        modifier = modifier
    )
}

@ExperimentalFoundationApi
@Composable
private fun ContentState(
    comments: List<CommentPreviewData>,
    commentPageEvents: CommentPageEvents,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(comments, { _, comment -> comment.id }) { index, item ->
            val isLast = index == comments.lastIndex

            val bottomPadding = if (isLast) 0.dp else 4.dp

            Comment(
                avatarUrl = item.avatarUrl,
                usernameTitle = item.username,
                comment = item.comment,
                isUpChecked = item.likeStatus.isLiked,
                isDownChecked = item.likeStatus.isDisliked,
                onOpenProfileClicked = { commentPageEvents.openProfile(item) },
                onUpClicked = { commentPageEvents.likeComment(item) },
                onDownClicked = { commentPageEvents.dislikeComment(item) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = bottomPadding)
                    .animateItemPlacement(
                        animationSpec = tween(600)
                    ),
            )
        }
    }
}


@Preview(name = "Preview light", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun PreviewCommentLight() {
    PreviewComments()
}

@Preview(name = "Preview dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewCommentDark() {
    PreviewComments()
}

@Composable
private fun PreviewComments() {
    val loremIpsum = LoremIpsum(50).values.joinToString(" ")
    val comments = listOf(
        CommentPreviewData(
            id = "1",
            comment = "Title",
            userId = "",
            username = "Georgium",
            avatarUrl = null,
            likeStatus = CommentLikeStatus.LIKED,
        ),
        CommentPreviewData(
            id = "2",
            comment = loremIpsum,
            userId = "",
            username = "Georgium",
            avatarUrl = null,
            likeStatus = CommentLikeStatus.DISLIKED,
        ),
    )
    val state = CommentPageState(listState = ListState.Success(comments))
    CommentWithStatePreview(state = state)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CommentWithStatePreview(state: CommentPageState) {
    PostiumTheme {
        CommentPageWithState(
            state = state,
            commentPageEvents = CommentPageEvents,
            enteredComment = "",
            onCommentChanged = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}