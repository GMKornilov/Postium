package com.gmkornilov.mainpage.mainpage

import androidx.annotation.StringRes
import com.gmkornilov.mainpage.R
import com.gmkornilov.model.TimeRange
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.post.model.SelectionTimeRange

internal data class MainPageState(
    val currentRange: PostTimeRange = PostTimeRange.ALL_TIME,
)

internal enum class PostTimeRange(@StringRes val titleRes: Int) {
    ALL_TIME(R.string.all_time_title),
    DAY(R.string.last_day_title),
    WEEK(R.string.last_week_title),
}

internal fun PostTimeRange.toDataTimeRange() = when (this) {
    PostTimeRange.ALL_TIME -> TimeRange.ALL_TIME
    PostTimeRange.DAY -> TimeRange.LAST_DAY
    PostTimeRange.WEEK -> TimeRange.LAST_WEEK
}

internal fun PostTimeRange.toSelectionTimeRange() = when (this) {
    PostTimeRange.ALL_TIME -> SelectionTimeRange.ALL_TIME
    PostTimeRange.DAY -> SelectionTimeRange.LAST_DAY
    PostTimeRange.WEEK -> SelectionTimeRange.LAST_WEEK
}
