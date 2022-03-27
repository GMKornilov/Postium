package com.gmkornilov.mainpage.mainpage

import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.model.PostiumUser
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.authorization.feature_flow.AuthorizationFlowScreenFactory
import com.gmkornilov.letIf
import com.gmkornilov.mainpage.domain.MainpageInteractor
import com.gmkornilov.mainpage.model.PostPreviewData
import com.gmkornilov.mainpage.model.PostPreviewLikeStatus
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

internal class MainPageViewModel @Inject constructor(
    private val mainpageInteractor: MainpageInteractor,
    private val router: TreeRouter,
    private val authoriztionFlowFactory: AuthorizationFlowScreenFactory,
    private val authInteractor: AuthInteractor,
) : BaseViewModel<MainPageState, Unit>(), MainPageEvents {
    override fun getBaseState() = MainPageState()

    private fun getLikeUserHandler(post: PostPreviewData) = object : UserResultHandler {
        override fun handleResult(user: PostiumUser) {
            intent {
                val newPost = post.copy(likeStatus = PostPreviewLikeStatus.LIKED)

                reduce {
                    val currentState = this.state.currentPageState()
                    val newState = currentState.letIf(
                        currentState is PostsState.Success,
                        { state ->
                            val success = state as PostsState.Success
                            val newItems = success.items.toMutableList().apply {
                                val index = this.indexOf(post)
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

                viewModelScope.launch {
                    try {
                        mainpageInteractor.likePost(post)
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            }
        }
    }

    private fun getDislikeUserHandler(post: PostPreviewData) = object: UserResultHandler {
        override fun handleResult(user: PostiumUser) {
            intent {
                val newPost = post.copy(likeStatus = PostPreviewLikeStatus.DISLIKED)

                reduce {
                    val currentState = this.state.currentPageState()
                    val newState = currentState.letIf(
                        currentState is PostsState.Success,
                        { state ->
                            val success = state as PostsState.Success
                            val newItems = success.items.toMutableList().apply {
                                val index = this.indexOf(post)
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

                viewModelScope.launch {
                    try {
                        mainpageInteractor.dislikePost(post)
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            }
        }
    }

    private fun getBookmarkUserHandler(post: PostPreviewData) = object : UserResultHandler {
        override fun handleResult(user: PostiumUser) {
            TODO("Not yet implemented")
        }
    }

    fun loadAllPosts() = intent {
        viewModelScope.launch {
            reduce { changeCurrentSelectionState(this.state, PostsState.Loading) }
            try {
                val posts = mainpageInteractor.loadData()
                reduce { changeCurrentSelectionState(this.state, PostsState.Success(posts)) }
            } catch (e: Exception) {
                Timber.e(e)
                reduce { changeCurrentSelectionState(this.state, PostsState.Error(e)) }
            }
        }
    }

    override fun selectTimeRange(postTimeRange: PostTimeRange) = intent {
        reduce { this.state.copy(currentRange = postTimeRange) }
    }

    override fun openPost(post: PostPreviewData) {
        TODO("Not yet implemented")
    }

    override fun likePost(post: PostPreviewData) {
        val likeUserHandler = getLikeUserHandler(post)
        val user = authInteractor.getPostiumUser()
        user?.let {
            likeUserHandler.handleResult(user)
        } ?: authoriztionFlowFactory.start(likeUserHandler, router)
    }

    override fun dislikePost(post: PostPreviewData) {
        val dislikeUserHandler = getDislikeUserHandler(post)
        val user = authInteractor.getPostiumUser()
        user?.let {
            dislikeUserHandler.handleResult(user)
        } ?: authoriztionFlowFactory.start(dislikeUserHandler, router)
    }

    override fun bookmarkPost(post: PostPreviewData) {
        val bookmarkUserHandler = getBookmarkUserHandler(post)
        val user = authInteractor.getPostiumUser()
        user?.let {
            bookmarkUserHandler.handleResult(user)
        } ?: authoriztionFlowFactory.start(bookmarkUserHandler, router)
    }

    private fun changeCurrentSelectionState(
        state: MainPageState,
        postsState: PostsState
    ): MainPageState {
        return when (state.currentRange) {
            PostTimeRange.ALL_TIME -> state.copy(allTimeState = postsState)
            PostTimeRange.DAY -> state.copy(lastDayState = postsState)
            PostTimeRange.WEEK -> state.copy(lastWeekState = postsState)
        }
    }
}