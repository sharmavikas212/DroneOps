package com.example.droneops.data.source

import com.example.droneops.data.model.DroneTelemetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class FakeTelemetrySource : TelemetrySource {
    private val telemetryChannel = kotlinx.coroutines.channels.Channel<DroneTelemetry>(kotlinx.coroutines.channels.Channel.UNLIMITED)

    override fun observeTelemetry(): Flow<DroneTelemetry> {
        return telemetryChannel.receiveAsFlow()
    }

    suspend fun emitTelemetry(telemetry: DroneTelemetry){
        telemetryChannel.send(telemetry)
    }

    fun completeStream() {
        telemetryChannel.close()
    }
}