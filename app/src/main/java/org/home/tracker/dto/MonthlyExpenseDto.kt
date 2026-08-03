package org.home.tracker.dto

data class MonthlyExpenseDto(
    val month: Int,
    val year: Int,
    override val currency: String,
    override val value: Long
) : Aggregation(currency, value) {

    override fun getTimeAxis(): String = "${month}/${year}"

    override fun getTimeAxisId(): Float {
        return year % 100 * 100f + month
    }

}
