package org.home.tracker.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class Currency(

    @PrimaryKey
    val id: Short,

    @ColumnInfo("code")
    val code: String,

    @ColumnInfo("name")
    val name: String
)
