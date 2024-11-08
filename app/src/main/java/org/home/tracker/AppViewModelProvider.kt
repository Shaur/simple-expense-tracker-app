package org.home.tracker

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.home.tracker.ui.expense.viewmodel.ExpenseViewModel
import org.home.tracker.ui.summary.viewmodel.SummaryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ExpenseViewModel(
                categoryRepository = expenseTrackerApplication().container.categoryRepository,
                expenseRepository = expenseTrackerApplication().container.expenseRepository
            )
        }

        initializer {
            SummaryViewModel(
                expenseRepository = expenseTrackerApplication().container.expenseRepository
            )
        }
    }
}

fun CreationExtras.expenseTrackerApplication(): ExpenseTrackerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ExpenseTrackerApplication)