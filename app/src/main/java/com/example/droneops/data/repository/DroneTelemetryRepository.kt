package com.example.droneops.data.repository

import com.example.droneops.data.local.FlightRecordDao
import com.example.droneops.data.local.FlightRecordEntity
import com.example.droneops.data.model.DroneTelemetry
import com.example.droneops.data.source.TelemetrySource
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

class DroneTelemetryRepository(
    private val telemetrySource: TelemetrySource,
    private val flightRecordDao: FlightRecordDao
) {
    fun observeTelemetry(): Flow<DroneTelemetry> {
        var startTime = 0L
        var maxAlt = 0.0
        var avgSpeedSum = 0.0
        var speedCount = 0
        var minBattery = 100

        return telemetrySource.observeTelemetry()
            .onStart {
                startTime = System.currentTimeMillis()
            }
            .onEach { telemetry ->
                // Update our running statistics with every new telemetry packet
                maxAlt = maxOf(maxAlt, telemetry.altitudeMeters)
                avgSpeedSum += telemetry.speedMetersPerSecond
                speedCount++
                minBattery = minOf(minBattery, telemetry.batteryPercentage)
            }
            .onCompletion {
                // When the flow stops (drone disconnects or battery dies), save the record
                if (speedCount > 0) {
                    val endTime = System.currentTimeMillis()
                    val duration = ((endTime - startTime) / 1000).toInt()
                    val avgSpeed = avgSpeedSum / speedCount

                    val record = FlightRecordEntity(
                        startTimeMillis = startTime,
                        endTimeMillis = endTime,
                        durationSeconds = duration,
                        maxAltitudeMeters = maxAlt,
                        averageSpeedMps = avgSpeed,
                        minBatteryPercentage = minBattery
                    )

                    // NonCancellable ensures the database write completes even if the UI cancels the collection
                    withContext(NonCancellable) {
                        flightRecordDao.insertFlightRecord(record)
                    }
                }
            }
    }

    // Expose the database history to the rest of the app
    fun getFlightHistory(): Flow<List<FlightRecordEntity>>{
        return flightRecordDao.getAllFlightRecords()
    }
}