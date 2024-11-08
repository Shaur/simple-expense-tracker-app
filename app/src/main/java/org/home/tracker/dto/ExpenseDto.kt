package org.home.tracker.dto

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class ExpenseDto(
    val id: Long,
    val date: Long? = null,
    val value: Long,
    val category: CategoryDto,
    val currencyId: String,
    val comment: String = ""
) {

    fun getLocalDate(): LocalDate? {
        if (date == null) return null

        return Instant.ofEpochMilli(date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

}
