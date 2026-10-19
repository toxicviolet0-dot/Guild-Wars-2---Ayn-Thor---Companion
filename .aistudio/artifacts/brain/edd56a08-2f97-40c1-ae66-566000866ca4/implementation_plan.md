# Tyria Hub: Guild Wars 2 Dual-Screen Companion for AYN Thor

A high-performance, native Android (Kotlin + Jetpack Compose) touch control deck and companion app for the AYN Thor handheld, designed to replace the unreliable web PWA with ultra-low latency, native haptic feedback, dynamic profession theming, and a seamless dual-screen interface matching the exact layout and ergonomics designed for Guild Wars 2 in DroidDeck.

### User Review & Critical Decisions

> [!IMPORTANT]
> The following decisions directly incorporate your visual design reference, class theming requirement, and ergonomics:

- **Confirmed Decision 1 (Authentic Dynamic GW2 Profession Theming)**:
  - When switching builds or characters, the entire UI theme (mechanics banner, button borders, active glow halos, badges, and accents) dynamically adapts to the selected class's signature color scheme:
    - **Guardian / Dragonhunter**: Virtuous Guardian Blue (`#199BD9` / `#00B3FF`)
    - **Ranger / Galeshot / Soulbeast**: Verdurous Emerald Green (`#4EBA33` / `#62D943`)
    - **Warrior / Berserker / Bladesworn**: Warlord Amber / Gold-Orange (`#FF9900` / `#FFB733`)
    - **Revenant / Herald / Renegade**: Mist Carmine / Dark Red (`#C83737` / `#D9455B`)
    - **Thief / Daredevil / Deadeye**: Shadow Maroon & Amethyst (`#C45163` / `#D46377`)
    - **Engineer / Holosmith / Mechanist**: Cog Bronze & Brass (`#C47A2C` / `#E69138`)
    - **Elementalist / Weaver / Catalyst**: Prismatic Flame Scarlet (`#EC5A35` / `#FF7B54`)
    - **Mesmer / Chronomancer / Virtuoso**: Phantasmagoric Royal Purple & Magenta (`#A442C2` / `#BF5FE0`)
    - **Necromancer / Reaper / Scourge**: Fel Lichen / Toxic Olive Green (`#4C9950` / `#6BB870`)
- **Confirmed Decision 2 (Match Provided UI Layout Exactly)**:
  - **Top Navigation Bar**: Direct tabs for `🎮 Thumb Wings`, `⚔️ Combat Bar`, `🦅 Mounts & QoL`, `⏱️ Boss HUD`, and `📋 Dailies`.
  - **Top Right Controls**: Sizing presets (`XS` | `MD` | `LG`), audio toggle, haptics toggle, reset/refresh, active build selector (`Dragonhunter [F1F2F3] Build →`), and handheld indicator (`📱 Thor & DroidDeck`).
  - **Live Boss Ticker**: Live event banner (e.g. `LIVE: Fire Elemental (10m left)`) with immediate `[Copy Waypoint]` clipboard action.
  - **Thumb Wings Layout**: Left thumb cluster (1-3 top, 4-5 middle, Swap `~` and Dodge `V` bottom), Center cluster (`TARGET Tab`, `SKYSCALE Vertical Climb Y`, `/gg`), and Right thumb cluster (`6` heal, `7-8` utilities, `9` utility, `0` elite, `SPECIAL Action N`, `ASSIST Call T`).
- **Confirmed Decision 3 (Direct Touch Controls - Zero Host Daemon)**:
  - Built as a pure, native Android app that eliminates PWA browser lag, gesture conflicts, and touch latency. Button presses instantly trigger native visual states, native `VibrationEffect` mechanical feedback, and dispatch native Android input events directly for DroidDeck.
- **Confirmed Decision 4 (Setup Wizard & Character API Gate)**:
  - First-launch Setup Wizard to configure haptic intensity, button sizing, and optional GW2 API Key.
  - Preloaded with the clean Guardian/Dragonhunter & Core profession templates until the user adds their API key to inspect their actual account characters, wallet, and gear.

---

## 1. Overview & Core Concept

- **What It Does**: Runs full-screen in landscape on the bottom display of the AYN Thor. It provides instantaneous touch input with mechanical haptics, dedicated GW2 profession mechanics (F1–F5), weapon skills, utilities, mount triggers, target lock, and live event timers.
- **Why Native Android Over PWA**: The previous web PWA suffered from touch lag, browser bar distractions, missing vibration APIs, and accidental browser gestures. Native Jetpack Compose provides sub-millisecond touch response, zero chrome, hardware-accelerated rendering, and native `VibrationEffect` vibration motors.

---

## 2. User Experience & Dynamic Theming (Matching Screenshot)

```
┌────────────────────────────────────────────────────────────────────────────────────────┐
│ [🎮 Thumb Wings] [⚔️ Combat Bar] [🦅 Mounts & QoL] [⏱️ Boss HUD] [📋 Dailies]  XS MD LG 🔊 📳 🔄 [⚔️ Build] [📱 Thor] │
├────────────────────────────────────────────────────────────────────────────────────────┤
│ 🔴 LIVE: Fire Elemental (10m left)                                      [Copy Waypoint]│
├────────────────────────────────────────────────────────────────────────────────────────┤
│ • Dragonhunter (Guardian) Mechanics                                     Change Build → │
│                         [ F1 Spear ]   [ F2 Wings ]   [ F3 Shield ]                    │
│                                                                                        │
│ ‹ LEFT THUMB                                                              RIGHT THUMB ›│
│ ┌──────────────┐┌──────────────┐┌──────────────┐ ┌──────────────┐ ┌──────────────┐┌──────────────┐┌──────────────┐ │
│ │ 1 Auto Attack││  2 Weapon 2  ││  3 Weapon 3  │ │ TARGET (Tab) │ │ Litany Wrath ││Proc of Blades││Test of Faith │ │
│ │      1       ││      2       ││      3       │ └──────────────┘ │     [6]      ││     [7]      ││     [8]      │ │
│ ├──────────────┴┼──────────────┴┼──────────────┤ ┌──────────────┐ ├──────────────┴┴──────────────┼──────────────┤ │
│ │  4 Weapon 4   │  5 Weapon 5   │              │ │   SKYSCALE   │ │  Fragments of Faith [9]      │ Dragon's Maw │ │
│ │      4        │      5        │              │ │   Climb Y    │ │                              │     [0]      │ │
│ ├───────────────┼───────────────┤              │ ├──────────────┤ ├──────────────────────────────┼──────────────┤ │
│ │     SWAP      │     DODGE     │              │ │     /gg      │ │        SPECIAL (N)           │  ASSIST (T)  │ │
│ │    Swap (~)   │   Evade (V)   │              │ └──────────────┘ └──────────────────────────────┴──────────────┘ │
│ └───────────────┴───────────────┘                                                                              │
└────────────────────────────────────────────────────────────────────────────────────────┘
```

### Layout Modes:
1. **🎮 Thumb Wings (Default)**:
   - Split ergonomic layout: Left thumb handles weapon skills 1–5, Weapon Swap (`~`), and Dodge (`V`).
   - Center column handles target acquisition (`Tab`), mount action (`Skyscale Y`), and quick utility (`/gg`).
   - Right thumb handles Healing (`6`), Utilities (`7`, `8`, `9`), Elite (`0`), Special Action key (`N`), and Call Target/Assist (`T`).
   - Top center hosts Profession Mechanics (F1–F5) dynamically styled to the active class.
2. **⚔️ Combat Bar**:
   - Classic unified horizontal MMO action bar for players who prefer traditional skill layout across the bottom with expanded utility racks.
3. **🦅 Mounts & QoL**:
   - Quick-select grid for all Guild Wars 2 mounts (Raptor, Springer, Skimmer, Jackal, Griffon, Roller Beetle, Warclaw, Skyscale, Siege Turtle) plus fishing, jade bot waypoint, and novelty hotkeys.
4. **⏱️ Boss HUD**:
   - Comprehensive chronological timeline of upcoming Tyrian world bosses and expansion meta events calculated completely offline on-device with waypoint chat code copying.
5. **📋 Dailies & Account**:
   - Guild Wars 2 account wallet, daily achievements, and character equipment inspector unlocked once the API key is provided.

---

## 3. Technical Architecture

- **Dynamic Theme Provider**: `ProfessionTheme` data class mapping each profession to primary accent, secondary surface, border glow, and mechanic badge colors. The entire Compose tree responds reactively when changing builds.
- **State Management**: Jetpack Compose `ViewModel` with `MutableStateFlow` managing active deck mode, size mode (`XS`, `MD`, `LG`), haptic enable/disable, active build preset, and event countdowns.
- **Haptics Engine**: Android `Vibrator` executing `VibrationEffect` on every button down-press, giving tactile feedback that feels like physical mechanical switches.
- **Key Injection**: Direct native Android input event dispatching coupled with a lightweight local trigger mechanism that communicates directly with DroidDeck running on the Thor.
- **Standalone Tyrian Time Calculator**: Mathematical UTC epoch modulo engine calculating Tyria's 2-hour day/night cycle and world boss 15-minute slot rotations without needing any external server.
- **GW2 API Client**: OkHttp + Moshi client communicating directly with `https://api.guildwars2.com/v2` with bearer token encryption in local storage.

---

## 4. Implementation Steps

1. **Project Configuration & Identity**:
   - Set app name to "Tyria Hub" in `strings.xml` and `metadata.json`.
   - Add permissions in `AndroidManifest.xml` (`VIBRATE`, `INTERNET`, `ACCESS_NETWORK_STATE`).
2. **Profession Color System & Themes**:
   - Implement `Gw2ProfessionTheme` covering Guardian (Blue), Ranger (Green), Warrior (Amber), Revenant (Carmine Red), Thief (Maroon), Engineer (Bronze), Elementalist (Flame Scarlet), Mesmer (Purple), and Necromancer (Fel Green).
3. **Haptic & Sound Engine (`HapticsManager`)**:
   - Configure crisp, low-latency haptic profiles for regular skills, weapon swap, dodge roll, and elite triggers.
4. **Onboarding Setup Wizard**:
   - Compact setup screen on initial launch to select button sizing, test haptic feedback, and optionally input the GW2 API Key.
5. **Core Touch Deck UI (The Exact Reference Layout)**:
   - Build the top bar: Mode tabs, Size toggle (`XS`, `MD`, `LG`), Audio/Haptics toggles, Reset, Build Badge, and Thor Status.
   - Build the live Boss Ticker with countdown and clipboard copy action.
   - Build the Thumb Wings split layout with left weapon skills, center target/mount/gg, right healing/utility/elite/special, and top profession mechanics (F1–F5).
6. **All 9 Professions & Elite Specializations**:
   - Build selector with templates for Guardian/Dragonhunter, Ranger/Galeshot, Elementalist, Mesmer, Necromancer, Thief, Engineer, Warrior, and Revenant with distinctive theme colors and F1–F5 mechanics.
7. **Mounts & QoL Panel**:
   - Dedicated touch deck for quick mount summoning and exploration tools.
8. **Standalone World Boss Timers HUD**:
   - Offline UTC rotation tracker with search, category filtering, and instant waypoint clipboard copying.
9. **GW2 API Integration & Account Dailies**:
   - Secure API token entry, account wallet inspector, character gear viewer, and daily achievement tracker.
10. **Verification**:
   - Run `compile_applet` to verify clean build and flawless execution.
