# Fabled — Open Source Novel Writing Software

A full-featured, local-first novel-writing desktop application built with **Kotlin Multiplatform** and **Compose Multiplatform**.

> **Philosophy:** Calm surface, deep water.

---

## ✨ Features

### Drafting Environment
- **Zen Drafting Mode** — full-screen, distraction-free writing
- **Scene-based writing** — chapters and scenes as movable units
- **Adaptive Sidebar** — collapses when idle, expands on hover
- **Auto word count** — live tracking per scene and project
- **Scene summaries** — track narrative beats per scene

### Proactive Assistant (Three-Mode System)
| Mode | Behaviour |
|------|-----------|
| **Soft** | Minimal suggestions, essential continuity alerts only |
| **Moderate** | World-building prompts, subplot gap detection, pacing hints |
| **Bold** | Alternate scene ideas, missing setups/payoffs, structural improvements |

- One-click mode toggle, persisted per project
- Suggestion cards with severity levels (info / warning / critical)
- Contextual Insight Panel with character, location, and timeline data

### World-Building System
- Auto-generated world bible (characters, locations, factions, magic/tech systems)
- Character sheets with traits, arcs, motivations, secrets
- Location registry
- Timeline visualisation

### Plot & Structure Tools
- Beat detection (inciting incident, midpoint, climax)
- Subplot tracker
- Pacing tools (word count, emotional beats, POV)

### Organisation & Workflow
- Project dashboard with word-count progress
- Create, open, and delete projects
- Global navigation across Drafting ↔ World-Building ↔ Characters ↔ Timeline

### Privacy & Storage
- **Local-first** — all data stored in a local SQLite database
- No server backend, fully offline capable
- Encrypted project support (planned)

---

## 🏗 Architecture

```
fabled/
├── composeApp/          # Compose Desktop UI + ViewModels + Koin DI
│   └── src/
│       ├── commonMain/  # Screens, components, ViewModels, DI modules
│       └── desktopMain/ # Application entry point (main.kt)
├── shared/              # Domain layer (models, repositories, use cases)
│   └── src/
│       ├── commonMain/  # Pure Kotlin domain logic
│       └── commonTest/  # Unit tests
└── data/                # Data layer (SQLDelight, repository implementations)
    └── src/
        ├── commonMain/  # Repository implementations + SQLDelight queries
        └── jvmMain/     # JVM SQLite driver factory
```

### Layers
- **Presentation** — Compose Multiplatform screens + ViewModels (StateFlow)
- **Domain** — Pure Kotlin use cases + repository interfaces + domain models
- **Data** — SQLDelight queries + repository implementations

### Key Libraries
| Library | Version | Purpose |
|---------|---------|---------|
| Kotlin Multiplatform | 2.0.21 | Shared logic across targets |
| Compose Multiplatform | 1.7.3 | Desktop UI |
| SQLDelight | 2.0.2 | Type-safe SQLite queries |
| Koin | 4.0.4 | Dependency injection |
| Kotlinx Coroutines | 1.8.1 | Async + Flow |
| Kotlinx Serialization | 1.7.3 | JSON serialization |

---

## 🚀 Getting Started

### Prerequisites
- JDK 17+
- No additional setup required — Gradle Wrapper is included

### Build & Run

```bash
# Build all modules
./gradlew build

# Run the desktop application
./gradlew :composeApp:run

# Run unit tests
./gradlew :shared:jvmTest
```

---

## 🧪 Tests

Unit tests live in `shared/src/commonTest` and cover:

- `CreateProjectUseCaseTest` — project creation validation
- `GenerateSuggestionUseCaseTest` — assistant suggestion logic per mode
- `SceneTest` — word-count calculation
- `AssistantModeTest` — mode comparison and behaviour

```bash
./gradlew :shared:jvmTest
```

---

## 📁 Data Models

| Model | Description |
|-------|-------------|
| `Project` | Top-level novel project |
| `Chapter` | Ordered chapter within a project |
| `Scene` | Writing unit within a chapter |
| `Character` | Character sheet (traits, arc, relationships) |
| `Location` | Named location with description |
| `Item` | Significant item in the story |
| `Faction` | Group or organisation |
| `Timeline` | Timeline event |
| `Note` | Inline or project-level notes |
| `Tag` | Smart tag (POV, era, theme, beat) |
| `AssistantSuggestion` | Proactive assistant recommendation |
| `ContinuityFlag` | Detected continuity issue |

---

## 🗺 Roadmap

- [ ] Scene drag-and-drop reordering
- [ ] Character relationship map visualisation
- [ ] Export to manuscript / outline / synopsis
- [ ] Moodboard (images, colour palettes)
- [ ] Character Voice Chat
- [ ] Encrypted projects
- [ ] Optional cloud sync

---

## 📄 License

Apache License 2.0 — see [LICENSE](LICENSE).

