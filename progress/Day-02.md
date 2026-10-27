# Day 2 — Simulator and Connection Behavior

**Session date:** [Enter the date]
**Status:** Not started

## Objective

Improve the realism of the telemetry simulator and make connection
behavior more explicit.

Do not add new frameworks just for the sake of adding them.
Focus on improving the behavior of the existing implementation.

## Step 1 — Review the Current Implementation

Read:
- SimulatedTelemetrySource.kt
- TelemetrySource.kt
- DroneTelemetryRepository.kt
- DroneDashboardViewModel.kt
- DroneDashboardUiState.kt

Make sure you understand how telemetry travels from the simulator
to the screen.

## Step 2 — Improve the Simulator

Current problems:
- Battery remains at zero.
- GPS coordinates never change.
- Altitude and speed change randomly without much realism.

Tasks:
- Decide how battery depletion should behave.
- Make altitude and speed changes more plausible.
- Consider introducing small GPS movements.
- Keep the simulation simple enough to understand and test.

## Step 3 — Model Connection Behavior

The current screen marks the drone connected when telemetry arrives.

Design clearer behavior for:
- Initial connection
- Active telemetry
- Temporary disconnection
- Reconnection
- Error conditions

Decide how to represent these states in the application.

Do not assume that normal Flow completion always means disconnection.

## Step 4 — Simulate Disconnection and Recovery

If time permits:
- Simulate a temporary loss of telemetry.
- Display a disconnected state.
- Decide whether to retain the last-known telemetry.
- Restore telemetry and show reconnection.

Keep connection status separate from the last telemetry reading.

## Step 5 — Verify Behavior

Manually check:
- Telemetry continues updating when connected.
- Battery behavior is sensible.
- Disconnection is visible.
- Last-known readings behave as intended.
- Reconnection restores live updates.
- The screen remains stable during state changes.

## Step 6 — Record What Was Learned

Before finishing, update this file with:
- What was implemented
- Which files changed
- What concepts were learned
- What remains incomplete
- Any bugs or decisions to revisit

## Completion Checklist

- [ ] Reviewed the current implementation.
- [ ] Improved battery behavior.
- [ ] Improved altitude and speed simulation.
- [ ] Considered GPS movement.
- [ ] Modelled connection behavior more explicitly.
- [ ] Tested disconnection and recovery, if implemented.
- [ ] Recorded the session date.
- [ ] Updated these notes.

## Notes From This Session

### Implemented
[Fill in after working.]

### Concepts learned
[Fill in after working.]

### Problems encountered
[Fill in after working.]

### Next steps
[Fill in before finishing.]