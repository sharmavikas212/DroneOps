# Day 2: Realistic Telemetry and Connection Handling

**Date worked on:** 2026-11-03
**Status:** Completed
**Objective:** Improve the realism of the simulated telemetry and implement explicit connection state handling, including disconnections and reconnections.

### Relevant Context & Files
*   `SimulatedTelemetrySource.kt`
*   `DroneDashboardUiState.kt`
*   `DroneDashboardViewModel.kt`
*   `DroneDashboardScreen.kt`

### Step-by-Step Tasks
*   [x] Review the existing telemetry model and simulator logic.
*   [x] Improve simulated battery behavior (depletion based on load, auto-land at 0%).
*   [x] Implement kinematic smoothing for altitude and speed using linear interpolation.
*   [x] Implement basic geographic coordinate shifts (GPS displacement) based on speed and heading.
*   [x] Replace boolean `isConnected` with explicit `ConnectionState` enum (`Connecting`, `Connected`, `Disconnected`).
*   [x] Implement a 3-second watchdog timer in the ViewModel using coroutine jobs to handle connection timeouts.
*   [x] Simulate temporary connection loss (5% chance of a 5-second pause) in the data source.
*   [x] Update Compose UI to handle explicit connection states and retain last-known telemetry when disconnected.

### Verification and Completion
*   [x] App launches and initially shows "Connecting...".
*   [x] Telemetry values update smoothly without erratic jumps.
*   [x] Disconnection state triggers automatically when the stream pauses, freezing the last known values on screen.
*   [x] Reconnection happens seamlessly when the stream resumes.

### Learning Notes

**Concepts understood:**
*   Using linear interpolation (lerp) to smoothly transition data values within a Flow loop.
*   Calculating basic geographic coordinate shifts using speed and trigonometric functions.
*   Using cancellable Coroutine Jobs (`Job.cancel()`) to build a resetting watchdog timer for debouncing/timeouts.
*   Designing UI state structures that cleanly decouple network connection status from the core data payload.

**Decisions made:**
*   Opted for the explicit `Job.cancel()` approach for the watchdog timer rather than a continuous timestamp loop, as it makes the intent highly readable and idiomatic.
*   Decided the app (ViewModel) should declare the disconnect locally based on radio silence rather than waiting for an explicit "disconnected" event from the stream.

**Problems encountered:**
*   *Issue:* The simulation initially paused on the first tick because the target and current values started identically. 
    *Fix:* Initialized random targets outside the main loop.
*   *Issue:* The initial connection state hung indefinitely if the drone was turned off/out of range on launch, as the watchdog only started *after* the first reading.
    *Fix:* Called `resetWatchdog()` immediately before the `.collect()` block to enforce a strict timeout on the initial connection attempt.

**Incomplete work:**
*   None.

**Next steps:**
*   Proceed to Day 3: Automated Testing.