package org.home.tracker.ui.summary.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.home.tracker.dto.Aggregation
import org.home.tracker.dto.SummaryDto
import org.home.tracker.persistence.repository.ExpenseRepository
import org.home.tracker.ui.SummaryType
import org.home.tracker.util.toMillis
import java.time.LocalDate

class SummaryViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    val items = mutableStateOf(listOf<SummaryDto>())
    val aggregations = mutableStateOf(mapOf<String, MutableList<Aggregation>>())

    companion object {
        private const val WEEKS_PERIOD = 5
    }

    fun initItems(date: LocalDate, type: SummaryType) {
        val endPeriod = date.plus(1L, type.unit)
        viewModelScope.launch {
            items.value = expenseRepository.summary(date.toMillis(), endPeriod.toMillis())
        }
    }

    fun initAggregations(type: SummaryType) {
        viewModelScope.launch {
            val values = when (type) {
                SummaryType.WEEK_BY_WEEK -> expenseRepository.getByWeeks(WEEKS_PERIOD)
                SummaryType.MONTH_BY_MONTH -> expenseRepository.getByMonths()
                else -> return@launch
            }

            val columns = mutableMapOf<String, MutableList<Aggregation>>()
            for (value in values) {
                val column = columns[value.currency] ?: mutableListOf()
                column.add(value)
                columns[value.currency] = column
            }

            aggregations.value = columns
        }
    }

}