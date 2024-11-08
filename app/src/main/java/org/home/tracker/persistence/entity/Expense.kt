package org.home.tracker.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
