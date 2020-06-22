package com.combo.flightmobileapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UrlViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UrlRepository
    val allUrls: LiveData<List<Url>>

    init {
        val urlsDao = UrlRoomDatabase.getDatabase(application, viewModelScope).urlDao()
        repository = UrlRepository(urlsDao)
        allUrls = repository.allUrls
    }

    //insert url to DB
    fun insert(url: Url) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(url)
    }

    fun updateList(url: Url) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(url)
    }

    //delete all urls from DB
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}