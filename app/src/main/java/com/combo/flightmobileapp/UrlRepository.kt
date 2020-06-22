package com.combo.flightmobileapp

import androidx.lifecycle.LiveData
import android.util.Log

class UrlRepository(private val urlDao: UrlDao) {

    //list of 5 urls
    val allUrls: LiveData<List<Url>> = urlDao.getAlphabetizedUrl()
    suspend fun insert(url: Url) {
        if (allUrls.value != null) { //if url exist in list
            if (!allUrls.value!!.isEmpty()) {
                val highestNum = allUrls.value!!.first().number
                url.number = highestNum + 1 //give timestamp to url record
            }
        }
        urlDao.insert(url)
    }

    //delete all urls from DB
    suspend fun deleteAll() {
        urlDao.deleteAll()
    }
}