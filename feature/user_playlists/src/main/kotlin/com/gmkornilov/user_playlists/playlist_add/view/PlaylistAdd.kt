package com.gmkornilov.user_playlists.playlist_add.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.components.EmptyStateContainer
import com.gmkornilov.design.components.ErrorStateContainer
import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.user_playlists.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun PlaylistAdd(
    viewModel: PlaylistAddViewModel,
    modifier: Modifier = Modifier,
) {
    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)
    {
        if (it == ModalBottomSheetValue.HalfExpanded) {
            return@rememberModalBottomSheetState false
        }
        if (it == ModalBottomSheetValue.Hidden) {
            viewModel.onCloseBottomSheet()
        }

        return@rememberModalBottomSheetState true
    }

    LaunchedEffect(viewModel) {
        viewModel.loadData()
    }

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = { SheetContent(viewModel, modifier) },
        content = {},
        sheetBackgroundColor = MaterialTheme.colors.surface,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    )
}

@Composable
private fun SheetContent(viewModel: PlaylistAddViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.container.stateFlow.collectAsState()

    Column(modifier = Modifier.heightIn(min = 256.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
        ) {
            Text(stringResource(R.string.playlist_add_label), modifier = Modifier.padding(16.dp))

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = { viewModel.submit() },
                enabled = !state.isLoading,
                modifier = Modifier.padding(end = 8.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colors.primary,
                    disabledContentColor = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.disabled)
                )
            ) {
                Text(stringResource(R.string.playlist_add))
            }
        }

        Divider()

        when (val listState = state.listState) {
            is ListState.Error -> ErrorState()
            ListState.Loading -> LoadingState()
            is ListState.Success -> SuccessState(
                playlistStates = listState.contents,
                isLoading = state.isLoading,
                viewModel = viewModel
            )
            ListState.None -> {}
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
        errorMessage = stringResource(R.string.playlists_error),
        modifier = modifier,
    )
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    EmptyStateContainer(
        emptyStateMessage = stringResource(R.string.playlists_empty),
        modifier = modifier.fillMaxWidth().padding(bottom = 42.dp)
    )
}

@Composable
private fun SuccessState(
    playlistStates: List<PlaylistItemState>,
    isLoading: Boolean,
    viewModel: PlaylistAddViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TextButton(
            onClick = { viewModel.createPlaylist() },
            enabled = !isLoading,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text(stringResource(R.string.create_playlist_title))
        }

        Divider()

        if (playlistStates.isNotEmpty()) {
            PlaylistsState(playlistStates, !isLoading, viewModel = viewModel)
        } else {
            EmptyState()
        }
    }
}

@Composable
private fun PlaylistsState(
    playlistStates: List<PlaylistItemState>,
    isEnabled: Boolean,
    viewModel: PlaylistAddViewModel,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier) {
        itemsIndexed(playlistStates,
            { _, state: PlaylistItemState -> state.playlist.id }) { index, state ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                Checkbox(
                    checked = state.isSelected,
                    onCheckedChange = { viewModel.selectPlaylist(state, it) },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    enabled = isEnabled,
                )

                CompositionLocalProvider(
                    LocalContentAlpha provides if (isEnabled) ContentAlpha.high else ContentAlpha.disabled
                ) {
                    Text(
                        state.playlist.name,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 8.dp)
                    )
                }
            }

            val isLast = index == playlistStates.lastIndex

            if (!isLast) {
                Divider()
            }
        }
    }
}