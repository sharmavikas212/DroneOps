package com.example.droneops.data.model

data class DroneTelemetry(
    val batteryPercentage: Int,
    val altitudeMeters: Double,
    val speedMetersPerSecond: Double,
    val latitude: Double,
    val longitude: Double
)
