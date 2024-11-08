package org.home.tracker.dto

import org.home.tracker.util.Constants.DateTime.CURRENT_YEAR
import org.home.tracker.util.Constants.DateTime.WEEKS_IN_YEAR

data class WeeklyExpenseDto(
    val week: Int,
    val year: Int,
    override val currency: String,
    override val value: Long
) : Aggregation(currency, value) {

    override fun getTimeAxis(): String = "${week}/${year}"

    override fun getTimeAxisId(): Float {
//        return WEEKS_IN_YEAR * (year - CURRENT_YEAR) + week
        return (year + week).toFloat()
    }
}
