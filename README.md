# [Shoplive](https://www.shoplive.cloud/kr) Player SDK for Android

[![Platform](https://img.shields.io/badge/platform-android-orange.svg)](https://github.com/shoplive/android-sdk-sample)
[![Languages](https://img.shields.io/badge/language-kotlin-orange.svg)](https://github.com/shoplive/android-sdk-sample)

## Table of contents

1.  [Introduction](#introduction)
1.  [Requirements](#requirements)
1.  [Getting started](#getting-started)

## Introduction

Simply install the SHOPLIVE SDK to quickly and easily provide live broadcasts to customers using your application.

<image src="doc/images/guide.gif" width="200" height="410"></image>

### Documentation

If you have any comments, questions or feature requests, let us know in the [email](mailto:ask@shoplive.cloud).

[English](https://en.shoplive.guide/docs/shoplive-sdk-for-android)<br />
[한국어](https://docs.shoplive.kr/docs/shoplive-android-sdk)


## Requirements

The minimum requirements for the Player SDK for Android are:

- `Android 4.4 (API level 19) or higher`
- `targetSdkVersion 33 or higher`

## Getting started

Please follow the step by step. <br />

## Step by step

### Step 1: Creating an admin account

To use the Shoplive SDK for Android, ask the Shoplive representative for an admin account and password. [Admin Guide - Creating Admin Account](https://en.shoplive.guide/docs/admin-account)

> **Note**: Please request for your admin account to a Shoplive representative. Your account will be issued once you provide your registered email and name. Once your account is issued, a temporary password will be sent to your email. Please reset the password on your first login.

<br />

### Step 2: Install the Shoplive SDK

Installing the Shoplive SDK is simple if you're familiar with using external libraries or SDKs. 

Add the dependency to the project's top-level `build.gradle` file:

```gradle
dependencies {
    ...
    def shoplive_sdk_version = "1.5.4"
    def your_exoplayer_version = "2.19.1"
    def your_media3_version = "1.1.1"
    def shoplive_exoplayer_version = your_exoplayer_version + "." + "8"
    def shoplive_media3_version = your_media3_version + "." + "8"

    // For submodules
    implementation "cloud.shoplive:shoplive-common:$shoplive_sdk_version" // must required
    implementation "cloud.shoplive:shoplive-exoplayer:$shoplive_exoplayer_version" // must required
    // When using media3. Exoplayer will be deprecated soon.
    // https://developer.android.com/guide/topics/media/media3/getting-started/migration-guide
    // implementation "cloud.shoplive:shoplive-media3:$shoplive_media3_version"
    implementation "cloud.shoplive:shoplive-network:$shoplive_sdk_version" // must required
    implementation "cloud.shoplive:shoplive-sdk-core:$shoplive_sdk_version" // for live player
    implementation "cloud.shoplive:shoplive-short-form:$shoplive_sdk_version" // for short-form player

    // For library - combined packaging
//    implementation "cloud.shoplive:shoplive-sdk-all:$shoplive_sdk_version" // live + short-form
    ...
}
```

### Step 3: Grant system permissions to the Shoplive SDK

The Shoplive SDK requires system permissions, add the following lines to your `AndroidManifest.xml` file.

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Step 4: Registering AccessKey & Handler

Register a accessKey & handler to receive multiple events from the Shoplive SDK for Android. <br />

```
ShopLive.setAccessKey("{accessKey}")
ShopLive.setHandler(object : ShopLiveHandler() {
    override fun handleNavigation(context: Context, url: String) {
        // Do something
    }
})
```

### Step 5: Running Shoplive Player

Play the video using the access key and campaign key.


#### A. Play the campaign

```
ShopLive.play(context, ShopLivePlayerData("{campaignKey}")) 
```

#### B. Preview the campaign

```
ShopLive.showPreviewPopup(this@YourActivity, ShopLivePreviewData("{campaignKey}"))
```