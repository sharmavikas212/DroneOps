package com.example.droneops.data.repository

import com.example.droneops.data.model.DroneTelemetry
import com.example.droneops.data.source.TelemetrySource
import kotlinx.coroutines.flow.Flow

class DroneTelemetryRepository(
    private val telemetrySource: TelemetrySource
) {
    fun observeTelemetry(): Flow<DroneTelemetry> =
         telemetrySource.observeTelemetry()
}