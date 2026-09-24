package org.home.tracker.ui.expense.rule.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.home.tracker.dto.CategoryDto
import org.home.tracker.dto.ExpenseRuleDto
import org.home.tracker.persistence.repository.CategoryRepository
import org.home.tracker.persistence.repository.ExpenseRuleRepository

class ExpenseRuleViewModel(
    private val expenseRuleRepository: ExpenseRuleRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val state = mutableStateOf(listOf<ExpenseRuleDto>())
    val categoryState = mutableStateOf(listOf<CategoryDto>())

    init {
        viewModelScope.launch {
            state.value = expenseRuleRepository.findAll()
            categoryState.value = categoryRepository.findAll()
        }
    }

    fun addEmpty() {
        state.value += ExpenseRuleDto(0L, "", categoryState.value.first())
    }

    fun save(rule: ExpenseRuleDto) {
        viewModelScope.launch {
            expenseRuleRepository.save(rule)
        }
    }

    fun delete(ruleId: Long?) {
        state.value = state.value.filter { it.id != ruleId }
        if (ruleId != null && ruleId != 0L) {
            viewModelScope.launch {
                expenseRuleRepository.delete(ruleId)
            }
        }
    }

}