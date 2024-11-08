package org.home.tracker.dto

data class PeriodSummaryStateDto(
    val yesterday: Map<String, Long> = mapOf(),
    val today: Map<String, Long> = mapOf(),
    val week: Map<String, Long> = mapOf(),
    val month: Map<String, Long> = mapOf()
)
