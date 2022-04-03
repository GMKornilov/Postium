package com.gmkornilov.user_playlists.playlist_posts.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.commons.posts.PostPreview
import com.gmkornilov.design.components.EmptyStateContainer
import com.gmkornilov.design.components.ErrorStateContainer
import com.gmkornilov.design.data.CornerType
import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.letIf
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.post_list.view.LocalEmptyStateMessage
import com.gmkornilov.post_list.view.LocalErrorStateMessage
import com.gmkornilov.post_list.view.PostList
import com.gmkornilov.user_playlists.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun PlaylistPostsList(
    viewModel: PlaylistPostsViewModel,
    modifier: Modifier = Modifier,
) {
    val listViewModel = viewModel.listViewModel

    CompositionLocalProvider(
        LocalErrorStateMessage provides stringResource(id = R.string.playlists_posts_error),
        LocalEmptyStateMessage provides stringResource(id = R.string.playlists_posts_empty)
    ) {
        Column(modifier = modifier.background(MaterialTheme.colors.background)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface)
                    .padding(top = 8.dp, bottom = 8.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = viewModel.playlistName,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .align(Alignment.CenterVertically),
                )
            }

            PostList(viewModel = listViewModel, modifier = Modifier.weight(1f))
        }
    }
}