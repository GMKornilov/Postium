package com.gmkornilov.design.commons.playlists

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmkornilov.context.pluralResource
import com.gmkornilov.design.R
import com.gmkornilov.design.data.CornerType
import com.gmkornilov.design.theme.PostiumTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlaylistPreview(
    name: String,
    postAmount: Int,
    modifier: Modifier = Modifier,
    onPlaylistClicked: () -> Unit = {},
    cornerType: CornerType = CornerType.ALL,
) {
    val cornerRadiuses = 16.dp
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = cornerType.toTopStart(cornerRadiuses),
            topEnd = cornerType.toTopEnd(cornerRadiuses),
            bottomStart = cornerType.toBottomStart(cornerRadiuses),
            bottomEnd = cornerType.toBottomEnd(cornerRadiuses),
        ),
        onClick = onPlaylistClicked,
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.secondary)
                .padding(
                    top = 16.dp,
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
        ) {
            Text(text = name,
                style = MaterialTheme.typography.h5,
                color = contentColorFor(MaterialTheme.colors.primary),
            )

            Spacer(modifier = Modifier.size(4.dp))

            Text(
                text = pluralResource(R.plurals.playlist_label, postAmount, postAmount),
                style = MaterialTheme.typography.body1,
                color = contentColorFor(MaterialTheme.colors.primary),
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun PlaylistsColumnLight() {
    PostsColumn()
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PlaylistColumnDark() {
    PostsColumn()
}

@Composable
private fun PostsColumn() {
    PostiumTheme {
        Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
            PlaylistPreview(
                name = "Playlist 1",
                postAmount = 4,
                cornerType = CornerType.BOTTOM,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            PlaylistPreview(
                name = "Playlist 2",
                postAmount = 3,
                cornerType = CornerType.ALL,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            PlaylistPreview(
                name = "Playlist 3",
                postAmount = 1,
                cornerType = CornerType.TOP,
            )
        }
    }
}