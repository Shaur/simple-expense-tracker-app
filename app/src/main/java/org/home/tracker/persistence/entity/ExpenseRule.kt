package org.home.tracker.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_rule")
data class ExpenseRule(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo("pattern")
    var pattern: String,

    @ColumnInfo("category_id")
    var categoryId: Long

)
