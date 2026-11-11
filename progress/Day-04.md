# Day 4: Room Persistence and Flight History

**Date worked on:** 2026-11-11
**Status:** Completed
**Objective:** Introduce local database persistence using Room to store and retrieve summarized flight records, avoiding the unnecessary storage of every single telemetry tick.

### Relevant Context & Files

* `libs.versions.toml` / `build.gradle.kts`
* `data/local/FlightRecordEntity.kt`
* `data/local/FlightRecordDao.kt`
* `data/local/DroneDatabase.kt`
* `DroneTelemetryRepository.kt`
* `data/local/FakeFlightRecordDao.kt`

### Step-by-Step Tasks

* [x] Add Room dependencies (runtime, ktx, and compiler/ksp) to the Version Catalog and Gradle files.
* [x] Decide on the useful metrics to retain (e.g., flight ID, start/end times, duration, max altitude, average speed, min battery).
* [x] Define the Room `@Entity` representing this flight record.
* [x] Create the `@Dao` interface with methods to insert a record and query history (returning `Flow<List<FlightRecordEntity>>`).
* [x] Construct the Room `@Database` and provide an instance to the repository.
* [x] Update `DroneTelemetryRepository` to aggregate metrics during a flight and save the summary to Room when the flight ends (e.g., on disconnect or battery death).
* [x] Create a `FakeFlightRecordDao` to support unit testing without needing an Android emulator.

### Verification and Completion

* [x] App compiles and successfully generates the Room database schema.
* [x] A concise summary record is saved to the local database when a flight completes.
* [x] The application does *not* attempt to save every single telemetry tick (saving space and performance).
* [x] Temporary signal losses (5% drop probability) do not incorrectly split the flight into multiple database entries.

### Learning Notes

**Concepts understood:**

* Room architecture components (`@Entity`, `@Dao`, `@Database`).
* The purpose of `@Volatile` in a Singleton pattern to prevent thread-caching issues during database instantiation.
* Using Kotlin Flow operators (`onStart`, `onEach`, `onCompletion`) to silently calculate running statistics alongside the main data stream without modifying it.
* Using `withContext(NonCancellable)` to ensure quick database writes complete successfully even if the parent UI coroutine is cancelled (e.g., when the OS kills the app in the background).
* Understanding `room_master_table` as Room's internal mechanism for schema versioning and preventing data corruption.
* Designing a `FakeFlightRecordDao` using `MutableStateFlow` to mimic Room's asynchronous, auto-incrementing behavior for local unit testing.

**Decisions made:**

* Chose to manually construct the database dependency using a Singleton rather than introducing a heavy dependency injection framework like Hilt for now.
* Decided to only save a summarized record of the flight upon completion to keep the database lightweight and highly performant.

**Problems encountered:**

* Investigated what happens when the phone screen is locked: The `viewModelScope` remains active, meaning the flight does not fragment into multiple entries. A record is only saved if the battery hits 0% or the OS fully kills the app process.
* Clarified that 5-second simulated signal drops just delay the flow rather than cancelling it, perfectly reflecting a real-world temporary disconnect without creating separate database records.

**Next steps:**

* Create a new UI screen to display the list of past flights retrieved from the database, or look into adding a Map/GPS presentation layer to the main dashboard.