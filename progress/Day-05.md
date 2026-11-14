# Day 5: Flight History UI and Navigation

**Date worked on:** [YYYY-MM-DD]
**Status:** Not Started
**Objective:** Build a Jetpack Compose screen to display the local database's flight history and implement navigation between the live dashboard and the history view.

### Relevant Context & Files

* `data/local/FlightRecordEntity.kt`
* `DroneTelemetryRepository.kt`
* (New) `presentation/history/FlightHistoryViewModel.kt`
* (New) `presentation/history/FlightHistoryScreen.kt`
* `MainActivity.kt` (for Compose Navigation setup)

### Step-by-Step Tasks

* [ ] Add the Jetpack Compose Navigation dependency (`androidx.navigation:navigation-compose`).
* [ ] Create a `FlightHistoryViewModel` that collects the database Flow from `DroneTelemetryRepository`.
* [ ] Build a `FlightHistoryScreen` using Compose (e.g., a `LazyColumn` of flight summary cards).
* [ ] Format the raw database data for the UI (e.g., converting UNIX timestamps to readable dates, formatting durations to MM:SS).
* [ ] Set up a `NavHost` in `MainActivity` to handle routing between the `DashboardScreen` and `FlightHistoryScreen`.
* [ ] Add a button or menu item to the main dashboard to navigate to the history screen.

### Verification and Completion

* [ ] The user can navigate seamlessly between the live dashboard and the history screen.
* [ ] The history screen automatically updates if a new flight finishes while the app is running.
* [ ] UI accurately displays the summarized metrics (duration, max altitude, min battery, etc.) saved from Day 4.

### Learning Notes

**Concepts understood:**
* 

**Decisions made:**
* 

**Problems encountered:**
* 

**Incomplete work:**
* 

**Next steps:**
* 