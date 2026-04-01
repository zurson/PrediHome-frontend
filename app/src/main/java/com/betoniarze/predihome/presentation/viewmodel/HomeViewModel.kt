package com.betoniarze.predihome.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _homeCounter: MutableLiveData<Int> = MutableLiveData(0)
    val homeCounter: LiveData<Int> = _homeCounter
}