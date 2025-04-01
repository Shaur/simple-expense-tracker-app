package org.home.tracker.dto

data class SummaryDto(
    val category: String,
    val categoryId: Long,
    val value: Long,
    val currency: String
)
