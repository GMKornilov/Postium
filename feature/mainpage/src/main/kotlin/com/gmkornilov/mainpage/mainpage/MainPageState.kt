package com.gmkornilov.mainpage.mainpage

import androidx.annotation.StringRes
import com.gmkornilov.mainpage.R
import com.gmkornilov.mainpage.model.PostPreviewData
import com.gmkornilov.model.TimeRange

internal data class MainPageState(
    val allTimeState: PostsState = PostsState.None,
    val lastDayState: PostsState = PostsState.None,
    val lastWeekState: PostsState = PostsState.None,

    val currentRange: PostTimeRange = PostTimeRange.ALL_TIME,
) {
    fun currentPageState(): PostsState {
        return when (currentRange) {
            PostTimeRange.ALL_TIME -> allTimeState
            PostTimeRange.DAY -> lastDayState
            PostTimeRange.WEEK -> lastWeekState
        }
    }
}

internal enum class PostTimeRange(@StringRes val titleRes: Int) {
    ALL_TIME(R.string.all_time_title),
    DAY(R.string.last_day_title),
    WEEK(R.string.last_week_title),
}

internal fun PostTimeRange.toTimeRange() = when(this) {
    PostTimeRange.ALL_TIME -> TimeRange.ALL_TIME
    PostTimeRange.DAY -> TimeRange.LAST_DAY
    PostTimeRange.WEEK -> TimeRange.LAST_WEEK
}

sealed class PostsState {
    object None: PostsState()

    object Loading: PostsState()

    data class Error(val e: Exception): PostsState()

    data class Success(val items: List<PostPreviewData>): PostsState()
}
