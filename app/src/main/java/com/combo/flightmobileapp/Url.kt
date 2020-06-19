package com.combo.flightmobileapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "url_table")
data class Url(
//    @PrimaryKey(autoGenerate = true) val id: Int,
//    @ColumnInfo(name = "url") val url: String

    @PrimaryKey val url: String,
    @ColumnInfo var number: Int = 0
)





