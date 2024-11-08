package org.home.tracker.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo("name")
    var name: String
)
