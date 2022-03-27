package com.gmkornilov.model

import com.gmkornilov.date.minusDays
import java.util.*

enum class TimeRange {
    LAST_DAY {
        override fun getStartDate(currentDate: Date): Date {
            return currentDate.minusDays(1)
        }
    },
    LAST_WEEK {
        override fun getStartDate(currentDate: Date): Date {
            return currentDate.minusDays(7)
        }
    },
    ALL_TIME {
        override fun getStartDate(currentDate: Date): Date {
            return Date(0)
        }
    };

    abstract fun getStartDate(currentDate: Date): Date
}