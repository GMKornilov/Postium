package com.gmkornilov.date

import java.util.*

fun Date.plusDays(days: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DATE, days)
    return calendar.time
}

fun Date.minusDays(days: Int) = this.plusDays(-days)