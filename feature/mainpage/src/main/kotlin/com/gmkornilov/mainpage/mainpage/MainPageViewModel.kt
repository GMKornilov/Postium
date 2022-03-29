package com.gmkornilov.mainpage.mainpage

import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorization.domain.UserResultHandler
import com.gmkornilov.authorization.feature_flow.AuthorizationFlowScreenFactory
import com.gmkornilov.letIf
import com.gmkornilov.mainpage.domain.MainpageInteractor
import com.gmkornilov.mainpage.model.toPostPageArgument
import com.gmkornilov.mainpage.model.toUserPageArgument
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.post.model.toOppositeStatus
import com.gmkornilov.postpage.brick_navigation.PostPageScreenFactory
import com.gmkornilov.userpage.brick_navigation.UserPageScreenFactory
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
    private val postPageScreenFactory: PostPageScreenFactory,
    private val userPageScreenFactory: UserPageScreenFactory,
) : BaseViewModel<MainPageState, Unit>(), MainPageEvents {
    override fun getBaseState() = MainPageState()

    private fun getLikeUserHandler(post: PostPreviewData) = UserResultHandler {
        intent {
            val newLikeStatus = post.likeStatus.toOppositeLikeStatus()

            val newPost = post.copy(likeStatus = newLikeStatus)
            replacePost(post, newPost)

            viewModelScope.launch {
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

            viewModelScope.launch {
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

        viewModelScope.launch {
            try {
                mainpageInteractor.setBookmarkStatus(post, newBookmarkStatus)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun loadAllPosts() = intent {
        viewModelScope.launch {
            reduce { changeCurrentSelectionState(this.state, PostsState.Loading) }
            try {
                val posts = mainpageInteractor.loadDataWithTimeRange(this@intent.state.currentRange)
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
        val screen = postPageScreenFactory.build(router)
        router.addScreen(screen, post.toPostPageArgument())
    }

    override fun openProfile(post: PostPreviewData) {
        val screen = userPageScreenFactory.build()
        router.addScreen(screen, post.toUserPageArgument())
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
        postsState: PostsState
    ): MainPageState {
        return when (state.currentRange) {
            PostTimeRange.ALL_TIME -> state.copy(allTimeState = postsState)
            PostTimeRange.DAY -> state.copy(lastDayState = postsState)
            PostTimeRange.WEEK -> state.copy(lastWeekState = postsState)
        }
    }
}