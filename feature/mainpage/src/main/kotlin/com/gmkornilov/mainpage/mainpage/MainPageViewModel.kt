package com.gmkornilov.mainpage.mainpage

import com.gmkornilov.post.Post
import com.gmkornilov.source.FirebasePostSource
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import javax.inject.Inject

internal class MainPageViewModel @Inject constructor(
    private val firebasePostSource: FirebasePostSource,
) : BaseViewModel<MainPageState, Unit>(), MainPageEvents {
    override fun getBaseState() = MainPageState()

    fun loadAllPosts() = intent {
        viewModelScope.launch {
            reduce { changeCurrentSelectionState(this.state, PostsState.Loading) }
            try {
                val posts = firebasePostSource.getAllPosts()
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

    override fun openPost(post: Post) {
        TODO("Not yet implemented")
    }

    override fun likePost(post: Post) {
        TODO("Not yet implemented")
    }

    override fun dislikePost(post: Post) {
        TODO("Not yet implemented")
    }

    override fun bookmarkPost(post: Post) {
        TODO("Not yet implemented")
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