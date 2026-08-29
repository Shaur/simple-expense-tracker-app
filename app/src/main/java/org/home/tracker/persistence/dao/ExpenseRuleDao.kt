package org.home.tracker.persistence.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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