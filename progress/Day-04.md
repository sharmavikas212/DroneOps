# Day 4: Room Persistence and Flight History

**Date worked on:** [YYYY-MM-DD]
**Status:** Not Started
**Objective:** Introduce local database persistence using Room to store and retrieve summarized flight records, avoiding the unnecessary storage of every single telemetry tick.

### Relevant Context & Files
*   `libs.versions.toml` / `build.gradle.kts` (for Room dependencies)
*   *New:* `data/local/FlightRecordEntity.kt`
*   *New:* `data/local/FlightRecordDao.kt`
*   *New:* `data/local/DroneDatabase.kt`
*   `DroneTelemetryRepository.kt`

### Step-by-Step Tasks
*   [ ] Add Room dependencies (runtime, ktx, and compiler/ksp) to the Version Catalog and Gradle files.
*   [ ] Decide on the useful metrics to retain (e.g., flight ID, start/end times, duration, max altitude, average speed, min battery).
*   [ ] Define the Room `@Entity` representing this flight record.
*   [ ] Create the `@Dao` interface with methods to insert a record and query history (returning `Flow<List<FlightRecordEntity>>`).
*   [ ] Construct the Room `@Database` and provide an instance to the repository.
*   [ ] Update `DroneTelemetryRepository` to aggregate metrics during a flight and save the summary to Room when the flight ends (e.g., on disconnect or battery death).
*   [ ] Add tests for the DAO and the new repository persistence logic.

### Verification and Completion
*   [ ] App compiles and successfully generates the Room database schema.
*   [ ] A concise summary record is saved to the local database when a flight completes.
*   [ ] The application does *not* attempt to save every single telemetry tick (saving space and performance).
*   [ ] Automated tests confirm the data is inserted and retrieved correctly.

### Learning Notes

**Concepts understood:**
*   *To be filled during development*

**Decisions made:**
*   *To be filled during development*

**Problems encountered:**
*   *To be filled during development*

**Incomplete work:**
*   *To be filled during development*

**Next steps:**
*   *To be filled during development*