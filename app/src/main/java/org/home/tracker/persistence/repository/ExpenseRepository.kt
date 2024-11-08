package org.home.tracker.persistence.repository

import org.home.tracker.dto.ExpenseDto
import org.home.tracker.dto.MonthlyExpenseDto
import org.home.tracker.dto.SummaryDto
import org.home.tracker.dto.WeeklyExpenseDto
import org.home.tracker.persistence.entity.Category
import org.home.tracker.persistence.entity.Expense

interface ExpenseRepository {

    suspend fun findAll(): List<ExpenseDto>

    suspend fun findAll(from: Long, to: Long): List<ExpenseDto>

    suspend fun summary(from: Long, to: Long): List<SummaryDto>

    suspend fun simpleSummary(from: Long, to: Long): Map<String, Long>

    suspend fun save(expense: ExpenseDto)

    suspend fun getByMonths(): List<MonthlyExpenseDto>

    suspend fun getByWeeks(limit: Int): List<WeeklyExpenseDto>
}