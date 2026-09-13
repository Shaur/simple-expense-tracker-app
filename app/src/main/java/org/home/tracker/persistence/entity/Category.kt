package org.home.tracker.persistence.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "category")
data class Category(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo("name")
    var name: String
)
