package com.gmkornilov.user_playlists.playlist_list.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.commons.playlists.PlaylistPreview
import com.gmkornilov.design.components.EmptyStateContainer
import com.gmkornilov.design.components.ErrorStateContainer
import com.gmkornilov.design.data.CornerType
import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.user_playlists.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import compose.icons.TablerIcons
import compose.icons.tablericons.FolderPlus

@Composable
internal fun PlaylistList(
    viewModel: PlaylistListViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.container.stateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.loadData()
    }

    PlaylistListWithState(state = state, playlistListEvents = viewModel, modifier = modifier)
}

@Composable
private fun PlaylistListWithState(
    state: PlaylistListState,
    playlistListEvents: PlaylistListEvents,
    modifier: Modifier = Modifier,
) {
    SwipeRefresh(
        rememberSwipeRefreshState(state.isRefreshing),
        onRefresh = { playlistListEvents.refreshData() }
    ) {
        val contentModifier = modifier.verticalScroll(rememberScrollState())
        when (state.listState) {
            is ListState.Error -> ErrorState(contentModifier)
            ListState.Loading -> LoadingState(contentModifier)
            is ListState.Success -> SuccessState(
                contents = state.listState.contents,
                playlistListEvents = playlistListEvents,
                modifier = contentModifier
            )
            ListState.None -> {}
        }
    }
}

@Composable
private fun SuccessState(
    contents: List<Playlist>,
    playlistListEvents: PlaylistListEvents,
    modifier: Modifier,
) {
    Box {
        if (contents.isEmpty()) {
            EmptyState(modifier.fillMaxSize())
        } else {
            PlaylistColumn(
                state = contents,
                playlistListEvents = playlistListEvents,
                modifier = Modifier.fillMaxSize()
            )
        }

        FloatingActionButton(
            onClick = { playlistListEvents.createPlaylist() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            backgroundColor = MaterialTheme.colors.secondary,
        ) {
            Icon(imageVector = TablerIcons.FolderPlus, contentDescription = null)
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
        errorMessage = stringResource(id = R.string.playlists_error),
        modifier = modifier,
    )
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    EmptyStateContainer(
        emptyStateMessage = stringResource(id = R.string.playlists_empty),
        modifier = modifier
    )
}

@Composable
private fun PlaylistColumn(
    state: List<Playlist>,
    playlistListEvents: PlaylistListEvents,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .padding(top = 8.dp),
    ) {
        Text(
            stringResource(R.string.playlists_title),
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 8.dp),
        )

        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .weight(1f)
        ) {
            itemsIndexed(state, { _, item: Playlist -> item.id }) { index, item ->
                val isLast = index == state.lastIndex
                val isFirst = index == 0
                val bottomPadding = if (!isLast) 8.dp else 0.dp
                val cornerType = if (isFirst) CornerType.BOTTOM else CornerType.ALL

                PlaylistPreview(name = item.name,
                    postAmount = item.postIds.size,
                    cornerType = cornerType,
                    modifier = Modifier.padding(bottom = bottomPadding),
                    onPlaylistClicked = { playlistListEvents.openPlaylist(item) }
                )
            }
        }
    }
}