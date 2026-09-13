package org.home.tracker.persistence.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import org.home.tracker.persistence.entity.Category
import org.home.tracker.persistence.entity.ExpenseRule

@Dao
interface ExpenseRuleDao {

    @Query("select * from expense_rule er join category c on er.category_id = c.id")
    suspend fun findAll(): Map<ExpenseRule, Category>

    @Insert
    suspend fun insert(rule: ExpenseRule)

    @Update
    suspend fun update(rule: ExpenseRule)

    @Delete
    suspend fun delete(rule: ExpenseRule)

    @Query("select * from expense_rule where id = :id")
    fun getById(id: Long): ExpenseRule
}