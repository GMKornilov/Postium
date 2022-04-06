package com.gmkornilov.mainpage.mainpage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmkornilov.mainpage.R
import com.gmkornilov.post_list.view.LocalEmptyStateMessage
import com.gmkornilov.post_list.view.LocalErrorStateMessage
import com.gmkornilov.post_list.view.PostList


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun Mainpage(
    mainPageViewModel: MainPageViewModel,
    modifier: Modifier,
) {
    val state by mainPageViewModel.container.stateFlow.collectAsState()

    val currentViewModel = mainPageViewModel.currentViewModel()

    val currentRange = state.currentRange

    var menuExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.background(MaterialTheme.colors.background)) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
                .padding(top = 8.dp, bottom = 8.dp)
                .height(48.dp)
        ) {
            Text(
                text = stringResource(id = R.string.main_page_title),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onPrimary,
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
                        color = MaterialTheme.colors.onPrimary,
                    )

                    Icon(
                        Icons.Filled.ArrowDropDown,
                        null,
                        modifier = Modifier.rotate(if (menuExpanded) 180f else 360f),
                        tint = MaterialTheme.colors.onPrimary,
                    )
                }
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    PostTimeRange.values().forEach { postTimeRange ->
                        DropdownMenuItem(
                            onClick = {
                                mainPageViewModel.changeSelection(postTimeRange)
                                menuExpanded = false
                            }
                        ) {
                            Text(text = stringResource(id = postTimeRange.titleRes))
                        }
                    }
                }
            }
        }

        CompositionLocalProvider(
            LocalErrorStateMessage provides stringResource(R.string.load_posts_error),
            LocalEmptyStateMessage provides stringResource(R.string.posts_not_found)
        ) {
            PostList(viewModel = currentViewModel, modifier = Modifier.fillMaxSize())
        }
    }
}