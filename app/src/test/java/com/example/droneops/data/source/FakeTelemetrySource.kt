package com.example.droneops.data.source

import com.example.droneops.data.model.DroneTelemetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeTelemetrySource : TelemetrySource {
    private val telemetryFlow = MutableSharedFlow<DroneTelemetry>()

    override fun observeTelemetry(): Flow<DroneTelemetry> {
        return telemetryFlow
    }

    // Helper method called exclusively by your test cases
    suspend fun emitTelemetry(telemetry: DroneTelemetry){
        telemetryFlow.emit(telemetry)
    }
}