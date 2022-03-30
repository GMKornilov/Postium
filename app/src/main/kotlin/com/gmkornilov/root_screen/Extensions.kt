package com.gmkornilov.root_screen

import com.alphicc.brick.TreeRouter
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.postpage.brick_navigation.PostPageArgument
import com.gmkornilov.userpage.brick_navigation.UserPageArgument

fun PostPreviewData.toPostPageArgument() = PostPageArgument(
    id = id,
    title = title,
    username = username,
    avatarUrl = avatarUrl,
    likeStatus = likeStatus,
    bookmarkStatus = bookmarkStatus,
)

fun PostPreviewData.toUserPageArgument() = UserPageArgument.ReadyHeader(
    id = userId,
    username = username,
    avatarUrl = avatarUrl
)

fun TreeRouter.isEmpty() = this.screen.value == null