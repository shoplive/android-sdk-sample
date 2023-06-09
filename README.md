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

단계별로 따라해 보세요. <br />

## Step by step

### Step 1: Shoplive 어드민 계정 생성하기

Shoplive Android SDK를 사용하려면 Shoplive 담당자에게 관리자 계정과 비밀번호를 요청하세요. [Shoplive 어드민 계정 발급받기 가이드](https://docs.shoplive.kr/docs/admin-account)

> **Note**: Shoplive 담당자에게 관리자 계정 발급을 요청하시기 바랍니다. 등록하실 이메일과 성함을 전달해 주시면 계정을 발급해 드립니다. 관리자 계정 생성이 완료되면 해당 이메일로 계정 생성 안내 메일이 전송됩니다. 절차에 따라 계정 등록을 완료해 주시기 바랍니다.

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

### Step 4: Handler 등록하기

Shoplive Android SDK 로부터 여러 이벤트를 수신할 Handler를 등록하세요.
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

### Step 5: Shoplive Player 실행하기

AccessKey와 Campaign키를 사용하여 방송을 재생할 수 있습니다.

#### A. 방송 재생하기
```
ShopLive.setAccessKey("{accessKey}")

ShopLive.play("{campaignKey}") 
```

#### B. 방송 미리보기
```
ShopLive.showPreviewPopup(ShopLivePreviewData(this@YourActivity, "{accessKey}", "{campaignKey}"))
```