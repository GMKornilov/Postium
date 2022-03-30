package com.gmkornilov.postcreatepage.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.components.ScrollableColumn
import com.gmkornilov.design.modifiers.bottomBorder
import com.gmkornilov.design.modifiers.topBorder
import com.gmkornilov.design.theme.PostiumTheme
import com.gmkornilov.postcreatepage.R
import com.google.accompanist.pager.*
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material.MaterialRichText
import compose.icons.TablerIcons
import compose.icons.tablericons.Send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

@OptIn(InternalCoroutinesApi::class, ExperimentalPagerApi::class)
@Composable
internal fun PostCreate(
    viewModel: PostCreateViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.container.stateFlow.collectAsState()

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    var enteredTitle by remember { mutableStateOf("") }
    var enteredContent by remember { mutableStateOf("") }

    val onTitleChanged = { value: String -> enteredTitle = value }
    val onContentChanged = { value: String -> enteredContent = value }

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
        viewModel.container.sideEffectFlow.collect(FlowCollector {
            when (it) {
                PostCreateSideEffect.ShowExitDialog -> {
                    exitDialogOpened = true
                }
            }
        })
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
        ExitDialog(onConfirm = exitDialogConfirmed, onDismiss = onDismissDialog)
    }
}

@Composable
private fun ExitDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.dialog_title))
        },
        text = {
            Text(stringResource(R.string.dialog_text))
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
    ) {
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
                }
//                when (val tabState = state.tabStates.getValue(tab)) {
//                    is TabState.Error -> ErrorState(tab)
//                    TabState.Loading -> LoadingState()
//                    is TabState.Success -> if (tabState.posts.isNotEmpty()) {
//                        SuccessState(
//                            posts = tabState.posts,
//                            userPageEvents = userPageEvents,
//                            modifier = contentModifier
//                        )
//                    } else {
//                        EmptyState(tab)
//                    }
//                    TabState.None -> {}
//                }
            }
        }

        FloatingActionButton(
            onClick = { },
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
        ) {
            Icon(TablerIcons.Send, null)
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
                .bottomBorder(1.dp, MaterialTheme.colors.background, 16.dp),
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
                .topBorder(1.dp, MaterialTheme.colors.background, 16.dp),
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
        Text(
            title,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.size(8.dp))

        Divider()

        Spacer(modifier = Modifier.size(8.dp))

        if (LocalInspectionMode.current) {
            Text(content, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp))
        } else {
            MaterialRichText(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp)) {
                Markdown(content = content)
            }
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    PostCreateWithStatePreview(state = PostCreateState())
}

@Preview
@Composable
private fun PreviewPreview() {
    PostCreateWithStatePreview(
        state = PostCreateState(),
        title = "title",
        content = "content",
        startPage = 1,
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun PostCreateWithStatePreview(
    state: PostCreateState,
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