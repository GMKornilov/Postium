package com.gmkornilov.root_screen

import com.alphicc.brick.TreeRouter
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.comments.model.CommentPreviewData
import com.gmkornilov.postpage.brick_navigation.PostPageArgument
import com.gmkornilov.userpage.brick_navigation.UserPageArgument

fun PostPreviewData.toPostPageArgument() = PostPageArgument(
    id = id,
    title = title,
    userId = userId,
    username = username,
    avatarUrl = avatarUrl,
    likeStatus = likeStatus,
    bookmarkStatus = bookmarkStatus,
    likes = likes,
    dislikes = dislikes,
    comments = commentAmount
)

fun PostPreviewData.toUserPageArgument() = UserPageArgument.ReadyHeader(
    id = userId,
    username = username,
    avatarUrl = avatarUrl,
)

fun CommentPreviewData.toUserPageArgument() = UserPageArgument.ReadyHeader(
    id = this.userId,
    username = username,
    avatarUrl = avatarUrl,
)

fun TreeRouter.isEmpty() = this.screen.value == null