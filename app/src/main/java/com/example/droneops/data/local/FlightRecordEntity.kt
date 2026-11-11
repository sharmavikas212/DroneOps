package com.example.droneops.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flight_records")
data class FlightRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTimeMillis: Long,
    val endTimeMillis: Long,
    val durationSeconds: Int,
    val maxAltitudeMeters: Double,
    val averageSpeedMps: Double,
    val minBatteryPercentage: Int
)