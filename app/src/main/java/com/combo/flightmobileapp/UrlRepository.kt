package com.combo.flightmobileapp

import androidx.lifecycle.LiveData
import android.util.Log

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class UrlRepository(private val urlDao: UrlDao) {
//    private var pri : Int = 0

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allUrls: LiveData<List<Url>> = urlDao.getAlphabetizedUrl()
    suspend fun insert(url: Url) {
//        pri += 1
//        url.number = pri
//        allUrls.value!!.size


//        val num = allUrls.value?.size
//        if (num != null) {
//            url.number = num
//        }
//        val num2 = allUrls.value?.last()?.url
//        if (num2 != null) {
//            url.number = num2
//        }
        if(allUrls.value != null) {
            if(!allUrls.value!!.isEmpty()) {
                val highestNum = allUrls.value!!.first().number
                url.number = highestNum + 1
                println(highestNum)
            }
        }
//        val highestNum = allUrls.value?.first()?.number
//        if (highestNum != null) {
//            url.number = highestNum + 1
//        }
//        println(highestNum)

        urlDao.insert(url)
    }
    suspend fun deleteAll() {
        urlDao.deleteAll()
    }
}