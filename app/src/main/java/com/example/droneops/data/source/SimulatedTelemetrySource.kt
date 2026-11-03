package com.example.droneops.data.source

import com.example.droneops.data.model.DroneTelemetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class SimulatedTelemetrySource : TelemetrySource {

    override fun observeTelemetry(): Flow<DroneTelemetry> = flow {
        // 1. Initial actual state
        var currentAltitude = 10.0
        var currentSpeed = 2.0

        // 2. Assign immediate random targets so it moves on tick 1
        var targetAltitude = Random.nextDouble(15.0, 40.0)
        var targetSpeed = Random.nextDouble(3.0, 12.0)
        var headingRadians = Random.nextDouble(0.0, 2 * Math.PI)

        var internalBattery = 100.0
        var currentLat = -33.8688
        var currentLon = 151.2093

        while (true) {
            // 1. Assign new targets including heading
            if (Random.nextFloat() < 0.20f && internalBattery > 0) {
                targetAltitude = Random.nextDouble(5.0, 40.0)
                targetSpeed = Random.nextDouble(0.0, 12.0)
                headingRadians = Random.nextDouble(0.0, 2 * Math.PI) // New random direction
            }

            if (internalBattery > 0) {
                val baseDrain = 0.05
                val speedDrain = currentSpeed * 0.02
                val altitudeDrain = currentAltitude * 0.005

                internalBattery -= (baseDrain + speedDrain + altitudeDrain)

                if (internalBattery <= 0) {
                    internalBattery = 0.0
                    targetSpeed = 0.0
                    targetAltitude = 0.0
                }
            } else {
                targetSpeed = 0.0
                targetAltitude = 0.0
            }

            // 2. Kinematic Smoothing
            currentAltitude += (targetAltitude - currentAltitude) * 0.15
            currentSpeed += (targetSpeed - currentSpeed) * 0.15

            // 3. GPS Movement Math
            // 1 degree of latitude is ~111,320 meters
            val deltaLat = (currentSpeed * cos(headingRadians)) / 111320.0
            val deltaLon = (currentSpeed * sin(headingRadians)) / (111320.0 * cos(Math.toRadians(currentLat)))

            currentLat += deltaLat
            currentLon += deltaLon

            // 4. Emit dynamic coordinates
            emit(
                DroneTelemetry(
                    batteryPercentage = internalBattery.toInt(),
                    altitudeMeters = currentAltitude,
                    speedMetersPerSecond = currentSpeed,
                    latitude = currentLat,
                    longitude = currentLon
                )
            )

            if (Random.nextFloat() < 0.05f) { // 5% chance to drop signal
                delay(5_000.milliseconds) // Sleep for 5 seconds, triggering the ViewModel's watchdog
            }

            delay(1_000.milliseconds)
        }
    }
}