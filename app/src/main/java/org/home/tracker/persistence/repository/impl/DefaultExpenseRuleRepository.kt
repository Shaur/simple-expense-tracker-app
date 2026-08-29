package org.home.tracker.persistence.repository.impl

import org.home.tracker.dto.CategoryDto
import org.home.tracker.dto.ExpenseRuleDto
import org.home.tracker.persistence.dao.ExpenseRuleDao
import org.home.tracker.persistence.entity.Category
import org.home.tracker.persistence.entity.ExpenseRule
import org.home.tracker.persistence.repository.ExpenseRuleRepository

class DefaultExpenseRuleRepository(private val expenseRuleDao: ExpenseRuleDao) : ExpenseRuleRepository {
    override suspend fun findAll(): List<ExpenseRuleDto> {
        return expenseRuleDao.findAll()
            .map { (rule, category) -> convert(rule, category) }
    }

    override suspend fun save(rule: ExpenseRuleDto) {
        val entity = ExpenseRule(
            pattern = rule.pattern,
            categoryId = rule.category.id ?: 0L
        )

        if (rule.id != 0L) {
            entity.id = rule.id
            expenseRuleDao.update(entity)
        } else {
            expenseRuleDao.insert(entity)
        }
    }

    override suspend fun delete(id: Long) {
        val rule = expenseRuleDao.getById(id)
        expenseRuleDao.delete(rule)
    }

    private fun convert(rule: ExpenseRule, category: Category): ExpenseRuleDto {
        return ExpenseRuleDto(
            id = rule.id!!,
            pattern = rule.pattern,
            category = CategoryDto(category.id, category.name)
        )
    }

}