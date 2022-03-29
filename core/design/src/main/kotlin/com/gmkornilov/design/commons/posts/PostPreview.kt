package com.gmkornilov.design.commons.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.gmkornilov.design.commons.buttons.BookmarkButton
import com.gmkornilov.design.commons.buttons.DislikeButton
import com.gmkornilov.design.commons.buttons.LikeButton
import com.gmkornilov.design.components.UserAvatar
import com.gmkornilov.design.data.CornerType
import com.gmkornilov.design.theme.PostiumTheme

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
    userProfileClicked: () -> Unit = {},
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
                UserAvatar(
                    avatarUrl = it,
                    modifier = Modifier
                        .constrainAs(avatarRef) {
                            top.linkTo(dividerRef.bottom, margin = 8.dp)
                            start.linkTo(parent.start, margin = 8.dp)
                            bottom.linkTo(parent.bottom, margin = 8.dp)
                        }
                        .clickable {
                            userProfileClicked()
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
                        top.linkTo(dividerRef.bottom)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        userProfileClicked()
                    }
            )

            LikeButton(
                isChecked = isUpChecked,
                onCheckedChange = upClicked,
                modifier = Modifier.constrainAs(upRef) {
                    end.linkTo(downRef.start, margin = 4.dp)
                    top.linkTo(dividerRef.bottom, margin = 8.dp)
                },
            )

            DislikeButton(
                isChecked = isDownChecked,
                onCheckedChange = downClicked,
                modifier = Modifier.constrainAs(downRef) {
                    end.linkTo(markRef.start, margin = 4.dp)
                    top.linkTo(upRef.top)
                }
            )

            BookmarkButton(
                isChecked = isBookmarkChecked,
                onCheckedChange = boolmarkClicked,
                modifier = Modifier.constrainAs(markRef) {
                    end.linkTo(parent.end, margin = 16.dp)
                    top.linkTo(upRef.top)
                },
            )
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
                modifier = Modifier.padding(bottom = 4.dp),
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