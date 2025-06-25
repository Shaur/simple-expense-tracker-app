package org.home.tracker.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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