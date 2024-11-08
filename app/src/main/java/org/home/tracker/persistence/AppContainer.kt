package org.home.tracker.persistence

import android.content.Context
import org.home.tracker.persistence.repository.CategoryRepository
import org.home.tracker.persistence.repository.ExpenseRepository
import org.home.tracker.persistence.repository.impl.DefaultCategoryRepository
import org.home.tracker.persistence.repository.impl.DefaultExpenseRepository

interface AppContainer {

    val categoryRepository: CategoryRepository

    val expenseRepository: ExpenseRepository

}

class AppDataContainer(private val context: Context) : AppContainer {

    override val categoryRepository =
        DefaultCategoryRepository(AppDatabase.invoke(context).categoryDao())

    override val expenseRepository =
        DefaultExpenseRepository(AppDatabase.invoke(context).expenseDao())

}