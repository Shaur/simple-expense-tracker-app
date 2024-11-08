package org.home.tracker.persistence.repository.impl

import org.home.tracker.dto.CategoryDto
import org.home.tracker.dto.ExpenseDto
import org.home.tracker.dto.MonthlyExpenseDto
import org.home.tracker.dto.SummaryDto
import org.home.tracker.dto.WeeklyExpenseDto
import org.home.tracker.persistence.dao.ExpenseDao
import org.home.tracker.persistence.entity.Category
import org.home.tracker.persistence.entity.Expense
import org.home.tracker.persistence.repository.ExpenseRepository

class DefaultExpenseRepository(private val expenseDao: ExpenseDao) : ExpenseRepository {

    override suspend fun findAll(): List<ExpenseDto> {
        return expenseDao.findAll().map { (expense, category) -> convert(expense, category) }
    }

    override suspend fun findAll(from: Long, to: Long): List<ExpenseDto> {
        return expenseDao.findAll(from, to)
            .map { (expense, category) -> convert(expense, category) }
    }

    override suspend fun summary(from: Long, to: Long): List<SummaryDto> {
        return expenseDao.summary(from, to)
    }

    override suspend fun simpleSummary(from: Long, to: Long): Map<String, Long> {
        return expenseDao.simpleSummary(from, to)
    }

    override suspend fun save(expense: ExpenseDto) {
        val entity = Expense(
            date = expense.date ?: System.currentTimeMillis(),
            value = expense.value,
            currencyId = expense.currencyId,
            categoryId = expense.category.id ?: 0L,
            comment = expense.comment
        )

        if (expense.id != 0L) {
            entity.id = expense.id
            expenseDao.update(entity)
        } else {
            expenseDao.insert(entity)
        }
    }

    override suspend fun getByMonths(): List<MonthlyExpenseDto> = expenseDao.getByMonths()

    override suspend fun getByWeeks(limit: Int): List<WeeklyExpenseDto> = expenseDao.getByWeeks(limit)

    private fun convert(entity: Expense, category: Category): ExpenseDto {
        return ExpenseDto(
            id = entity.id!!,
            date = entity.date,
            value = entity.value,
            currencyId = entity.currencyId,
            category = CategoryDto(category.id, category.name)
        )
    }
}