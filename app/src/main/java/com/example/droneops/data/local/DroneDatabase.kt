package com.example.droneops.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FlightRecordEntity::class], version = 1, exportSchema = false)
abstract class DroneDatabase : RoomDatabase() {

    // Expose the DAO so the repository can use it
    abstract fun flightRecordDao(): FlightRecordDao

    companion object {
        @Volatile
        private var INSTANCE: DroneDatabase? = null

        fun getDatabase(context: Context): DroneDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DroneDatabase::class.java,
                    "drone_operations_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}