# DroneOps

DroneOps is an Android drone-operations dashboard built with Kotlin and Jetpack Compose. The latest build presents live simulated telemetry, connection health, GPS flight tracking, and persistent flight-session history in a dark Material 3 interface.

> **Project status:** DroneOps currently uses a simulated telemetry source. It is designed to demonstrate Android architecture, reactive state management, persistence, and testing; it is not connected to physical drone hardware yet.

## Latest demo

The screenshots below show the current dashboard and flight-history experience. The app displays live telemetry, a mapped flight path, battery status, and saved flight summaries.


### Current telemetry dashboard

The dashboard displays connection status, battery, altitude, speed, and GPS coordinates from the simulated telemetry stream.

![DroneOps live telemetry dashboard](https://github.com/user-attachments/assets/2d9b5f6e-16b5-4474-b692-c6985452ff0a)

### Current flight history

Completed telemetry sessions are summarized and persisted locally with Room, including duration, maximum altitude, average speed, and minimum battery level.

![DroneOps flight history](https://github.com/user-attachments/assets/32e58ef5-5116-4f00-ab29-5c9cf2bd0a77)

### App walkthrough video

Watch the short walkthrough to see the dashboard and flight-history flow in action:

https://github.com/user-attachments/assets/968624d1-5ffe-41c7-a8f4-e061bdca930d

## Features

- Live telemetry dashboard with a mapped flight path and:
  - Battery percentage
  - Altitude
  - Speed
  - GPS coordinates
  - Connection status
- Connection watchdog that detects missing telemetry and marks the drone as disconnected
- Last-known telemetry remains visible during a temporary connection loss
- Simulated drone movement with changing altitude, speed, heading, GPS position, and battery drain
- Automatic flight-session summaries when a telemetry stream ends
- Local flight-history persistence using Room
- Navigation between the dashboard and flight-history screens
- Dark Material 3 UI for the current Android dashboard experience
- Unit tests for ViewModel state transitions, connection loss, and history formatting

## Architecture

The project follows a layered MVVM structure:

```text
UI (Jetpack Compose)
        |
ViewModels + StateFlow
        |
Repository
   /             \
Telemetry Source   Room DAO
(simulated)       (local history)
```

### Main layers

- **Presentation:** Compose screens, UI state models, ViewModels, and Navigation Compose
- **Domain/data flow:** Repository coordinates telemetry collection and flight-session aggregation
- **Data sources:** A pluggable `TelemetrySource` interface currently backed by `SimulatedTelemetrySource`
- **Persistence:** Room database and DAO expose flight history as a Kotlin `Flow`
- **Testing:** Fake telemetry and DAO implementations enable fast local JVM tests without Android or SQLite setup

## Technical highlights

- Kotlin Coroutines and Flow for asynchronous telemetry streams
- `StateFlow` for lifecycle-aware UI state
- A cancellable three-second watchdog for connection monitoring
- `NonCancellable` persistence during stream completion so a completed flight record is not lost when UI collection is cancelled
- Room for local storage of flight summaries
- Turbine and coroutine test dispatchers for deterministic Flow/ViewModel tests
- KSP for Room code generation

## Tech stack

- Kotlin
- Android SDK
- Jetpack Compose and Material 3
- AndroidX ViewModel and Navigation Compose
- Kotlin Coroutines and Flow
- Room
- KSP
- JUnit
- Turbine

## Project structure

```text
app/src/main/java/com/example/droneops/
├── data/
│   ├── local/          # Room database, entities, and DAO
│   ├── model/          # Telemetry models
│   ├── repository/     # Telemetry and flight-history coordination
│   └── source/         # Telemetry source abstraction and simulator
├── presentation/
│   ├── dashboard/      # Live telemetry dashboard and ViewModel
│   └── history/        # Flight-history screen and ViewModel
└── ui/theme/           # Compose theme configuration
```

## Getting started

### Requirements

- Android Studio with Kotlin and Android SDK support
- JDK 11 or later
- Android SDK configured for the project

### Build the project

```bash
./gradlew assembleDebug
```

### Run unit tests

```bash
./gradlew test
```

Install the generated debug APK on an emulator or Android device from:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Testing

The test suite covers:

- Initial dashboard connection state
- Transition from connecting to connected when telemetry arrives
- Disconnection after telemetry stops for three seconds
- Retention of the last known telemetry during disconnection
- Empty flight-history state
- Formatting and display of saved flight records

## Future improvements

- Connect a real telemetry protocol or drone SDK
- Add dependency injection with Hilt
- Improve dashboard visualizations and accessibility
- Add configurable telemetry timeouts
- Add post-flight charts and reports
- Add instrumentation coverage for the complete navigation flow

## License

This project is currently intended for learning and portfolio demonstration. A formal open-source license can be added when distribution terms are defined.