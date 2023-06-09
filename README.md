# [Shoplive](https://www.shoplive.cloud/kr) Player SDK for Android

[![Platform](https://img.shields.io/badge/platform-android-orange.svg)](https://github.com/sendbird/sendbird-chat-sdk-android)
[![Languages](https://img.shields.io/badge/language-kotlin-orange.svg)](https://github.com/sendbird/sendbird-chat-sdk-android)

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
- `targetSdkVersion 30 or higher`

## Getting started

Please follow the step by step. <br />

## Step by step

### Step 1: Creating an admin account

To use the Shoplive SDK for Android, ask the Shoplive representative for an admin account and password. [Admin Guide - Creating Admin Account](https://en.shoplive.guide/docs/admin-account)

> **Note**: Please request for your admin account to a Shoplive representative. Your account will be issued once you provide your registered email and name. Once your account is issued, a temporary password will be sent to your email. Please reset the password on your first login.

<br />

### Step 2: Install the Shoplive SDK

Installing the Shoplive SDK is simple if you're familiar with using external libraries or SDKs. First, add the following code to your **root** `build.gradle` file:

```gradle
allprojects {
    repositories {
        ...
        url 'https://shoplivesdk.jfrog.io/artifactory/shoplive-sdk/'
    }
}
```
Then, add the dependency to the project's top-level `build.gradle` file:

```gradle
dependencies {
    ...
    def shoplive_sdk_version = "1.4.1"
    implementation "cloud.shoplive:shoplive-sdk-all:$shoplive_sdk_version"
    ...
}
```

### Step 3: Grant system permissions to the Shoplive SDK

The Shoplive SDK requires system permissions, add the following lines to your `AndroidManifest.xml` file.

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Step 4: Registering Handler

Register a handler to receive multiple events from the Shoplive SDK for Android. <br />

```
ShopLive.setHandler(object : ShopLiveHandler {
    override fun handleNavigation(context: Context, url: String) {
    }

    override fun handleDownloadCoupon(
        context: Context,
        couponId: String,
        callback: ShopLiveHandlerCallback) {

    }

    override fun onChangeCampaignStatus(
        context: Context, 
        campaignStatus: String) {
    }

    override fun onCampaignInfo(campaignInfo: JSONObject) {
    }

    override fun onError(context: Context, code: String, message: String) {
    }

    override fun handleShare(context: Context, shareUrl: String) {
    }

    override fun handleCustomAction(
        context: Context, 
        id: String, 
        type: String, 
        payload: String,
        callback: ShopLiveHandlerCallback) {
    }

    override fun onChangedPlayerStatus(
        isPipMode: Boolean, 
        state: String) {
        super.onChangedPlayerStatus(isPipMode, state)
    }

    override fun onSetUserName(jsonObject: JSONObject) {
        super.onSetUserName(jsonObject)
    }

    override fun onReceivedCommand(
        context: Context, 
        command: String, 
        data: JSONObject) {
    }
})
```

### Step 5: Running Shoplive Player

Play the video using the access key and campaign key.


#### A. Play the campaign

```
ShopLive.setAccessKey("{accessKey}")

ShopLive.play("{campaignKey}") 
```

#### B. Preview the campaign

```
ShopLive.showPreviewPopup(ShopLivePreviewData(this@YourActivity, "{accessKey}", "{campaignKey}"))
```