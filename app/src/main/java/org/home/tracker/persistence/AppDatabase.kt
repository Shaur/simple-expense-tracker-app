package org.home.tracker.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.home.tracker.persistence.dao.CategoryDao
import org.home.tracker.persistence.dao.ExpenseDao
import org.home.tracker.persistence.entity.Category
import org.home.tracker.persistence.entity.Currency
import org.home.tracker.persistence.entity.Expense

@Database(
    entities = [Category::class, Currency::class, Expense::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "expense-tracker-database"
        ).build()
    }

}