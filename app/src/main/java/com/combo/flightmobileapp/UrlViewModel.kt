package com.combo.flightmobileapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UrlViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UrlRepository
    // Using LiveData and caching what getAlphabetizedUrl's returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allUrls: LiveData<List<Url>>

    init {
        val urlsDao = UrlRoomDatabase.getDatabase(application, viewModelScope).urlDao()
        repository = UrlRepository(urlsDao)
        allUrls = repository.allUrls
    }


    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(url: Url) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(url)
    }

    fun updateList(url: Url) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(url)
    }
}