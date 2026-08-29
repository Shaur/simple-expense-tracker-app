package org.home.tracker.dto

data class ExpenseRuleDto(
    val id: Long,
    val pattern: String,
    val category: CategoryDto
)
