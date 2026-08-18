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

### 1. Set up the Maven repository

`settings.gradle.kts` (or the root `repositories` block):

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://sdk.shoplive.cloud/maven-repo")
        }
    }
}
```

Use the `sdk.shoplive.cloud` endpoint as the canonical Maven repository URL.

### 2. Dependencies

Declare the product coordinates you need, all on the same version:

```kotlin
dependencies {
    implementation("cloud.shoplive:shoplive-player-sdk:3.0.0")
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
```

Everything else — `shoplive-core`, `shoplive-core-player`, `shoplive-exoplayer`, `shoplive-webrtc`,
`shoplive-android-webrtc`, `shoplive-rtmp` — is an implementation detail that arrives transitively
through each product's POM. You never declare those yourself.

### 3. Products

| Product coordinate | Purpose |
| --- | --- |
| `cloud.shoplive:shoplive-player-sdk` | Live / VOD playback |
| `cloud.shoplive:shoplive-streamer-sdk` | Broadcasting |

**Player**

```kotlin
implementation("cloud.shoplive:shoplive-player-sdk:3.0.0")
```

**Streamer**

```kotlin
implementation("cloud.shoplive:shoplive-streamer-sdk:3.0.0")
```

**All**

```kotlin
implementation("cloud.shoplive:shoplive-player-sdk:3.0.0")
implementation("cloud.shoplive:shoplive-streamer-sdk:3.0.0")
```

Using both products still resolves each shared module exactly once — Gradle deduplicates the
identical coordinates coming from both POMs.

With any of the three, the core surface is available without declaring it:

```kotlin
import cloud.shoplive.core.publicsurface.Shoplive
import cloud.shoplive.core.publicsurface.ShopliveConfiguration
import cloud.shoplive.core.publicsurface.ShopliveUser

Shoplive.initialize(context, ShopliveConfiguration(accessKey = "{ACCESS_KEY}"))
Shoplive.setUser(ShopliveUser.Guest)
```

## Releases

- [Releases](https://github.com/shoplive/shoplive-sdk-android/releases) — tagged versions with the
  AAR/POM assets attached, plus the release notes for each version.
- The `maven-repo` branch holds the same binaries in Maven path layout for Gradle to resolve.
- The auto-generated "Source code (zip / tar.gz)" archives on a release are this distribution repo,
  not the SDK sources — use the AAR/POM assets or the Maven repository above.

## Ownership & Support

- Team: Shoplive Mobile
- Contact: [ask@shoplive.cloud](mailto:ask@shoplive.cloud)
