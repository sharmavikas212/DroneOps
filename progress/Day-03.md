# Day 3: Automated Testing

**Date worked on:** [YYYY-MM-DD]
**Status:** Not Started
**Objective:** Add unit tests for the ViewModel and Repository logic using a fake telemetry source to ensure state transitions behave predictably.

### Relevant Context & Files
*   `DroneDashboardViewModel.kt`
*   `DroneTelemetryRepository.kt`
*   `SimulatedTelemetrySource.kt` (to be mocked/faked)
*   `build.gradle.kts` (for test dependencies)

### Step-by-Step Tasks
*   [ ] Review the existing local unit-test setup.
*   [ ] Add necessary dependencies for Coroutine and Flow testing (e.g., `kotlinx-coroutines-test`, Turbine).
*   [ ] Create a fake or controllable telemetry source (`FakeTelemetrySource`) that allows manual emission of telemetry and errors.
*   [ ] Write a test verifying the ViewModel's initial state (`Connecting`, null telemetry).
*   [ ] Write a test verifying UI state updates correctly when a telemetry reading is emitted.
*   [ ] Write a test verifying the watchdog timer triggers a `Disconnected` state after 3 seconds of inactivity.
*   [ ] Write a test verifying the state recovers to `Connected` when a new reading arrives after a disconnect.
*   [ ] Write a test verifying graceful stream completion and exception handling (catch block).

### Verification and Completion
*   [ ] All tests execute deterministically without relying on real delays (using `StandardTestDispatcher` or `UnconfinedTestDispatcher`).
*   [ ] Test coverage includes standard operation, disconnections, and error states.
*   [ ] No random values are used in tests; all inputs are strictly controlled.

### Learning Notes

**Concepts understood:**
*   [To be filled]

**Decisions made:**
*   [To be filled]

**Problems encountered:**
*   [To be filled]

**Incomplete work:**
*   [To be filled]

**Next steps:**
*   Proceed to Milestone 4: Room Persistence and Flight History.