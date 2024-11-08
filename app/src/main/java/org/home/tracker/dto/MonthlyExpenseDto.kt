package org.home.tracker.dto

import org.home.tracker.util.Constants.DateTime.CURRENT_YEAR

data class MonthlyExpenseDto(
    val month: Int,
    val year: Int,
    override val currency: String,
    override val value: Long
) : Aggregation(currency, value) {

    override fun getTimeAxis(): String = "${month}/${year}"

    override fun getTimeAxisId(): Float {
        return 12f * (year - CURRENT_YEAR) + month
    }

}
