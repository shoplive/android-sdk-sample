# Shoplive Android SDK

Distribution repository for the Shoplive Android SDK. **No SDK source lives here.**

> Canonical repo: [`shoplive/shoplive-sdk-android`](https://github.com/shoplive/shoplive-sdk-android)  
> (`android-sdk-sample` redirects here.)

Binaries (AAR + POM) are published to:

| Channel | Purpose |
| --- | --- |
| **GitHub Releases** | Downloadable AAR/POM assets per version tag |
| **`maven-repo` branch** | Maven repository layout for Gradle `implementation(...)` resolve |

Consumers never need the internal modules (`shoplive-core`, `shoplive-webrtc`, …).  
Declare **only** the product(s) you use — transitive dependencies come from each artifact’s POM (same behaviour as Maven Central).

## Requirements

| | |
| --- | --- |
| Min SDK | **23+** (see release notes if a release raises the floor) |
| Distribution | Gradle Maven repository (this GitHub `maven-repo` branch) |
| Android Gradle Plugin | 8.x recommended |

## Installation

### 1. Repository

`settings.gradle.kts` (or root `repositories` block):

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

#### Player only

```kotlin
dependencies {
    implementation("cloud.shoplive:shoplive-player-sdk:3.0.0")
}
```

Version catalog (`gradle/libs.versions.toml`):

```toml
[versions]
shoplive = "3.0.0"

[libraries]
shoplive-player-sdk = { module = "cloud.shoplive:shoplive-player-sdk", version.ref = "shoplive" }
```

```kotlin
implementation(libs.shoplive.player.sdk)
```

That single line pulls in the transitive Shoplive modules declared in the published POM
(`shoplive-core`, `shoplive-core-player`, `shoplive-exoplayer`, `shoplive-webrtc`, …).

#### Streamer only

```kotlin
dependencies {
    implementation("cloud.shoplive:shoplive-streamer-sdk:3.0.0")
}
```

```toml
shoplive-streamer-sdk = { module = "cloud.shoplive:shoplive-streamer-sdk", version.ref = "shoplive" }
```

```kotlin
implementation(libs.shoplive.streamer.sdk)
```

#### Player + Streamer together

```kotlin
dependencies {
    implementation("cloud.shoplive:shoplive-player-sdk:3.0.0")
    implementation("cloud.shoplive:shoplive-streamer-sdk:3.0.0")
}
```

```kotlin
implementation(libs.shoplive.player.sdk)
implementation(libs.shoplive.streamer.sdk)
```

Shared modules (`shoplive-core`, `shoplive-webrtc`, …) resolve once; Gradle deduplicates identical coordinates via the POMs.

> Do **not** add `shoplive-core` / `shoplive-webrtc` / `shoplive-exoplayer` yourself unless Shoplive support asks you to. They are implementation details of the product SDKs.

## Products

| Product coordinate | Purpose | Pulled transitively (examples) |
| --- | --- | --- |
| `cloud.shoplive:shoplive-player-sdk` | Live / VOD playback | `shoplive-core`, `shoplive-core-player`, `shoplive-exoplayer`, `shoplive-webrtc` → `shoplive-android-webrtc`, … |
| `cloud.shoplive:shoplive-streamer-sdk` | Broadcasting | `shoplive-core`, `shoplive-webrtc` → `shoplive-android-webrtc`, `shoplive-rtmp`, … |

On international **3.x**, former `common` / `lokalise` / `network` / `permission` surfaces ship inside `shoplive-core`. You only depend on the product SDK rows above.

## Releases

- See [Releases](https://github.com/shoplive/shoplive-sdk-android/releases) for tagged versions and attached AAR/POM files.
- The `maven-repo` branch holds the same binaries in Maven path layout for Gradle resolve.

## Note on “Source code” zip / tar.gz

GitHub always attaches auto-generated source archives to a Release. Those archives are **this distribution repo** (README / docs), not the private SDK sources.

## Cutting a release (maintainers)

Artifacts are built in the private SDK source repository (`matrix-sdk-android`, international line) and published here.

```bash
# from matrix-sdk-android
make githubReleaseInternational
# or: VERSION=3.0.0 ./scripts/deploy-github-release-international.sh
```

## Ownership

- Team: Shoplive Mobile
- Contact: [ask@shoplive.cloud](mailto:ask@shoplive.cloud)

