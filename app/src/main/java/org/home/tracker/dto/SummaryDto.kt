package org.home.tracker.dto

data class SummaryDto(
    val category: String,
    val categoryId: Long,
    val value: Long,
    val currency: String
) {
    fun key(): Pair<Long, String> {
        return categoryId to currency
    }
}
