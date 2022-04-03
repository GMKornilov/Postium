package com.gmkornilov.mainpage.mainpage

import com.gmkornilov.authorizarion.domain.UserResultHandler
import com.gmkornilov.mainpage.brick_navigation.TimeRange
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.post_list.view.PostListViewModel
import com.gmkornilov.view_model.BaseViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

internal class MainPageViewModel @Inject constructor(
    @TimeRange(PostTimeRange.ALL_TIME) private val allTimeViewModel: PostListViewModel,
    @TimeRange(PostTimeRange.DAY) private val lastDayViewModel: PostListViewModel,
    @TimeRange(PostTimeRange.WEEK) private val lastWeekViewModel: PostListViewModel,
) : BaseViewModel<MainPageState, Unit>() {
    override fun getBaseState() = MainPageState(PostTimeRange.ALL_TIME)

    fun changeSelection(selectedTimeRange: PostTimeRange) = intent {
        reduce { this.state.copy(currentRange = selectedTimeRange) }
    }

    fun currentViewModel(): PostListViewModel {
        return when (this.container.stateFlow.value.currentRange) {
            PostTimeRange.ALL_TIME -> allTimeViewModel
            PostTimeRange.DAY -> lastDayViewModel
            PostTimeRange.WEEK -> lastWeekViewModel
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        allTimeViewModel.onDestroy()
        lastDayViewModel.onDestroy()
        lastWeekViewModel.onDestroy()
    }
}

interface MainPageListener {
    fun openPost(postPreviewData: PostPreviewData)

    fun startAuthorizationFlow(userResultHandler: UserResultHandler)

    fun openUserProfile(postPreviewData: PostPreviewData)
}