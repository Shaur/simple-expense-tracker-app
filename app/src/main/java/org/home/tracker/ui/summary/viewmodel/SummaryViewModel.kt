package org.home.tracker.ui.summary.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.home.tracker.dto.Aggregation
import org.home.tracker.dto.ExpenseDto
import org.home.tracker.dto.ExtendedSummaryDto
import org.home.tracker.persistence.repository.ExpenseRepository
import org.home.tracker.ui.SummaryType
import org.home.tracker.util.toMillis
import java.time.LocalDate

class SummaryViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    val items = mutableStateOf(listOf<ExtendedSummaryDto>())
    val subItems = mutableStateOf(mapOf<Long, List<ExpenseDto>>())
    val aggregations = mutableStateOf(mapOf<String, MutableList<Aggregation>>())

    companion object {
        private const val WEEKS_PERIOD = 5
    }

    fun initItems(date: LocalDate, type: SummaryType) {
        val endPeriod = date.plus(1L, type.unit)
        val startPreviousPeriod = date.minus(1L, type.unit)
        viewModelScope.launch {
            val previousPeriod = expenseRepository.summary(
                startPreviousPeriod.toMillis(),
                date.toMillis() - 1
            )
                    .associate { dto -> dto.key() to dto.value }

            items.value = expenseRepository.summary(date.toMillis(), endPeriod.toMillis())
                .map { summary ->
                    ExtendedSummaryDto.from(summary)
                        .copy(prevValue = previousPeriod[summary.key()] ?: 0L)
                }
        }
    }

    fun initSubitems(date: LocalDate, type: SummaryType, categoryId: Long) {
        val endPeriod = date.plus(1L, type.unit)
        viewModelScope.launch {
            val map = subItems.value.toMutableMap()
            map[categoryId] =
                expenseRepository.findAll(date.toMillis(), endPeriod.toMillis(), categoryId)
            subItems.value = map
        }
    }

    fun clearSubItems(categoryId: Long) {
        val map = subItems.value.toMutableMap()
        map.remove(categoryId)
        subItems.value = map
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