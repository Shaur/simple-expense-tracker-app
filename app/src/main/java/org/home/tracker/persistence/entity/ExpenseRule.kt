package org.home.tracker.persistence.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "expense_rule")
data class ExpenseRule(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo("pattern")
    var pattern: String,

    @ColumnInfo("category_id")
    var categoryId: Long

)
