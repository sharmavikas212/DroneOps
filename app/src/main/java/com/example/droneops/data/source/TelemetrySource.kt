package com.example.droneops.data.source

import com.example.droneops.data.model.DroneTelemetry
import kotlinx.coroutines.flow.Flow

interface TelemetrySource {
    fun observeTelemetry() : Flow<DroneTelemetry>
}