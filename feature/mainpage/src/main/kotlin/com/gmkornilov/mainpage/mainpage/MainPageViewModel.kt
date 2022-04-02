package com.gmkornilov.mainpage.mainpage

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.domain.UserResultHandler
import com.gmkornilov.letIf
import com.gmkornilov.mainpage.domain.MainpageInteractor
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.post.model.toOppositeStatus
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

internal class MainPageViewModel @Inject constructor(
    private val mainpageInteractor: MainpageInteractor,
    private val authInteractor: AuthInteractor,
    private val listener: MainPageListener,
) : BaseViewModel<MainPageState, Unit>(), MainPageEvents {
    override fun getBaseState() = MainPageState()

    private fun getLikeUserHandler(post: PostPreviewData) = UserResultHandler {
        intent {
            val newLikeStatus = post.likeStatus.toOppositeLikeStatus()

            val newPost = post.copy(likeStatus = newLikeStatus)
            replacePost(post, newPost)

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    mainpageInteractor.setLikeStatus(post, newLikeStatus)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    private fun getDislikeUserHandler(post: PostPreviewData) = UserResultHandler {
        intent {
            val newLikeStatus = post.likeStatus.toOppositeDislikeStatus()

            val newPost = post.copy(likeStatus = newLikeStatus)
            replacePost(post, newPost)

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    mainpageInteractor.setLikeStatus(post, newLikeStatus)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    private fun getBookmarkUserHandler(post: PostPreviewData) = UserResultHandler {
        val newBookmarkStatus = post.bookmarkStatus.toOppositeStatus()
        val newPost = post.copy(bookmarkStatus = newBookmarkStatus)
        replacePost(post, newPost)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                mainpageInteractor.setBookmarkStatus(post, newBookmarkStatus)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun loadAllPosts(isRefresh: Boolean = false) = intent {
        viewModelScope.launch(Dispatchers.IO) {
            if (isRefresh) {
                reduce { this.state.copy(isRefresh = true) }
            } else {
                reduce { changeCurrentSelectionState(this.state, PostsState.Loading) }
            }
            try {
                val posts = mainpageInteractor.loadDataWithTimeRange(this@intent.state.currentRange)
                reduce { changeCurrentSelectionState(this.state, PostsState.Success(posts)) }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { changeCurrentSelectionState(this.state, PostsState.Error(e)) }
            }
        }
    }

    override fun refreshData() {
        loadAllPosts(true)
    }

    override fun selectTimeRange(postTimeRange: PostTimeRange) = intent {
        reduce { this.state.copy(currentRange = postTimeRange) }
    }

    override fun openPost(post: PostPreviewData) {
        listener.openPost(post)
    }

    override fun openProfile(post: PostPreviewData) {
        listener.openUserProfile(post)
    }

    override fun likePost(post: PostPreviewData) {
        val likeUserHandler = getLikeUserHandler(post)
        val user = authInteractor.getPostiumUser()
        user?.let {
            likeUserHandler.handleResult(user)
        } ?: listener.startAuthorizationFlow(likeUserHandler)
    }

    override fun dislikePost(post: PostPreviewData) {
        val dislikeUserHandler = getDislikeUserHandler(post)
        val user = authInteractor.getPostiumUser()
        user?.let {
            dislikeUserHandler.handleResult(user)
        } ?: listener.startAuthorizationFlow(dislikeUserHandler)
    }

    override fun bookmarkPost(post: PostPreviewData) {
        val bookmarkUserHandler = getBookmarkUserHandler(post)
        val user = authInteractor.getPostiumUser()
        user?.let {
            bookmarkUserHandler.handleResult(user)
        } ?: listener.startAuthorizationFlow(bookmarkUserHandler)
    }

    private fun replacePost(oldPost: PostPreviewData, newPost: PostPreviewData) = intent {
        reduce {
            val currentState = this.state.currentPageState()
            val newState = currentState.letIf(
                currentState is PostsState.Success,
                { state ->
                    val success = state as PostsState.Success
                    val newItems = success.items.toMutableList().apply {
                        val index = this.indexOf(oldPost)
                        if (index != -1) {
                            this[index] = newPost
                        }
                    }
                    PostsState.Success(newItems)
                },
                { it }
            )
            changeCurrentSelectionState(this.state, newState)
        }
    }

    private fun changeCurrentSelectionState(
        state: MainPageState,
        postsState: PostsState,
        isRefresh: Boolean = false,
    ): MainPageState {
        return when (state.currentRange) {
            PostTimeRange.ALL_TIME -> state.copy(allTimeState = postsState, isRefresh = isRefresh)
            PostTimeRange.DAY -> state.copy(lastDayState = postsState, isRefresh = isRefresh)
            PostTimeRange.WEEK -> state.copy(lastWeekState = postsState, isRefresh = isRefresh)
        }
    }
}

interface MainPageListener {
    fun openPost(postPreviewData: PostPreviewData)

    fun startAuthorizationFlow(userResultHandler: UserResultHandler)

    fun openUserProfile(postPreviewData: PostPreviewData)
}