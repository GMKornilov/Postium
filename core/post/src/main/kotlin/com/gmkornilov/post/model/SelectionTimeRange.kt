package com.gmkornilov.post.model

import com.gmkornilov.model.TimeRange

enum class SelectionTimeRange {
    LAST_DAY,
    LAST_WEEK,
    ALL_TIME
}

internal fun SelectionTimeRange.toTimeRange() = when(this) {
    SelectionTimeRange.ALL_TIME -> TimeRange.ALL_TIME
    SelectionTimeRange.LAST_DAY -> TimeRange.LAST_DAY
    SelectionTimeRange.LAST_WEEK -> TimeRange.LAST_WEEK
}