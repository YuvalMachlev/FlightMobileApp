package com.combo.flightmobileapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "url_table")
data class Url(
    @PrimaryKey val url: String, //url address as string
    @ColumnInfo var number: Int = 0 //timestamp of record
)





