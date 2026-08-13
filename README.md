# Shoplive Android SDK

The Shoplive Android SDK, distributed as **AAR + POM** through a Gradle Maven repository. Every
release tag carries the AAR/POM assets, and the `maven-repo` branch serves the same binaries in
Maven layout for Gradle to resolve.

## Requirements

| | |
| --- | --- |
| Min SDK | **23** (see release notes if a release raises the floor) |
| Distribution | Gradle Maven repository |
| Android Gradle Plugin | 8.x recommended |

## Installation

### 1. Repository

`settings.gradle.kts` (or the root `repositories` block):

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://raw.githubusercontent.com/shoplive/shoplive-sdk-android/maven-repo")
        }
    }
}
```

### 2. Dependencies

Declare only the products your app needs — Player, Streamer, or both:

```kotlin
dependencies {
    implementation("cloud.shoplive:shoplive-player-sdk:3.0.0")
    implementation("cloud.shoplive:shoplive-streamer-sdk:3.0.0")
}
```

Or through a version catalog (`gradle/libs.versions.toml`):

```toml
[versions]
shoplive = "3.0.0"

[libraries]
shoplive-player-sdk = { module = "cloud.shoplive:shoplive-player-sdk", version.ref = "shoplive" }
shoplive-streamer-sdk = { module = "cloud.shoplive:shoplive-streamer-sdk", version.ref = "shoplive" }
```

```kotlin
implementation(libs.shoplive.player.sdk)
implementation(libs.shoplive.streamer.sdk)
```

## Modules

| Product coordinate | Purpose | Pulled in transitively |
| --- | --- | --- |
| `cloud.shoplive:shoplive-player-sdk` | Live / VOD playback | `shoplive-core`, `shoplive-core-player`, `shoplive-exoplayer`, `shoplive-webrtc` → `shoplive-android-webrtc` |
| `cloud.shoplive:shoplive-streamer-sdk` | Broadcasting | `shoplive-core`, `shoplive-webrtc` → `shoplive-android-webrtc`, `shoplive-rtmp` |

**Two products, and you declare only those two.** Everything else in the table is an
implementation detail that arrives through each product's POM:

- `shoplive-core` — shared configuration, user/session state and networking; the one module both products depend on
- `shoplive-core-player` — playback layer behind the Player product
- `shoplive-exoplayer` — HLS / VOD playback on ExoPlayer 2.19.1
- `shoplive-webrtc` — WebRTC path used by both products, including camera capture
- `shoplive-android-webrtc` — the prebuilt WebRTC binary (~22MB, so it stays its own artifact)
- `shoplive-rtmp` — RTMP broadcasting behind the Streamer product

You never add these yourself. Using Player and Streamer together still resolves each shared module
exactly once — Gradle deduplicates the identical coordinates coming from both POMs.

```kotlin
import cloud.shoplive.core.publicsurface.Shoplive
import cloud.shoplive.core.publicsurface.ShopliveConfiguration
import cloud.shoplive.core.publicsurface.ShopliveUser

Shoplive.initialize(context, ShopliveConfiguration(accessKey = "{ACCESS_KEY}"))
Shoplive.setUser(ShopliveUser.Guest)
```

`Shoplive.*` is available with only the product coordinate declared — the core surface comes in
transitively through the product's POM.

## Support

Questions and issue reports: contact your Shoplive representative, or
[ask@shoplive.cloud](mailto:ask@shoplive.cloud).
