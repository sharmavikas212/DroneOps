package com.example.droneops.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.droneops.data.repository.DroneTelemetryRepository

class DroneDashboardViewModelFactory(
    private val repository: DroneTelemetryRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((DroneDashboardViewModel::class.java))){
            @Suppress("UNCHECKED_CAST")
            return DroneDashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}