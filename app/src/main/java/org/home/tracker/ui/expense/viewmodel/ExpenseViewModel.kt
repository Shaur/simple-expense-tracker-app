package org.home.tracker.ui.expense.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.home.tracker.dto.CategoryDto
import org.home.tracker.dto.ExpenseDto
import org.home.tracker.dto.PeriodSummaryStateDto
import org.home.tracker.persistence.repository.CategoryRepository
import org.home.tracker.persistence.repository.ExpenseRepository
import org.home.tracker.util.toMillis
import java.time.LocalDate

class ExpenseViewModel(
    private val categoryRepository: CategoryRepository,
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    val state = mutableStateOf(listOf<ExpenseDto>())
    val categories = mutableStateOf(listOf<CategoryDto>())
    val beginPeriod = mutableStateOf(LocalDate.now())
    val periodSummaryState = mutableStateOf(PeriodSummaryStateDto())

    init {
        val now = LocalDate.now()
        val initPeriod = LocalDate.of(now.year, now.monthValue, 1)
        val endPeriod = initPeriod.plusMonths(1L)

        beginPeriod.value = initPeriod

        viewModelScope.launch {
            state.value = expenseRepository.findAll(initPeriod.toMillis(), endPeriod.toMillis())
            categories.value = categoryRepository.findAll()
            updatePeriodSummary()
        }
    }


    fun save(expense: ExpenseDto) {
        viewModelScope.launch {
            var category = expense.category
            if (category.id == null) {
                category = categoryRepository.save(expense.category)
                categories.value = buildList {
                    addAll(categories.value)
                    add(category)
                }
            }

            expenseRepository.save(expense.copy(category = category))

            val endPeriodMillis = beginPeriod.value.plusMonths(1L).toMillis()
            state.value = expenseRepository.findAll(beginPeriod.value.toMillis(), endPeriodMillis)
            updatePeriodSummary()
        }
    }

    fun updateBeginPeriod(selectedDateMillis: Long?) {
        val notNullDateMillis = selectedDateMillis ?: beginPeriod.value.toMillis()
        beginPeriod.value = LocalDate.ofEpochDay(notNullDateMillis / 1000L / 60 / 60 / 24)

        val endPeriodMillis = beginPeriod.value.plusMonths(1L).toMillis()

        updatePeriodSummary()

        viewModelScope.launch {
            state.value = expenseRepository.findAll(beginPeriod.value.toMillis(), endPeriodMillis)
        }
    }

    private fun updatePeriodSummary() {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1L)

        val startOfTheWeek = today.minusDays(today.dayOfWeek.ordinal.toLong()).atStartOfDay()
        val endOfTheWeek = startOfTheWeek.plusDays(7L)

        val startMonth = beginPeriod.value
        val endMonth = startMonth.plusMonths(1L)

        viewModelScope.launch {
            val yesterdaySummary = expenseRepository.simpleSummary(yesterday.toMillis(), today.toMillis() - 1)
            val todaySummary = expenseRepository.simpleSummary(today.toMillis(), today.plusDays(1L).toMillis())
            val weekSummary = expenseRepository.simpleSummary(startOfTheWeek.toMillis(), endOfTheWeek.toMillis())
            val monthSummary = expenseRepository.simpleSummary(startMonth.toMillis(), endMonth.toMillis())

            periodSummaryState.value = periodSummaryState.value.copy(
                yesterday = yesterdaySummary,
                today = todaySummary,
                week = weekSummary,
                month = monthSummary
            )
        }
    }

}