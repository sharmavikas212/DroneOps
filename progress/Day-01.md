# Day 1 — Basic Telemetry Dashboard

**Session date:** 27/10/2026
**Status:** Completed

## 1. Objective

Build a working Android dashboard that receives simulated drone telemetry
and displays it using Jetpack Compose.

## 2. Architecture

Data flow:

SimulatedTelemetrySource
|
v
TelemetrySource interface
|
v
DroneTelemetryRepository
|
v
DroneDashboardViewModel
|
v
DroneDashboardUiState (StateFlow)
|
v
DroneDashboardScreen (Compose)

The goal is to separate data generation, data access, UI state management,
and presentation.

## 3. Project Structure

Current expected structure:

    com.example.droneops/
    ├── data/
    │   ├── model/
    │   │   └── DroneTelemetry.kt
    │   ├── source/
    │   │   ├── TelemetrySource.kt
    │   │   └── SimulatedTelemetrySource.kt
    │   └── repository/
    │       └── DroneTelemetryRepository.kt
    ├── presentation/
    │   └── dashboard/
    │       ├── DroneDashboardScreen.kt
    │       ├── DroneDashboardViewModel.kt
    │       ├── DroneDashboardUiState.kt
    │       └── DroneDashboardViewModelFactory.kt
    └── MainActivity.kt

Note: Replace com.example.droneops with the actual package name if different.

## 4. Telemetry Model

File: data/model/DroneTelemetry.kt

Represents one telemetry reading.

Properties:
- batteryPercentage: Int
- altitudeMeters: Double
- speedMetersPerSecond: Double
- latitude: Double
- longitude: Double

A data class provides a convenient way to represent and pass around
the drone's telemetry values.

## 5. TelemetrySource Interface

File: data/source/TelemetrySource.kt

    interface TelemetrySource {
        fun observeTelemetry(): Flow<DroneTelemetry>
    }

This defines how the application observes telemetry without tying
the rest of the application to a particular data source.

Later, the simulator could be replaced by a real drone SDK or another
telemetry provider.

## 6. SimulatedTelemetrySource

File: data/source/SimulatedTelemetrySource.kt

Implemented a Flow-based telemetry simulator.

Initial values:
- Battery: 100%
- Altitude: 10 metres
- Speed: 2 metres per second
- GPS: Fixed Sydney coordinates

Current behavior:
- Emits telemetry every second.
- Decreases battery by one per emission.
- Randomly adjusts altitude and speed.
- Keeps GPS coordinates fixed.

### Kotlin and Flow concepts learned

- flow { } creates a cold Flow.
- A cold Flow starts executing when collected.
- emit() sends a value downstream.
- collect() consumes the Flow.
- delay(1000) suspends the coroutine for one second.
- delay() does not block the thread like Thread.sleep().
- Cancelling collection can cancel the Flow's coroutine work.

### Current limitations

- Battery remains at zero once depleted.
- GPS coordinates do not move.
- Altitude and speed changes are simplistic.
- Connection loss and recovery are not simulated yet.

These limitations will be addressed in a later milestone.

## 7. Repository

File: data/repository/DroneTelemetryRepository.kt

The repository receives a TelemetrySource through its constructor
and exposes observeTelemetry().

Currently, it forwards the source's Flow.

The important design decision is that the repository depends on
an interface rather than directly depending on the simulator.

This makes it easier to replace the data source later.

## 8. UI State

File: presentation/dashboard/DroneDashboardUiState.kt

Properties:
- telemetry: DroneTelemetry?
- isConnected: Boolean
- isLoading: Boolean
- errorMessage: String?

Important distinctions:

- A null telemetry value means no reading has arrived yet.
- Connection status is separate from telemetry data.
- Loading describes the initial loading state.
- Error messages describe failures that the UI may need to display.

Later, the dashboard should be able to retain the last-known telemetry
while clearly indicating that the drone is disconnected.

## 9. ViewModel

File: presentation/dashboard/DroneDashboardViewModel.kt

Responsibilities:
- Starts observing telemetry during initialization.
- Collects the repository's Flow inside viewModelScope.
- Updates UI state when telemetry arrives.
- Marks the connection active when telemetry arrives.
- Clears loading and previous errors after successful readings.
- Handles Flow exceptions and updates the error state.

### State management

- MutableStateFlow stores mutable state.
- _uiState is private.
- uiState exposes a read-only StateFlow.
- update { } creates a new state based on the previous state.
- viewModelScope ties coroutine work to the ViewModel lifecycle.

The UI observes state instead of modifying the ViewModel's internal state.

### Current limitation

The current implementation handles Flow exceptions, but normal Flow
completion does not automatically mean the drone disconnected.

Connection behavior needs to be modelled more explicitly.

## 10. ViewModel Factory

File: presentation/dashboard/DroneDashboardViewModelFactory.kt

The ViewModel requires a repository through its constructor.

The factory tells Android how to create the ViewModel with that dependency.

MainActivity currently creates:
1. SimulatedTelemetrySource
2. DroneTelemetryRepository
3. DroneDashboardViewModelFactory
4. DroneDashboardViewModel through ViewModelProvider

ViewModelProvider can reuse the existing ViewModel across Activity
recreation, such as device rotation.

Dependency creation is currently manual. Hilt may be considered later.

## 11. Compose Dashboard

File: presentation/dashboard/DroneDashboardScreen.kt

The screen observes UI state using:

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

It displays:
- DroneOps title
- Dashboard heading
- Connection status
- Battery percentage
- Altitude
- Speed

Placeholder values are displayed when telemetry has not arrived.

MainActivity hosts the screen inside DroneOpsTheme using setContent.

## 12. MainActivity

MainActivity creates the source, repository, factory, and ViewModel.

It then displays DroneDashboardScreen inside DroneOpsTheme.

enableEdgeToEdge() allows application content to extend behind system bars.
It is separate from the ViewModel and telemetry architecture.

## 13. Verification

- [x] App compiles and launches.
- [x] Dashboard displays telemetry.
- [x] Battery decreases every second.
- [x] Altitude and speed fluctuate.
- [x] Connection status is displayed.
- [x] Rotated the emulator/device.
- [x] App remained stable after rotation.
- [x] Telemetry continued updating after rotation.

## 14. Key Architectural Lessons

1. Interfaces separate contracts from implementations.
2. The repository separates data access from presentation logic.
3. The ViewModel manages screen state.
4. StateFlow allows the UI to observe state changes.
5. Compose renders the current UI state.
6. ViewModel lifecycle and UI lifecycle are related but different.
7. MVVM and Clean Architecture are not the same thing.

## Next Milestone

See Day-02.md for the next session's tasks.