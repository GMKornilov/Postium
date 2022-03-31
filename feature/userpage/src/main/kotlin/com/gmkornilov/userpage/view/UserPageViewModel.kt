package com.gmkornilov.userpage.view

import android.util.Log
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.domain.UserResultHandler
import com.gmkornilov.letIf
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.post.model.toOppositeStatus
import com.gmkornilov.userpage.brick_navigation.UserPageArgument
import com.gmkornilov.userpage.domain.UserPageInteractor
import com.gmkornilov.view_model.BaseViewModel
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
        viewModelScope.launch {
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

    private fun getLikeResultHandler(postPreview: PostPreviewData) = UserResultHandler {
        val newLikeStatus = postPreview.likeStatus.toOppositeLikeStatus()
        val newPost = postPreview.copy(likeStatus = newLikeStatus)
        replacePost(postPreview, newPost)

        viewModelScope.launch {
            try {
                userPageInteractor.setLikeStatus(postPreview, newLikeStatus)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun getDislikeResultHandler(postPreview: PostPreviewData) = UserResultHandler {
        val newLikeStatus = postPreview.likeStatus.toOppositeDislikeStatus()
        val newPost = postPreview.copy(likeStatus = newLikeStatus)
        replacePost(postPreview, newPost)

        viewModelScope.launch {
            try {
                userPageInteractor.setLikeStatus(postPreview, newLikeStatus)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun getBookmarkResultHandler(postPreview: PostPreviewData) = UserResultHandler {
        val newBookmarkStatus = postPreview.bookmarkStatus.toOppositeStatus()
        val newPost = postPreview.copy(bookmarkStatus = newBookmarkStatus)
        replacePost(postPreview, newPost)

        viewModelScope.launch {
            try {
                userPageInteractor.setBookmarkStatus(postPreview, newBookmarkStatus)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun tabSelected(tab: Tab) = intent {
        Timber.log(Log.INFO, tab.toString())

        currentTab = tab

        val state = this.state.tabStates.getValue(tab)

        if (state != TabState.None) {
            return@intent
        }

        when (tab) {
            Tab.POSTS -> loadUserPosts()
            Tab.BOOKMARKS -> loadUserBookmarks()
        }
    }

    override fun likePost(postPreviewData: PostPreviewData) {
        val userResultHandler = getLikeResultHandler(postPreviewData)
        val currentUser = authInteractor.getPostiumUser()
        currentUser?.let {
            userResultHandler.handleResult(currentUser)
        } ?: listener.startAuthorizationFlow(userResultHandler)
    }

    override fun dislikePost(postPreviewData: PostPreviewData) {
        val userResultHandler = getDislikeResultHandler(postPreviewData)
        val currentUser = authInteractor.getPostiumUser()
        currentUser?.let {
            userResultHandler.handleResult(currentUser)
        } ?: listener.startAuthorizationFlow(userResultHandler)
    }

    override fun bookmarkPost(postPreviewData: PostPreviewData) {
        val userResultHandler = getBookmarkResultHandler(postPreviewData)
        val currentUser = authInteractor.getPostiumUser()
        currentUser?.let {
            userResultHandler.handleResult(currentUser)
        } ?: listener.startAuthorizationFlow(userResultHandler)
    }

    override fun openPost(postPreviewData: PostPreviewData) {
        listener.openPost(postPreviewData)
    }

    override fun loadHeader() = intent {
        viewModelScope.launch {
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
        }
    }

    private fun getCurrentState() = this.container.stateFlow.value.getCurrentTabState()

    private fun loadUserPosts(isRefresh: Boolean = false) = intent {
        if (isRefresh) {
            reduce { this.state.copy(isRefresh = true) }
        } else {
            reduce { this.state.changeTabState(Tab.POSTS, TabState.Loading) }
        }

        viewModelScope.launch {
            try {
                val posts = userPageInteractor.loadPosts(navArgument.id)
                reduce { this.state.changeTabState(Tab.POSTS, TabState.Success(posts)) }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { this.state.changeTabState(Tab.POSTS, TabState.Error(e)) }
            }
        }
    }

    private fun loadUserBookmarks(isRefresh: Boolean = false) = intent {
        if (isRefresh) {
            reduce { this.state.copy(isRefresh = true) }
        } else {
            reduce { this.state.changeTabState(Tab.BOOKMARKS, TabState.Loading) }
        }

        viewModelScope.launch {
            try {
                val posts = userPageInteractor.loadBookmarks(navArgument.id)
                reduce { this.state.changeTabState(Tab.BOOKMARKS, TabState.Success(posts)) }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { this.state.changeTabState(Tab.BOOKMARKS, TabState.Error(e)) }
            }
        }
    }

    private fun replacePost(oldPost: PostPreviewData, newPost: PostPreviewData) = intent {
        reduce {
            val currentState = getCurrentState()
            val newState = currentState.letIf(
                currentState is TabState.Success,
                { state ->
                    val success = state as TabState.Success
                    val newItems = success.posts.toMutableList().apply {
                        val index = this.indexOf(oldPost)
                        if (index != -1) {
                            this[index] = newPost
                        }
                    }
                    TabState.Success(newItems)
                },
                { it }
            )
            this.state.changeTabState(currentTab, newState)
        }
    }

    private fun UserPageState.getCurrentTabState() = this.tabStates.getValue(currentTab)

    private fun UserPageState.changeTabState(tab: Tab, tabState: TabState, isRefresh: Boolean = false): UserPageState {
        val newTabStates = this.tabStates.toMutableMap().apply {
            this[tab] = tabState
        }
        return this.copy(tabStates = newTabStates, isRefresh = isRefresh)
    }
}

interface UserPageListener {
    fun openPost(postPreviewData: PostPreviewData)

    fun openUserProfile(postPreviewData: PostPreviewData)

    fun createPost()

    fun startAuthorizationFlow(userResultHandler: UserResultHandler)
}