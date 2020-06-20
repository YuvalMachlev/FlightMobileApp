package com.combo.flightmobileapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UrlDao {

    @Query("SELECT * from url_table ORDER BY number DESC LIMIT 5")
    fun getAlphabetizedUrl(): LiveData<List<Url>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(url: Url)

    @Query("DELETE FROM url_table")
    suspend fun deleteAll()

//    @Query("DELETE FROM url_table WHERE url = ")
//    suspend fun deleteCertain()
}