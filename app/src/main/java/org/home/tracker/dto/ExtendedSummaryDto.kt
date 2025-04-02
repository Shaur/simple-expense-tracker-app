package org.home.tracker.dto

data class ExtendedSummaryDto(
    val category: String,
    val categoryId: Long,
    val value: Long,
    val prevValue: Long = 0L,
    val currency: String
) {
    companion object {
        fun from(summary: SummaryDto): ExtendedSummaryDto {
            return ExtendedSummaryDto(
                category = summary.category,
                categoryId = summary.categoryId,
                value = summary.value,
                currency = summary.currency
            )
        }
    }
}