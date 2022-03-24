package com.gmkornilov.design.commons.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.SubcomposeAsyncImage
import com.gmkornilov.design.data.CornerType
import com.gmkornilov.design.theme.DarkBurgundy
import com.gmkornilov.design.theme.Green
import com.gmkornilov.design.theme.PostiumTheme
import com.google.accompanist.placeholder.material.placeholder

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostPreview(
    title: String,
    userName: String,
    avatarUrl: String?,
    isUpChecked: Boolean,
    isDownChecked: Boolean,
    isBookmarkChecked: Boolean,
    modifier: Modifier = Modifier,
    cornerType: CornerType = CornerType.ALL,
    onCardClick: () -> Unit = {},
    upClicked: (Boolean) -> Unit = {},
    downClicked: (Boolean) -> Unit = {},
    boolmarkClicked: (Boolean) -> Unit = {},
) {
    val cornerRadius = 32.dp
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        shape = RoundedCornerShape(
            topStart = cornerType.toTopStart(cornerRadius),
            topEnd = cornerType.toTopEnd(cornerRadius),
            bottomStart = cornerType.toBottomStart(cornerRadius),
            bottomEnd = cornerType.toBottomEnd(cornerRadius),
        ),
        onClick = onCardClick
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (titleRef, userNameRef, dividerRef, avatarRef, upRef, downRef, markRef) = createRefs()
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.constrainAs(titleRef) {
                    top.linkTo(parent.top, margin = 8.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
            )

            Divider(modifier = Modifier.constrainAs(dividerRef) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(titleRef.bottom, margin = 8.dp)
            })

            avatarUrl?.let {
                SubcomposeAsyncImage(
                    model = it,
                    loading = {
                        Box(
                            modifier = Modifier
                                .height(IntrinsicSize.Max)
                                .width(IntrinsicSize.Max)
                                .placeholder(visible = true)
                        )
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .constrainAs(avatarRef) {
                            top.linkTo(dividerRef.bottom, margin = 8.dp)
                            start.linkTo(parent.start, margin = 8.dp)
                            bottom.linkTo(parent.bottom, margin = 8.dp)
                        }
                )
            }

            val userNamePadding = if (avatarUrl == null) 16.dp else 0.dp

            Text(
                text = userName,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .padding(start = userNamePadding)
                    .constrainAs(userNameRef) {
                        start.linkTo(avatarRef.end, margin = 8.dp)
                        top.linkTo(dividerRef.bottom, margin = 8.dp)
                        bottom.linkTo(upRef.bottom)
                    }
            )

            IconToggleButton(
                checked = isUpChecked,
                onCheckedChange = upClicked,
                modifier = Modifier.constrainAs(upRef) {
                    end.linkTo(downRef.start, margin = 4.dp)
                    top.linkTo(dividerRef.bottom, margin = 8.dp)
                }
            ) {
                Icon(
                    imageVector = if (isUpChecked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                    contentDescription = null,
                    tint = if (isUpChecked) Green else MaterialTheme.colors.onSurface,
                )
            }

            IconToggleButton(
                checked = isDownChecked,
                onCheckedChange = downClicked,
                modifier = Modifier.constrainAs(downRef) {
                    end.linkTo(markRef.start, margin = 4.dp)
                    top.linkTo(upRef.top)
                }
            ) {
                Icon(
                    imageVector = if (isDownChecked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                    contentDescription = null,
                    tint = if (isDownChecked) Color.Red else MaterialTheme.colors.onSurface,
                )
            }

            IconToggleButton(
                checked = isBookmarkChecked,
                onCheckedChange = boolmarkClicked,
                modifier = Modifier.constrainAs(markRef) {
                    end.linkTo(parent.end, margin = 16.dp)
                    top.linkTo(upRef.top)
                }
            ) {
                Icon(
                    imageVector = if (isBookmarkChecked) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkAdd,
                    contentDescription = null,
                    tint = if (isBookmarkChecked) DarkBurgundy else MaterialTheme.colors.onSurface,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PostsColumnLight() {
    PostsColumn(isLight = true)
}

@Preview
@Composable
private fun PostsColumnDark() {
    PostsColumn(isLight = false)
}

@Composable
private fun PostsColumn(isLight: Boolean = true) {
    PostiumTheme(darkTheme = !isLight) {
        Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
            PostPreview(
                title = "Title",
                userName = "Georgium",
                avatarUrl = null,
                isUpChecked = true,
                isDownChecked = false,
                isBookmarkChecked = false,
                cornerType = CornerType.BOTTOM,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            PostPreview(
                title = "Title",
                userName = "Georgium",
                avatarUrl = "",
                isUpChecked = false,
                isDownChecked = true,
                cornerType = CornerType.ALL,
                isBookmarkChecked = false,
                modifier =  Modifier.padding(bottom = 4.dp),
            )

            PostPreview(
                title = "Title",
                userName = "Georgium",
                avatarUrl = "",
                isUpChecked = false,
                isDownChecked = false,
                isBookmarkChecked = true,
                cornerType = CornerType.TOP,
            )
        }
    }
}