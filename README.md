# Tyria Hub ⚔️

**Tyria Hub** is a companion app, touch deck, and real-time character/inventory inspector for **Guild Wars 2**, designed specifically for Android gaming handhelds (such as the **AYN Thor** dual-screen handheld), tablets, and secondary Android displays.

---

## ✨ Features

### 🎮 Dual-Screen Touch Deck
- **Thumb Wings Mode**: Ergonomically angled touch deck clusters (Left: Weapon Skills 1–5, Heal 6, Weapon Swap, Dodge; Right: Utilities 7–9, Elite 0, F1–F5 profession mechanics, Special Action Key, Mounts).
- **Combat Bar Mode**: Linear, compact battle deck modeled after the Guild Wars 2 HUD.
- **Mounts & QoL Grid**: Fast one-tap mount summoning macros (Raptor, Springer, Skimmer, Jackal, Griffon, Roller Beetle, Warclaw, Skyscale, Siege Turtle) plus Jade Bot Waypoint, Fishing, and Skiff toggles.
- **AYN Thor Second-Screen Integration**: Seamless multi-display targeting (`DisplayManager`), floating windowed overlay mode via `SYSTEM_ALERT_WINDOW`, and Android IME input method injection.

### 📐 Universal Interface Scaling (XS / MD / LG)
- **Centralized Scale Management (`DeckSizeConfig`)**: Instant dynamic scaling across the entire interface.
- Adapts font sizes, icon dimensions, button touch heights, padding, and inventory grid column counts (8 columns on XS, 7 on MD, 6 on LG) so nothing gets cut off on 16:9 or handheld aspect ratios.

### 🎒 Live Guild Wars 2 Character & Inventory Inspector
- **ArenaNet API v2 Integration**: Safely connects to official Guild Wars 2 API endpoints using official API keys.
- **Resilient Network Architecture**: Employs `supervisorScope` and explicit HTTP 401/403 authorization handling to ensure invalid tokens display friendly in-app warnings without crashing the app.
- **Bag & Item Grid**: Dynamic slot grid rendering Ascended, Legendary, and Exotic rarity borders, item icons, stack counts, and localized catalog lookups (`Gw2ItemCatalog`).
- **Currency Tickers**: Instant balance tracking for Gold, Silver, Copper, Karma, Laurels, Gems, and Wizard's Vault Astral Acclaim.
- **Dynamic Profession Sync**: Automatically adapts the active build template and UI accent color (Guardian Blue, Warrior Amber, Ranger Green, Necromancer Emerald, Thief Crimson, Elementalist Flame, Mesmer Violet, Engineer Bronze, Revenant Mist Red) to the active character.

### 🧪 Offline Example & Testing Data Mode
- Complete offline test mode (`ExampleData`) with 9 level-80 characters across all Guild Wars 2 professions, full Ascended/Legendary equipment sets, 5 bags per character, and currency pools.
- Completely isolated from network calls—test dual-screen layouts, inventory scaling, and touch responses without needing an active API key or Internet connection.

### ⏰ World Boss & Event Timers
- Live rotation tracker for Tyria World Bosses and Meta Events (Tequatl, Shatterer, Claw of Jormag, Triple Trouble, Chak Gerent, Octovine) with countdown timers and 1-tap chat waypoint broadcasting (`[&BDkDAAA=]`).

### 💬 Low-Latency Chat & Key Injection
- Direct key injection via `FastKeyInjector` with configurable delay ms (`chatMacroDelayMs`) for seamless waypoint broadcasting and macro execution in GW2 chat.

---

## 🏗️ Architecture & Technology Stack

- **Language**: Kotlin 2.0+
- **UI Toolkit**: Jetpack Compose with Material Design 3 (M3)
- **Architecture**: MVVM (Model-View-ViewModel) + StateFlow reactive UI patterns
- **Networking**: Retrofit 2 + OkHttp + Moshi (Kotlin serialization)
- **Coroutines**: Structured concurrency with Kotlin Coroutines (`supervisorScope`, `viewModelScope`, `Dispatchers.IO`)
- **System Services**:
  - `TyriaOverlayService` (`android.permission.SYSTEM_ALERT_WINDOW`, `FOREGROUND_SERVICE_SPECIAL_USE`)
  - `TyriaHubInputMethodService` (`android.view.InputMethod`)
  - `HapticsManager` (`android.os.Vibrator`)
- **Testing**: Robolectric JVM unit testing suite verifying state management, size configurations, and API safety.

---

## 📁 Project Structure

```
app/src/main/java/com/example/
├── MainActivity.kt                      # Main edge-to-edge entry point
└── tyriahub/
    ├── api/
    │   ├── Gw2ApiClient.kt              # Retrofit API client configuration
    │   └── Gw2ApiService.kt             # ArenaNet v2 endpoint definitions
    ├── data/
    │   ├── Gw2ItemCatalog.kt            # Offline item metadata resolver
    │   └── PresetBuilds.kt              # Pre-configured profession skill bars
    ├── haptics/
    │   └── HapticsManager.kt            # Precision haptic feedback controller
    ├── ime/
    │   └── TyriaHubInputMethodService.kt# Input Method Service for keystroke injection
    ├── input/
    │   └── FastKeyInjector.kt           # Low-latency root/shell key sender
    ├── model/
    │   ├── ExampleData.kt               # Rich offline testing account & characters
    │   └── Gw2Models.kt                 # Response DTOs and currency formatters
    ├── overlay/
    │   └── TyriaOverlayService.kt       # Dual-screen floating window service
    ├── storage/
    │   └── SecurePreferences.kt         # Local SharedPreferences manager
    ├── theme/
    │   ├── Color.kt & Theme.kt          # Material 3 dynamic color scheme
    │   ├── DeckSizeConfig.kt            # Universal XS / MD / LG scale system
    │   └── Gw2Themes.kt                 # Profession color palettes & dark mode
    └── ui/
        ├── MainViewModel.kt             # Central business logic & API supervisor
        └── components/
            ├── BossTimersView.kt        # World boss rotation & countdowns
            ├── CharactersView.kt        # Character details & account viewer
            ├── InventoryView.kt         # Bag slot grid & item inspector
            ├── SettingsDialog.kt        # Handheld, AYN Thor & API settings
            ├── SetupWizardDialog.kt     # First-launch onboarding wizard
            ├── TouchDeckView.kt         # Dual-screen touch skill deck
            ├── TyriaHubAppContent.kt    # Root navigation & overlay host
            └── WizardsVaultView.kt      # Astral Acclaim & objective tracker
```

---

## 🚀 Building & Running

### Prerequisites
- Android Studio Ladybug / Meerkat or Gradle 8.11+
- JDK 17 or JDK 21
- Android SDK 36 (minSdk 24)

### Build Commands
```bash
# Compile debug APK
./gradlew :app:assembleDebug

# Run Robolectric unit tests
./gradlew :app:testDebugUnitTest

# Install on connected device or emulator
./gradlew :app:installDebug
```

### 🤖 Automated GitHub Actions CI/CD & Releases
A GitHub Actions workflow is pre-configured at [`.github/workflows/build-apk.yml`](.github/workflows/build-apk.yml):
- Automatically triggers on every push and pull request to `main` / `master`.
- Executes the full Robolectric unit test suite and compiles the APK.
- Builds and packages `tyria-hub-debug-apk` as an artifact under the **Actions** tab.
- **Publishing Releases**:
  - **Automated**: Push a Git tag (e.g., `git tag v1.0.0 && git push origin v1.0.0`) or run the workflow manually with the *Publish a GitHub Release* checkbox enabled. It will automatically build the APK and publish a formatted GitHub Release with `tyria-hub.apk` attached.
  - **Manual**: Go to your repository on GitHub -> click **Releases** -> click **Draft a new release** -> choose a tag (e.g. `v1.0.0`) -> drag and drop your downloaded APK into the binaries box -> click **Publish release**.

---

## 🔒 Privacy & Permissions Notice

- **No Remote Telemetry**: Tyria Hub does not collect analytics or user telemetry.
- **API Key Security**: Guild Wars 2 API keys are stored exclusively on-device in Android private app storage (`EncryptedSharedPreferences` / `Context.MODE_PRIVATE`).
- **ArenaNet Fair Play**: Tyria Hub touch deck acts as standard keyboard/touch inputs and complies with ArenaNet's third-party tool policy (1 button press = 1 action).

---

## 📜 License
Distributed under the MIT License. Guild Wars 2 and ArenaNet are registered trademarks of ArenaNet, LLC. This project is not affiliated with or endorsed by ArenaNet.
