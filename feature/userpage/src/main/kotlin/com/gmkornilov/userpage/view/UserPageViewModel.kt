package com.gmkornilov.userpage.view

import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.domain.UserResultHandler
import com.gmkornilov.design.data.CornerType
import com.gmkornilov.lazy_column.ListState
import com.gmkornilov.letIf
import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.post.model.toOppositeStatus
import com.gmkornilov.userpage.brick_navigation.UserPageArgument
import com.gmkornilov.userpage.domain.UserPageInteractor
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

internal class UserPageViewModel @Inject constructor(
    private val navArgument: UserPageArgument,
    private val userPageInteractor: UserPageInteractor,
    private val authInteractor: AuthInteractor,
    private val listener: UserPageListener,
) : BaseViewModel<UserPageState, Unit>(), UserPageEvents {
    private var currentTab = Tab.POSTS

    init {
        viewModelScope.launch(Dispatchers.IO) {
            authInteractor.authState.collect {
                val canCreatePost = it?.getUid() == navArgument.id
                intent { reduce { this.state.copy(createPostButtonVisible = canCreatePost) } }
            }
        }
    }

    override fun getBaseState(): UserPageState {
        val canCreatePost = authInteractor.getPostiumUser()?.getUid() == navArgument.id

        val headerState = when (navArgument) {
            is UserPageArgument.LoadHeader -> HeaderState(
                needLoading = true,
                username = "",
                avatarUrl = null,
            )
            is UserPageArgument.ReadyHeader -> HeaderState(
                username = navArgument.username,
                avatarUrl = navArgument.avatarUrl,
            )
        }
        return UserPageState(headerState = headerState, createPostButtonVisible = canCreatePost)
    }

    private fun getLikeResultHandler(postPreviewItem: TabListItem.PostPreviewItem) =
        UserResultHandler {
            val newLikeStatus = postPreviewItem.postPreviewData.likeStatus.toOppositeLikeStatus()
            val newPost = postPreviewItem.postPreviewData.copy(likeStatus = newLikeStatus)
            val newPostItem = postPreviewItem.copy(postPreviewData = newPost)
            replacePost(postPreviewItem, newPostItem)

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    userPageInteractor.setLikeStatus(postPreviewItem.postPreviewData, newLikeStatus)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }

    private fun getDislikeResultHandler(postPreviewItem: TabListItem.PostPreviewItem) =
        UserResultHandler {
            val newLikeStatus = postPreviewItem.postPreviewData.likeStatus.toOppositeDislikeStatus()
            val newPost = postPreviewItem.postPreviewData.copy(likeStatus = newLikeStatus)
            val newPostItem = postPreviewItem.copy(postPreviewData = newPost)
            replacePost(postPreviewItem, newPostItem)

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    userPageInteractor.setLikeStatus(postPreviewItem.postPreviewData, newLikeStatus)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }

    private fun getBookmarkResultHandler(postPreviewItem: TabListItem.PostPreviewItem) =
        UserResultHandler {
            val newBookmarkStatus =
                postPreviewItem.postPreviewData.bookmarkStatus.toOppositeStatus()
            val newPost = postPreviewItem.postPreviewData.copy(bookmarkStatus = newBookmarkStatus)
            val newPostItem = postPreviewItem.copy(postPreviewData = newPost)
            replacePost(postPreviewItem, newPostItem)

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    userPageInteractor.setBookmarkStatus(postPreviewItem.postPreviewData,
                        newBookmarkStatus)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }

    override fun tabSelected(tab: Tab) = intent {
        Timber.log(Log.INFO, tab.toString())

        currentTab = tab

        val state = this.state.tabStates.getValue(tab)

        if (state != ListState.None) {
            return@intent
        }

        when (tab) {
            Tab.POSTS -> loadUserPosts()
            Tab.BOOKMARKS -> loadUserBookmarks()
            Tab.PLAYLISTS -> loadUserPlaylists()
        }
    }

    override fun likePost(postPreviewItem: TabListItem.PostPreviewItem) {
        val userResultHandler = getLikeResultHandler(postPreviewItem)
        val currentUser = authInteractor.getPostiumUser()
        currentUser?.let {
            userResultHandler.handleResult(currentUser)
        } ?: listener.startAuthorizationFlow(userResultHandler)
    }

    override fun dislikePost(postPreviewItem: TabListItem.PostPreviewItem) {
        val userResultHandler = getDislikeResultHandler(postPreviewItem)
        val currentUser = authInteractor.getPostiumUser()
        currentUser?.let {
            userResultHandler.handleResult(currentUser)
        } ?: listener.startAuthorizationFlow(userResultHandler)
    }

    override fun bookmarkPost(postPreviewItem: TabListItem.PostPreviewItem) {
        val userResultHandler = getBookmarkResultHandler(postPreviewItem)
        val currentUser = authInteractor.getPostiumUser()
        currentUser?.let {
            userResultHandler.handleResult(currentUser)
        } ?: listener.startAuthorizationFlow(userResultHandler)
    }

    override fun openPost(postPreviewItem: TabListItem.PostPreviewItem) {
        listener.openPost(postPreviewItem.postPreviewData)
    }

    override fun openProfile(postPreviewData: PostPreviewData) {
        listener.openUserProfile(postPreviewData)
    }

    override fun addToPlaylists(post: PostPreviewData) {
        listener.addToPlaylist(post)
    }

    override fun openPlaylist(playlist: Playlist) {
        listener.openPlaylist(playlist)
    }

    override fun loadHeader() = intent {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = userPageInteractor.loadHeader(navArgument.id)
                val newHeader = HeaderState(
                    needLoading = false,
                    username = user.name,
                    avatarUrl = user.avatarUrl,
                )
                reduce { this.state.copy(headerState = newHeader) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun createPost() {
        listener.createPost()
    }

    override fun refreshData() {
        when (currentTab) {
            Tab.POSTS -> loadUserPosts(true)
            Tab.BOOKMARKS -> loadUserBookmarks(true)
            Tab.PLAYLISTS -> loadUserPlaylists(true)
        }
    }

    private fun getCurrentState() = this.container.stateFlow.value.getCurrentTabState()

    private fun loadUserPosts(isRefresh: Boolean = false) = intent {
        if (isRefresh) {
            reduce { this.state.copy(isRefresh = true) }
        } else {
            reduce { this.state.changeTabState(Tab.POSTS, ListState.Loading) }
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val posts = userPageInteractor.loadPosts(navArgument.id)
                val postItems = mapPosts(posts)
                reduce { this.state.changeTabState(Tab.POSTS, ListState.Success(postItems)) }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { this.state.changeTabState(Tab.POSTS, ListState.Error(e)) }
            }
        }
    }

    private fun loadUserBookmarks(isRefresh: Boolean = false) = intent {
        if (isRefresh) {
            reduce { this.state.copy(isRefresh = true) }
        } else {
            reduce { this.state.changeTabState(Tab.BOOKMARKS, ListState.Loading) }
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val posts = userPageInteractor.loadBookmarks(navArgument.id)
                val postItems = mapPosts(posts)
                reduce { this.state.changeTabState(Tab.BOOKMARKS, ListState.Success(postItems)) }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { this.state.changeTabState(Tab.BOOKMARKS, ListState.Error(e)) }
            }
        }
    }

    private fun loadUserPlaylists(isRefresh: Boolean = false) = intent {
        if (isRefresh) {
            reduce { this.state.copy(isRefresh = true) }
        } else {
            reduce { this.state.changeTabState(Tab.PLAYLISTS, ListState.Loading) }
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val playlists = userPageInteractor.loadPlaylists(navArgument.id)
                val playlistItems = mapPlaylists(playlists)
                reduce {
                    this.state.changeTabState(Tab.PLAYLISTS, ListState.Success(playlistItems))
                }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { this.state.changeTabState(Tab.PLAYLISTS, ListState.Error(e)) }
            }
        }
    }

    private fun mapPosts(posts: List<PostPreviewData>): List<TabListItem.PostPreviewItem> {
        return posts.mapIndexed { index, post ->
            val isFirst = index == 0
            val isLast = index == posts.lastIndex

            val cornerType: CornerType
            val bottomPadding: Dp

            when {
                isFirst -> {
                    cornerType = CornerType.BOTTOM
                    bottomPadding = 4.dp
                }
                isLast -> {
                    cornerType = CornerType.ALL
                    bottomPadding = 0.dp
                }
                else -> {
                    cornerType = CornerType.ALL
                    bottomPadding = 4.dp
                }
            }

            TabListItem.PostPreviewItem(
                post,
                cornerType,
                bottomPadding,
                this@UserPageViewModel
            )
        }
    }

    private fun mapPlaylists(playlists: List<Playlist>): List<TabListItem.PlaylistItem> {
        return playlists.map {
            TabListItem.PlaylistItem(
                it,
                this@UserPageViewModel,
            )
        }
    }

    private fun replacePost(
        oldPost: TabListItem.PostPreviewItem,
        newPost: TabListItem.PostPreviewItem,
    ) = intent {
        reduce {
            val currentState = getCurrentState()
            val newState = currentState.letIf(
                currentState is ListState.Success,
                { state ->
                    val success = state as ListState.Success
                    val newItems = success.contents.toMutableList().apply {
                        val index = this.indexOf(oldPost)
                        if (index != -1) {
                            this[index] = newPost
                        }
                    }
                    ListState.Success(newItems)
                },
                { it }
            )
            this.state.changeTabState(currentTab, newState)
        }
    }

    private fun UserPageState.getCurrentTabState() = this.tabStates.getValue(currentTab)

    private fun UserPageState.changeTabState(
        tab: Tab,
        tabState: ListState<TabListItem>,
        isRefresh: Boolean = false,
    ): UserPageState {
        val newTabStates = this.tabStates.toMutableMap().apply {
            this[tab] = tabState
        }
        return this.copy(tabStates = newTabStates, isRefresh = isRefresh)
    }
}

interface UserPageListener {
    fun openPost(postPreviewData: PostPreviewData)

    fun openUserProfile(postPreviewData: PostPreviewData)

    fun openPlaylist(playlist: Playlist)

    fun addToPlaylist(postPreviewData: PostPreviewData)

    fun createPost()

    fun startAuthorizationFlow(userResultHandler: UserResultHandler)
}