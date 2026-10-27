package com.example.droneops.data.source

import com.example.droneops.data.model.DroneTelemetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class SimulatedTelemetrySource : TelemetrySource {

    override fun observeTelemetry(): Flow<DroneTelemetry> = flow {
        var battery = 100
        var altitude = 10.0
        var speed = 2.0

        while (true) {
            emit(
                DroneTelemetry(
                    batteryPercentage = battery,
                    altitudeMeters = altitude,
                    speedMetersPerSecond = speed,
                    latitude = -33.8688,
                    longitude = 151.2093
                )
            )

            battery = (battery - 1).coerceAtLeast(0)
            altitude = (altitude + (-1..1).random()).coerceAtLeast(0.0)
            speed = (speed + (-1..1).random()).coerceAtLeast(0.0)

            delay(1_000.milliseconds)
        }
    }
}