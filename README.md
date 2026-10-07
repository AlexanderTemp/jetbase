# JetBase

Android template project to bootstrap new repositories with an architecture that is already
decided. It takes the stack and the best patterns of [Seal](https://github.com/JunkFood02/Seal),
rewritten (not copied: Seal is GPL-3.0) and aligned with the current state of the art
(Now in Android and the official architecture recommendations, October 2026).

**Stack:** Kotlin 2.4 · AGP 9 (built-in Kotlin) · Jetpack Compose + Material 3 · Navigation 3 ·
Material 3 adaptive (navigation suite + list-detail) · Koin 4 · Room 2.8 · MMKV ·
kotlinx.serialization · MaterialKolor (color spec 2025) · ktfmt · JUnit4 + Turbine ·
Compose UI tests.

## Modules

```
app                    -> Application, MainActivity, NavDisplay, root Koin module, crash handler
core/model             -> domain models (pure Kotlin)
core/common            -> dispatchers, scopes, AppResult, TimeProvider (pure Kotlin)
core/navigation        -> NavigationState + Navigator (Navigation 3, one back stack per tab)
core/datastore         -> preferences (MMKV behind KeyValueStore)
core/database          -> Room: entities, DAOs, mappers
core/data              -> repositories (public interfaces, internal implementations)
core/designsystem      -> dynamic theme, typography, motion, components
core/ui                -> NavDisplay transitions, previews, model->UI mappings
core/testing           -> fakes, MainDispatcherRule, test utilities
feature/notes/api      -> public navigation keys of the notes feature
feature/notes/impl     -> sample feature (full CRUD, list-detail on tablets)
feature/settings/api   -> public navigation key of the settings feature
feature/settings/impl  -> appearance, dark theme, language, about
build-logic            -> convention plugins (jetbase.android.*)
```

Dependency rules: `feature:x:impl -> feature:x:api, core:data/ui/designsystem/navigation`;
another feature may only depend on `feature:y:api`; nobody depends on an `:impl` except `app`;
features never see `core:database` or `core:datastore`.

## Create a new project

```bash
git clone <this-template> my-app && cd my-app
rm -rf .git && git init
./scripts/rename.sh com.mycompany.myapp MyApp
./gradlew assembleDebug testDebugUnitTest
```

## Commands

| Command | What it does |
|---|---|
| `./gradlew assembleDebug` | debug APK (`com.jetbase.debug`) |
| `./gradlew testDebugUnitTest :core:common:test` | JVM unit tests |
| `./gradlew connectedDebugAndroidTest` | Compose UI tests on a device/emulator |
| `./gradlew ktfmtFormat` / `ktfmtCheck` | format / check formatting |
| `./gradlew lintDebug` | lint |
| `./gradlew assembleRelease` | R8-minified release APK (unsigned) |
