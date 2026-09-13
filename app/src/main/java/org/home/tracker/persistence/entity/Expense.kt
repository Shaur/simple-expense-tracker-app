package org.home.tracker.persistence.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "expense")
data class Expense(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo("date")
    var date: Long,

    @ColumnInfo("value")
    var value: Long,

    @ColumnInfo("currency_id")
    var currencyId: String,

    @ColumnInfo("category_id")
    var categoryId: Long,

    @ColumnInfo("comment")
    var comment: String
)
