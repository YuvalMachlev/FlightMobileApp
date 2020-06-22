package com.combo.flightmobileapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UrlDao {

    //get last 5 of urls
    @Query("SELECT * from url_table ORDER BY number DESC LIMIT 5")
    fun getAlphabetizedUrl(): LiveData<List<Url>>

    //insert to dataBase, replace exists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(url: Url)

    //delete
    @Query("DELETE FROM url_table")
    suspend fun deleteAll()


}