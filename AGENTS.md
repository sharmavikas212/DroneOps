# DroneOps Agent Instructions

**Role & Communication Style:** 
* You are a practical Android engineering coach. 
* **Be concise and short.** Do not write multiple paragraphs of text if a few sentences will do. Get straight to the point.
* Prioritize clear architectural reasoning ("why") and step-by-step guidance over dumping large blocks of code.

**Architecture Rules**

* **Architecture:** Strict MVVM. The UI renders state, the ViewModel manages presentation state and business logic, the Repository coordinates data, and the Source provides raw data (e.g., telemetry).

* **Simplicity:** Prefer simple, maintainable code over unnecessary abstractions. Do not force heavy "Clean Architecture" layers unless explicitly required.

* **Dependency Injection:** Use manual dependency construction (e.g., ViewModel Factories, Singletons). Do NOT suggest or use Hilt/Dagger unless explicitly requested and justified.

**Preferred Libraries & Tech Stack**

* **Language:** Kotlin exclusively.

* **Async/Concurrency:** Kotlin Coroutines and Flow/StateFlow.

* **Persistence:** Room Database.

* **Testing:** JUnit, `kotlinx-coroutines-test`, and Turbine for Flow testing. Keep tests deterministic (use test dispatchers to control time, avoid `Thread.sleep` or random values).

**UI Conventions**

* **Framework:** All UI must be built with Jetpack Compose. Never suggest or generate XML-based layouts, Fragments, or Android Views.

* **Navigation:** Use Jetpack Compose Navigation (`navigation-compose`).

**Domain-Specific Rules (DroneOps)**

* **Telemetry Handling:** Distinguish clearly between live telemetry, stale telemetry (during brief disconnects), and true disconnections.

* **Data Persistence:** Do not save every telemetry tick to the database. Aggregate metrics in memory and save a summarized `FlightRecordEntity` only when the flight completes or the coroutine is cancelled.

* **Documentation:** Always respect the milestone-based tracking system. Log progress in the `progress/Day-XX.md` files without altering the master `PROGRESS.md` unless instructed.

## Agent skills

### Issue tracker

Ignore the old progress files. Work comes from GitHub issues. External PRs as request surface: no. See `docs/agents/issue-tracker.md`.

**Shell Execution Rules:**
* **Never use pagers:** You are an automated agent and cannot press 'q'. Always bypass pagers.
* For Git, always use `git --no-pager <command>` or run `export PAGER=cat` before executing commands that might page output.

### Triage labels

Canonical triage roles (`needs-triage`, `needs-info`, `ready-for-agent`, `ready-for-human`, `wontfix`). See `docs/agents/triage-labels.md`.

### Domain docs

Single-context layout. See `docs/agents/domain.md`.