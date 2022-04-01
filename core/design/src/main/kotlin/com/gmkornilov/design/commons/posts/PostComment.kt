package com.gmkornilov.design.commons.posts

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.commons.buttons.DislikeButton
import com.gmkornilov.design.commons.buttons.LikeButton
import com.gmkornilov.design.components.LocalAvatarSize
import com.gmkornilov.design.components.UserAvatar
import com.gmkornilov.design.theme.PostiumTheme

@Composable
fun Comment(
    avatarUrl: String?,
    usernameTitle: String,
    comment: String,
    isUpChecked: Boolean,
    isDownChecked: Boolean,
    modifier: Modifier = Modifier,
    onUpClicked: (Boolean) -> Unit = {},
    onDownClicked: (Boolean) -> Unit = {},
    onOpenProfileClicked: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .padding(top = 4.dp, bottom = 4.dp),
    ) {
        CompositionLocalProvider(
            LocalAvatarSize provides 48.dp
        ) {
            UserAvatar(
                avatarUrl = avatarUrl,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.Top)
            )
        }

        Spacer(Modifier.size(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                usernameTitle,
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.clickable { onOpenProfileClicked.invoke() },
            )

            Spacer(Modifier.size(8.dp))

            Text(
                comment,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
            )
        }

        Spacer(Modifier.size(8.dp))

        LikeButton(isChecked = isUpChecked, onCheckedChange = onUpClicked)

        DislikeButton(isChecked = isDownChecked, onCheckedChange = onDownClicked)
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun CommentsColumnLight() {
    CommentsColumn()
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CommentsColumnDark() {
    CommentsColumn()
}

@Composable
private fun CommentsColumn() {
    val loremIpsum = LoremIpsum(20).values.joinToString(" ")

    PostiumTheme {
        Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
            Comment(
                comment = "Title",
                usernameTitle = "Georgium",
                avatarUrl = null,
                isUpChecked = true,
                isDownChecked = false,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            Comment(
                comment = loremIpsum,
                usernameTitle = "Georgium",
                avatarUrl = "",
                isUpChecked = false,
                isDownChecked = true,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            Comment(
                avatarUrl = "",
                usernameTitle = "Test",
                comment = "test comment",
                isUpChecked = false,
                isDownChecked = false,
            )
        }
    }
}