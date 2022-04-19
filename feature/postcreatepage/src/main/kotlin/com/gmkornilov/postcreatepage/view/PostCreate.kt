package com.gmkornilov.postcreatepage.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.components.ErrorStateContainer
import com.gmkornilov.design.components.ScrollableColumn
import com.gmkornilov.design.components.SelectableChip
import com.gmkornilov.design.modifiers.bottomBorder
import com.gmkornilov.design.modifiers.imePaddingWithBottomBar
import com.gmkornilov.design.modifiers.topBorder
import com.gmkornilov.design.theme.PostiumTheme
import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.postcreatepage.R
import com.gmkornilov.postcreatepage.domain.PostCreateCategory
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.*
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material.MaterialRichText
import compose.icons.TablerIcons
import compose.icons.tablericons.Send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(InternalCoroutinesApi::class, ExperimentalPagerApi::class)
@Composable
internal fun PostCreate(
    viewModel: PostCreateViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.container.stateFlow.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    var enteredTitle by remember { mutableStateOf("") }
    var enteredContent by remember { mutableStateOf("") }

    val onTitleChanged = { value: String -> enteredTitle = value }
    val onContentChanged = { value: String -> enteredContent = value }

    var exitDialogTitle by remember { mutableStateOf("") }
    var exitDialogMessage by remember { mutableStateOf("") }
    var exitDialogOpened by remember { mutableStateOf(false) }
    val exitDialogConfirmed = {
        exitDialogOpened = false
        viewModel.backConfirmed()
    }
    val onDismissDialog = { exitDialogOpened = false }

    BackHandler {
        viewModel.backPressed()
    }

    LaunchedEffect(viewModel) {
        viewModel.restoreData()

        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is PostCreateSideEffect.ShowExitDialog -> {
                    exitDialogTitle = it.title
                    exitDialogMessage = it.message
                    exitDialogOpened = true
                }
                PostCreateSideEffect.EmptyContent -> showError(
                    context.getString(R.string.content_cant_be_empty),
                    scope,
                    snackbarHostState
                )
                PostCreateSideEffect.EmptyTitle -> showError(
                    context.getString(R.string.title_cant_be_empty),
                    scope,
                    snackbarHostState
                )
                PostCreateSideEffect.Error -> showError(
                    context.getString(R.string.something_went_wrong),
                    scope,
                    snackbarHostState
                )
                is PostCreateSideEffect.RestoreDraft -> {
                    enteredTitle = it.title
                    enteredContent = it.contents
                }
            }
        }
    }

    LaunchedEffect(pagerState.currentPage, state.categoryState) {
        if (
            pagerState.currentPage == Tab.values().indexOf(Tab.EDITOR)
            && state.categoryState is ListState.None
        ) {
            viewModel.loadCategories()
        }
    }

    DisposableEffect(viewModel) {
        onDispose {
            viewModel.saveDraft(enteredTitle, enteredContent)
        }
    }

    PostCreateWithState(
        state = state,
        postCreateEvents = viewModel,
        pagerState = pagerState,
        enteredTitle = enteredTitle,
        enteredContent = enteredContent,
        onTitleChanged = onTitleChanged,
        onContentChanged = onContentChanged,
        coroutineScope = scope,
        modifier = modifier
    )

    if (exitDialogOpened) {
        ExitDialog(
            title = exitDialogTitle,
            message = exitDialogMessage,
            onConfirm = exitDialogConfirmed,
            onDismiss = onDismissDialog
        )
    }
}

private fun showError(
    message: String,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
) {
    coroutineScope.launch {
        snackbarHostState.showSnackbar(message = message)
    }
}

@Composable
private fun ExitDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(title)
        },
        text = {
            Text(message)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.dialog_dismiss))
            }
        }
    )
}

@ExperimentalPagerApi
@Composable
private fun PostCreateWithState(
    state: PostCreateState,
    postCreateEvents: PostCreateEvents,
    pagerState: PagerState,
    enteredTitle: String,
    onTitleChanged: (String) -> Unit,
    enteredContent: String,
    onContentChanged: (String) -> Unit,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier,
) {
    val pages = Tab.values()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .imePaddingWithBottomBar()
    ) {
        Column(modifier = modifier.fillMaxSize()) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                    )
                },
                backgroundColor = MaterialTheme.colors.secondary,
                edgePadding = 16.dp,
            ) {
                pages.forEachIndexed { index, tab ->
                    LeadingIconTab(
                        text = { Text(stringResource(tab.headerRes).toUpperCase(Locale.current)) },
                        icon = { Icon(tab.iconVector, null) },
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    )
                }
            }

            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { pageNumber ->
                val contentModifier = Modifier.fillMaxSize()
                when (pages[pageNumber]) {
                    Tab.EDITOR -> EditPost(
                        enteredTitle,
                        onTitleChanged,
                        enteredContent,
                        onContentChanged,
                        contentModifier
                    )
                    Tab.PREVIEW -> PreviewPost(
                        enteredTitle,
                        enteredContent,
                        contentModifier
                    )
                    Tab.CATEGORIES -> when (state.categoryState) {
                        is ListState.Error -> ErrorState(contentModifier)
                        ListState.Loading -> LoadingState(contentModifier)
                        is ListState.Success -> SuccessState(
                            state = state.categoryState.contents,
                            postCreateEvents = postCreateEvents,
                            modifier = contentModifier
                        )
                        ListState.None -> {}
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { postCreateEvents.submitPost(enteredTitle, enteredContent) },
            backgroundColor = MaterialTheme.colors.secondary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colors.onSecondary)
            } else {
                Icon(TablerIcons.Send, null)
            }
        }
    }
}

@Composable
private fun EditPost(
    title: String,
    onTitleChanged: (String) -> Unit,
    content: String,
    onContentChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScrollableColumn(modifier = modifier) {
        TextField(
            value = title,
            onValueChange = onTitleChanged,
            placeholder = {
                Text(
                    stringResource(R.string.title_label),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .bottomBorder(1.dp, 16.dp),
        )

        Spacer(modifier = Modifier.size(8.dp))

        TextField(
            value = content,
            onValueChange = onContentChanged,
            placeholder = { Text(stringResource(R.string.content_label)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .fillMaxSize()
                .topBorder(1.dp, 16.dp),
        )
    }
}

@Composable
private fun PreviewPost(
    title: String,
    content: String,
    modifier: Modifier = Modifier,
) {
    ScrollableColumn(modifier = modifier) {
        val titleModifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .bottomBorder(1.dp, 16.dp)
        if (title.isNotBlank()) {
            Text(
                title,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h4,
                modifier = titleModifier,
            )
        } else {
            Text(
                stringResource(R.string.empty_title),
                textAlign = TextAlign.Center,
                color = LocalContentColor.current.copy(ContentAlpha.disabled),
                style = MaterialTheme.typography.subtitle1,
                modifier = titleModifier,
            )
        }

        Spacer(modifier = Modifier.size(8.dp))


        val contentModifier = Modifier
            .fillMaxWidth()
            .topBorder(1.dp, 16.dp)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        when {
            content.isBlank() -> Text(
                stringResource(R.string.empty_content),
                color = LocalContentColor.current.copy(ContentAlpha.disabled),
                modifier = contentModifier
            )
            LocalInspectionMode.current -> Text(content, modifier = contentModifier)
            else -> MaterialRichText(modifier = contentModifier) {
                Markdown(content = content)
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
        errorMessage = stringResource(id = R.string.category_error),
        modifier = modifier,
    )
}

@Composable
private fun SuccessState(
    state: List<PostCreateCategory>,
    postCreateEvents: PostCreateEvents,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .padding(8.dp),
    ) {
        FlowRow(
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 12.dp,
            mainAxisAlignment = FlowMainAxisAlignment.Start,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            state.forEach { item ->
                key(item.category.id) {
                    SelectableChip(
                        text = item.category.name,
                        isSelected = item.isMarked,
                        onSelected = { postCreateEvents.markPost(it, item) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    PostCreateWithStatePreview()
}

@Preview
@Composable
private fun PreviewPreview() {
    PostCreateWithStatePreview(
        title = "title",
        content = "content",
        startPage = 1,
    )
}

@Preview
@Composable
private fun EmptyPreview() {
    PostCreateWithStatePreview(
        state = PostCreateState(),
        title = "",
        content = "",
        startPage = 2,
    )
}

@Preview
@Composable
private fun LoadingPreview() {
    PostCreateWithStatePreview(
        state = PostCreateState(isLoading = true),
        title = "",
        content = "",
        startPage = 2,
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun PostCreateWithStatePreview(
    state: PostCreateState = PostCreateState(),
    title: String = "",
    content: String = "",
    startPage: Int = 0,
) {
    val onTitleChanged = { _: String -> }
    val onContentChanged = { _: String -> }

    PostiumTheme {
        PostCreateWithState(
            state = state,
            postCreateEvents = PostCreateEvents,
            pagerState = rememberPagerState(startPage),
            enteredTitle = title,
            enteredContent = content,
            onTitleChanged = onTitleChanged,
            onContentChanged = onContentChanged,
            coroutineScope = rememberCoroutineScope(),
            modifier = Modifier.fillMaxSize()
        )
    }
}