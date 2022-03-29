package com.gmkornilov.userpage.view

import android.util.Log
import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.authorization.feature_flow.AuthorizationFlowScreenFactory
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
    private val router: TreeRouter,
    private val userPageInteractor: UserPageInteractor,
    private val authInteractor: AuthInteractor,
    private val authorizationFlowScreenFactory: AuthorizationFlowScreenFactory,
): BaseViewModel<UserPageState, Unit>(), UserPageEvents {
    private var currentTab = Tab.POSTS

    override fun getBaseState(): UserPageState {
        return UserPageState(
            headerState = HeaderState(navArgument.username, navArgument.avatarUrl)
        )
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
        } ?: authorizationFlowScreenFactory.start(userResultHandler, router)
    }

    override fun dislikePost(postPreviewData: PostPreviewData) {
        val userResultHandler = getDislikeResultHandler(postPreviewData)
        val currentUser = authInteractor.getPostiumUser()
        currentUser?.let {
            userResultHandler.handleResult(currentUser)
        } ?: authorizationFlowScreenFactory.start(userResultHandler, router)
    }

    override fun bookmarkPost(postPreviewData: PostPreviewData) {
        val userResultHandler = getBookmarkResultHandler(postPreviewData)
        val currentUser = authInteractor.getPostiumUser()
        currentUser?.let {
            userResultHandler.handleResult(currentUser)
        } ?: authorizationFlowScreenFactory.start(userResultHandler, router)
    }

    override fun openPost(postPreviewData: PostPreviewData) {
        TODO("Not yet implemented")
    }

    private fun getCurrentState() = this.container.stateFlow.value.getCurrentTabState()

    private fun loadUserPosts() = intent {
        reduce { this.state.changeTabState(Tab.POSTS, TabState.Loading) }

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

    private fun loadUserBookmarks() = intent {
        reduce { this.state.changeTabState(Tab.BOOKMARKS, TabState.Loading) }

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

    private fun UserPageState.changeTabState(tab: Tab, tabState: TabState): UserPageState {
        val newTabStates = this.tabStates.toMutableMap().apply {
            this[tab] = tabState
        }
        return this.copy(tabStates = newTabStates)
    }
}