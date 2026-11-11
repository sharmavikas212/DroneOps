# Day 3: Automated Testing

**Date worked on:** 2026-11-11
**Status:** Completed
**Objective:** Add unit tests for the ViewModel and Repository logic using a fake telemetry source to ensure state transitions behave predictably.

### Relevant Context & Files

* `DroneDashboardViewModelTest.kt`
* `FakeTelemetrySource.kt`
* `DroneDashboardViewModel.kt`
* `libs.versions.toml` / `build.gradle.kts`

### Step-by-Step Tasks

* [x] Review the existing local unit-test setup.
* [x] Add necessary dependencies for Coroutine and Flow testing (`kotlinx-coroutines-test`, Turbine).
* [x] Create a fake telemetry source (`FakeTelemetrySource`) using `MutableSharedFlow` to manually control data emissions.
* [x] Write a test verifying the ViewModel's initial state (`Connecting`, null telemetry).
* [x] Write a test verifying UI state updates correctly to `Connected` when a telemetry reading is emitted.
* [x] Write a test verifying the watchdog timer triggers a `Disconnected` state after 3 seconds of inactivity while retaining stale data.

### Verification and Completion

* [x] All tests execute deterministically without relying on real delays (using `StandardTestDispatcher`).
* [x] Test coverage includes standard operation and disconnections.
* [x] No random values are used in tests; all inputs are strictly controlled.

### Learning Notes

**Concepts understood:**

* Using `kotlinx-coroutines-test` (`runTest`, `StandardTestDispatcher`) to manipulate virtual time in tests, allowing delays (like the 3-second watchdog) to be skipped instantly using `advanceTimeBy()`.
* Using Turbine (`.test { awaitItem() }`) to cleanly and sequentially verify emissions from a `StateFlow` without writing custom collection loops.
* Using `MutableSharedFlow` in a fake data source to mimic a hot stream of data that tests can manually control.

**Decisions made:**

* Used a dedicated `FakeTelemetrySource` rather than a mocking framework (like Mockito/MockK) to provide a more authentic, controllable flow of data for the repository and ViewModel to consume.
* Added Turbine and Coroutines Test dependencies via the Version Catalog to keep dependency management clean and centralized.

**Problems encountered:**

* Navigating the setup of test dependencies in a modern Gradle project using `libs.versions.toml` (Version Catalogs) instead of raw string declarations.

**Incomplete work:**

* None for this milestone.

**Next steps:**

* Proceed to Milestone 4: Room Persistence and Flight History (defining entities, DAOs, and a local database).