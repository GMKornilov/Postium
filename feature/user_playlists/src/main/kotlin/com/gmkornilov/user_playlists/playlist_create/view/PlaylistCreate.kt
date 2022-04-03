package com.gmkornilov.user_playlists.playlist_create.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmkornilov.user_playlists.R
import com.google.accompanist.insets.navigationBarsWithImePadding

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun PlaylistCreate(
    viewModel: PlaylistCreateViewModel,
    modifier: Modifier = Modifier,
) {
    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded) {
        if (it == ModalBottomSheetValue.HalfExpanded) {
            return@rememberModalBottomSheetState false
        }
        if (it == ModalBottomSheetValue.Hidden) {
            viewModel.onCloseBottomSheet()
        }

        return@rememberModalBottomSheetState true
    }

    DisposableEffect(viewModel) {
        onDispose {
            viewModel.onCloseBottomSheet()
        }
    }

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = { SheetContent(viewModel) },
        content = {},
        sheetBackgroundColor = MaterialTheme.colors.surface,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    )
}

@Composable
private fun SheetContent(viewModel: PlaylistCreateViewModel) {
    val state by viewModel.container.stateFlow.collectAsState()
    var enteredName by remember { mutableStateOf("") }

    Column(
        Modifier
            .navigationBarsWithImePadding()
            .padding(bottom = 32.dp)
    ) {
        Text(
            stringResource(R.string.create_playlist_title),
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 8.dp),
        )

        Divider()

        TextField(
            value = enteredName,
            onValueChange = { enteredName = it },
            label = { Text(stringResource(R.string.playlist_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 16.dp),
        )

        Spacer(modifier = Modifier.size(16.dp))

        Button(
            onClick = { viewModel.createPlaylist(enteredName) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
                .height(52.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = LocalContentColor.current)
            } else {
                Text(stringResource(R.string.playlist_create_button_label))
            }
        }
    }
}