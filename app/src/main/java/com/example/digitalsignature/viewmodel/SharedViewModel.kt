package com.example.digitalsignature.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _filePath = MutableLiveData<String>()
    val filePath: LiveData<String> get() = _filePath

    fun setFilePaths(paths: String) {
        _filePath.value = paths
    }

}