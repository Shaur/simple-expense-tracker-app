package org.home.tracker.persistence.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import org.home.tracker.persistence.entity.Category

@Dao
interface CategoryDao {

    @Query("select c.* from category c left join expense ex on ex.category_id = c.id group by c.id order by count(ex.id) desc")
    suspend fun findAll(): List<Category>

    @Insert
    suspend fun insert(category: Category): Long

    @Update
    suspend fun update(category: Category)
}