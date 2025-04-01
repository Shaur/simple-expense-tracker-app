package org.home.tracker.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Update
import org.home.tracker.dto.MonthlyExpenseDto
import org.home.tracker.dto.SummaryDto
import org.home.tracker.dto.WeeklyExpenseDto
import org.home.tracker.persistence.entity.Category
import org.home.tracker.persistence.entity.Expense

@Dao
interface ExpenseDao {

    @Query("select * from expense ex left join category c on ex.category_id = c.id order by date")
    suspend fun findAll(): Map<Expense, Category>

    @Query(
        """
        select * from expense ex 
            left join category c on ex.category_id = c.id 
        where ex.date >= :from and ex.date <= :to 
        order by date desc
        """
    )
    suspend fun findAll(from: Long, to: Long): Map<Expense, Category>

    @Query(
        """
        select * from expense ex 
            left join category c on ex.category_id = c.id 
        where ex.date >= :from and ex.date <= :to and ex.category_id = :categoryId
        order by date desc
        """
    )
    suspend fun findAll(from: Long, to: Long, categoryId: Long): Map<Expense, Category>

    @Query(
        """
            select 
                sum(ex.value) as value, 
                ex.currency_id as currency, 
                c.name as category,
                 ex.category_id as categoryId
            from expense ex
                left join category c on ex.category_id = c.id
            where ex.date >= :from and ex.date <= :to 
            group by ex.category_id, ex.currency_id
            order by sum(ex.value) desc
        """
    )
    suspend fun summary(from: Long, to: Long): List<SummaryDto>

    @Insert
    suspend fun insert(expense: Expense): Long

    @Update
    suspend fun update(expense: Expense)


    @Query(
        """
            select ex.currency_id as currency, sum(ex.value) as sum from expense ex
            where ex.date >= :from and ex.date <= :to 
            group by ex.currency_id
        """
    )
    suspend fun simpleSummary(from: Long, to: Long): Map<@MapColumn(columnName = "currency") String, @MapColumn(columnName = "sum") Long>

    @Query(
        """
            select 
                strftime('%m', DATE(date / 1000, 'unixepoch')) as month,
                strftime('%Y', DATE(date / 1000, 'unixepoch')) as year,
                currency_id as currency,
                sum(value) as value
            from expense group by year, month, currency
        """
    )
    suspend fun getByMonths(): List<MonthlyExpenseDto>

    @Query(
        """
            select 
                strftime('%W', DATE(date / 1000, 'unixepoch')) as week,
                strftime('%Y', DATE(date / 1000, 'unixepoch')) as year,
                currency_id as currency,
                sum(value) as value
            from expense group by year, week, currency
            having year + week > strftime('%Y', 'now') + strftime('%W', 'now') - :limit
            order by year, week desc
        """
    )
    suspend fun getByWeeks(limit: Int): List<WeeklyExpenseDto>
}