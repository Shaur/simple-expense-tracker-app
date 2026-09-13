package org.home.tracker.persistence.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "currency")
data class Currency(

    @PrimaryKey
    val id: Short,

    @ColumnInfo("code")
    val code: String,

    @ColumnInfo("name")
    val name: String
)
