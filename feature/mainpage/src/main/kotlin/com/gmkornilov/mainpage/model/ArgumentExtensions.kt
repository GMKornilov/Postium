package com.gmkornilov.mainpage.model

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

fun PostPreviewData.toUserPageArgument() = UserPageArgument(
    id = "",
    username = username,
    avatarUrl = avatarUrl
)