package com.gmkornilov.userpage.view

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.commons.playlists.PlaylistPreview
import com.gmkornilov.design.commons.posts.PostPreview
import com.gmkornilov.design.data.CornerType
import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.letIf
import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.userpage.R

internal data class UserPageState(
    val headerState: HeaderState = HeaderState(),
    val tabStates: Map<Tab, ListState<TabListItem>> = Tab.values().map { it to ListState.None }.toMap(),
    val createPostButtonVisible: Boolean = false,
    val isRefresh: Boolean = false,
)

data class HeaderState(
    val needLoading: Boolean = false,
    val username: String = "",
    val avatarUrl: String? = null,
)

internal sealed class TabListItem {
    @Composable
    abstract fun Layout(modifier: Modifier)

    abstract val id: String

    data class PostPreviewItem(
        val postPreviewData: PostPreviewData,
        private val cornerType: CornerType,
        private val bottomPadding: Dp,
        private val userPageEvents: UserPageEvents,
    ): TabListItem() {
        @Composable
        override fun Layout(modifier: Modifier) {
            PostPreview(
                title = postPreviewData.title,
                userName = postPreviewData.username,
                avatarUrl = postPreviewData.avatarUrl.letIf(!postPreviewData.avatarUrl.isNullOrEmpty()) { it },
                isUpChecked = postPreviewData.likeStatus.isLiked,
                isDownChecked = postPreviewData.likeStatus.isDisliked,
                isBookmarkChecked = postPreviewData.bookmarkStatus.isBookmarked,
                cornerType = cornerType,
                modifier = modifier.padding(bottom = bottomPadding),
                onCardClick = { userPageEvents.openPost(this) },
                upClicked = { userPageEvents.likePost(this) },
                downClicked = { userPageEvents.dislikePost(this) },
                boolmarkClicked = { userPageEvents.bookmarkPost(this) },
                userProfileClicked = { userPageEvents.openProfile(postPreviewData) },
                playlistClicked = { userPageEvents.addToPlaylists(postPreviewData) }
            )
        }

        override val id: String = postPreviewData.id
    }

    data class PlaylistItem(
        private val playlist: Playlist,
        private val userPageEvents: UserPageEvents,
    ): TabListItem() {
        @Composable
        override fun Layout(modifier: Modifier) {
            PlaylistPreview(
                name = playlist.name,
                postAmount = playlist.postIds.size,
                cornerType = CornerType.ALL,
                modifier = modifier.padding(start = 4.dp, end = 4.dp, top = 4.dp),
                onPlaylistClicked = { userPageEvents.openPlaylist(playlist) }
            )
        }

        override val id: String = playlist.id
    }
}

internal enum class Tab(
    @StringRes val headerRes: Int,
    @StringRes val errorRes: Int,
    @StringRes val emptyRes: Int,
) {
    POSTS(R.string.posts, R.string.posts_error, R.string.posts_empty),
    BOOKMARKS(R.string.bookmarks, R.string.bookmarks_error, R.string.bookmarks_empty),
    PLAYLISTS(R.string.playlists, R.string.playlists_error, R.string.playlists_empty),
}