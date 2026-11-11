package com.example.droneops.presentation.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.droneops.data.local.DroneDatabase
import com.example.droneops.data.repository.DroneTelemetryRepository
import com.example.droneops.data.source.SimulatedTelemetrySource

class DroneDashboardViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((DroneDashboardViewModel::class.java))){

            // 1. Initialize the Room Database and DAO
            val database = DroneDatabase.getDatabase(context.applicationContext)
            val flightRecordDao = database.flightRecordDao()

            // 2. Initialize the Source
            val telemetrySource = SimulatedTelemetrySource()


            val repository = DroneTelemetryRepository(telemetrySource, flightRecordDao)

            @Suppress("UNCHECKED_CAST")
            return DroneDashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}