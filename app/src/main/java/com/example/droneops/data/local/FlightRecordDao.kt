package com.example.droneops.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightRecordDao {

    // Suspend function because inserting to a database is I/O work
    @Insert
    suspend fun insertFlightRecord(record: FlightRecordEntity)

    // Room automatically provides this as an asynchronous Flow
    @Query("SELECT * FROM flight_records ORDER BY startTimeMillis DESC")
    fun getAllFlightRecords(): Flow<List<FlightRecordEntity>>
}