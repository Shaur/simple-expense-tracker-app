package org.home.tracker.ui

import java.time.temporal.ChronoUnit

enum class SummaryType(val unit: ChronoUnit) {
    DAY_BY_DAY(ChronoUnit.DAYS),
    WEEK_BY_WEEK(ChronoUnit.WEEKS),
    MONTH_BY_MONTH(ChronoUnit.MONTHS)
}
