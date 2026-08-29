package org.home.tracker.persistence.repository

import org.home.tracker.dto.ExpenseRuleDto

interface ExpenseRuleRepository {

    suspend fun findAll(): List<ExpenseRuleDto>

    suspend fun save(rule: ExpenseRuleDto)

    suspend fun delete(id: Long)
}