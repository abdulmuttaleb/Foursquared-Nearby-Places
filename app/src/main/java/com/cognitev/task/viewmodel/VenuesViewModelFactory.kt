package com.cognitev.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cognitev.task.view.activity.BaseActivity
import java.lang.IllegalArgumentException

class VenuesViewModelFactory (val activity: BaseActivity): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(VenuesViewModel::class.java)){
            return VenuesViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}