# Shoplive Android SDK guide

- [Shoplive Android SDK guide](#shoplive-android-sdk-guide)
- [Shoplive Android SDK](#1-shoplive-android-sdk)
  - [Introduction](#introduction)
  - [Feature](#feature)
  - [Requirements](#requirements)
  - [Changes](#changes)
- [Before starting](#2-before-starting)
- [Getting started](#3-getting-started)
  - [`Step 1` Installing](#step-1-installing)
  - [`Step 2` Running Shoplive Player](#step-2-running-shoplive-player)
  - [`Step 3` Using Shoplive chat](#step-3-using-shoplive-chat)
  - [`Step 4` Using a coupon](#step-4-using-a-coupon)
  - [`Step 5` Managing products](#step-5-managing-products)
  - [`Step 6` Sharing a campaign link](#step-6-sharing-a-campaign-link)
  - [`Step 7` Switching to PIP(Picture-in-Picture) mode](#step-7-switching-to-pippicture-in-picture-mode)
  - [`Step 8` Setting the progress bar](#step-8-setting-the-progress-bar)
  - [`Step 9` Setting interrupt options while playing Shoplive Player](#step-9-setting-interrupt-options-while-playing-shoplive-player)
  - [`Step 10` Setting the aspect ratio in tablet portrait mode](#step-10-setting-the-aspect-ratio-in-tablet-portrait-mode)
  - [`Step 11` Setting Shoplive Player's next action when a user selects a link](#step-11-setting-shoplive-players-next-action-when-a-user-selects-a-link)

- [Reference - API Documentation](api-document.md)

<br>
<br>

# 1\. Shoplive Android SDK

<br>

## Introduction

Shoplive Android SDK is a mobile SDK that allows users to quickly and easily provide live campaigns to customers using the app by inserting simple `Java` or `Kotlin` code. Enable a seamless mobile experience on smartphones, including Shoplive PIP(Picture-in-Picture) mode and native keyboard UI.

<image src="../images/guide.gif" width="20%" height="20%"></image>

<br>

## Feature

+ Shoplive campaign available using Shoplive Player
+ Possible to induce users to watch Shoplive campaigns by displaying Shoplive campaigns set to low-quality/muted in PIP mode.
+ Chatting and product search available as users watch Shoplive campaigns
+ Chatting available in unauthenticated user mode, and real-time user information who participated in the chat delivery is possible
+ Chatting available in authenticated user mode, and user account integration possible with simple authentication/security authentication
+ Changeable the Chat nickname font and Chat font
+ coupons are available
+ Product management available using multiple functions
+ Native system shared UI or Custom system shared UI are available
+ Shareable the Link to the campaign that is playing
+ Configurable the interrupt option while playing Shoplive Player
+ Configurable the aspect ratio in tablet portrait mode
+ Real-time reception possible the various information about the campaign being played
+ Real-time reception possible  the various information about the Shoplive Android SDK

<br>

## Requirements

These are the minimum requirements to use the Shoplive Android SDK. If you do not meet these requirements, you cannot use the Shoplive Android SDK.

+ `Android 4.4` and above
+ `targetSdkVersion` 30 and above

<br>

## Changes

+ v1.2.2
  * We improved to link quiz functions such as vibration and sound effects.
  * We improved to play silently when previewing.
  * Added API that is selectable the next action instead of forcibly switching to PIP in the app when the user selects a product or notices.
  * Added API to set endpoint.
+ \[[Previous version update](change-log.md)]

<br>

# 2\. Before starting

+ To use the Shoplive Android SDK, ask the Shoplive representative for an admin account and password.
  + \[[Make a request](https://www.shoplive.cloud/en)]
+ Add campaigns in Shoplive admin and write down Campaign Key.
  + \[[Shoplive admin account creation guide](https://en.shoplive.guide/docs)]

<br>

# 3\. Getting started

Install the Shoplive Android SDK and simply apply it to Shoplive Player.

<br>

## `Step 1` Installing

1. Add the code below to `project/build.gradle`.
   
   ```gradle
   allprojects {
       repositories
           ...
           maven { url 'https://shoplivesdk.jfrog.io/artifactory/shoplive-sdk/' }
           ...
       }
   }
   ```

2. Add the code below to `app/build.gradle`.
   
   ```gradle
   android {
      defaultConfig {
          ...
          multiDexEnabled true
          ...
      }
   }
   
   dependencies {
       implementation 'cloud.shoplive:shoplive-sdk:1.2.2'
   }
   ```

3. Register the application below to `AndroidManifest.xml`.
   
   ```xml
   <application
           android:name=".YourApplication"
          .
          .>
   </application>
   ```

4. Add the code below to `YourApplication.kt`.
   
   ```kotlin
   class YourApplication: Application() {
   
       override fun onCreate() {
           super.onCreate()
   
           ShopLive.init(this)
       }
   }
   ```

+ API Reference
  > [ShopLive.init](./api-document.md#shoplive.init)

  <br>

   ### Registering Handler

   > Register a handler to receive multiple events from the Shoplive Android SDK.

  ```kotlin
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

<br>

## `Step 2` Running Shoplive Player

+ Initialize the Shoplive Android SDK using the Access Key received from the Shoplive representative.
  ```kotlin
  ShopLive.setAccessKey("{accessKey}")
  ```
  
  * API Reference
    > [ShopLive.setAccessKey](./api-document.md#ShopLive.setAccessKey)

<br>

### Play the campaign

+ Play the campaign using the Campaign Key.
  ```kotlin
  ShopLive.play("{campaignKey}")
  ```
  
  * API Reference
    > [ShopLive.play](./api-document.md#ShopLive.play)

+ If it runs successfully, you will see the view below.
  
  <image src="../images/run.png" width="20%" height="20%"></image>

<br>

### Campaign preview

> Add a preview within the app even if it is not in the campaign.  
`android.permission.SYSTEM_ALERT_WINDOW` permission is required to use this feature.

<image src="../images/preview.png" width="20%" height="20%"/> 

```kotlin
ShopLive.showPreview({campaignKey})
```

+ Select the preview to enter the campaign. To handle other handling without entering the main campaign, `override` the `handlePreview` function and implement it directly.
  
  ```kotlin
  override fun handlePreview(context: Context, campaignKey: String) {
      Log.d(TAG, "campaignKey=$campaignKey")
  }
  ```

+ API Reference
  
  > [ShopLive.showPreview](./api-document.md#ShopLive.showPreview)   
[ShopLive.hidePreview](./api-document.md#ShopLive.hidePreview)     
[ShopLive.resumePreview](./api-document.md#ShopLive.resumePreview)  

<br>

## `Step 3` Using Shoplive chat

<br>

<image src="../images/chat.png" width="20%" height="20%"></image>

+ Getting ready to use the chat.
  * Set access to chat in Shoplive admin.
   <br>
   
   [[Shoplive Admin Guide - Preparing Campaign - Chat Settings](https://en.shoplive.guide/docs/preps-for-campaign#chat-settings)]
    
    <image src="../images/admin_campaign_chat_settings.png" width="50%" height="50%"></image>

<br>

+ Chat as an unauthenticated user
  * Connect to chat without any authentication.
  * If it does not allow unauthenticated users to chat, even if select the chat button, no response occurs.
  * If unauthenticated user nicknames are not set to be mandatory, unauthenticated user nicknames are randomly assigned.  
  
    <image src="../images/guest_auto_nickname.png" width="20%" height="20%"></image>
  * If you set Unauthenticated User Nickname to Required, the user must enter a nickname. 
  
    <image src="../images/chat_nickname.png" width="20%" height="20%"></image>

<br>

+ Chat as an authenticated user
  
  * #### Integrate with simple authentication
    
    - Integrate user accounts with the ability to set user IDs and login user nicknames.
    - Set user ID, name, age, gender, user score, and other custom data information.
      ```kotlin
      val user = ShopLiveUser().apply {
          userId = "{USER_ID}"
          userName = "{USER_NAME}"
          gender = ShopLiveUserGender.Male
          age = 25
          userScore = 0
      }
      ShopLive.setUser(user)
      ```
    - API Reference
      > [ShopLive.setUser](./api-document.md#ShopLive.setUser)
      <br>
  
  * #### Integrate with secure authentication
    
    - Set information such as user ID and name in the Shoplive Android SDK using the security authentication token (JWT) granted with the secret key creatd by the Shoplive admin.
    
      <br>
    <B>\[Ask the Shoplive representative to create a secure authentication token (JWT)]</B>
    
    - It must create a secure authentication token (JWT) on the client server and provide it to the App client via the Shoplive Android SDK API.
    - Authenticates the user using the Secret Key created by the Shoplive admin.
    
       \[[Auth Token Creation Guide](https://en.shoplive.guide/docs/use-jwt-auth)]
    
       ```kotlin
          ShopLive.setAuthToken("{jwt}")
       ```
    
      <br>

    - API Reference
      > [ShopLive.setAuthToken](./api-document.md#shopLive.setauthtoken)
    
      <br>

+ Changing nickname
  
  * When a user changes their nickname in Shoplive Player, the Shoplive Android SDK delivers the change to the client using the  `onSetUserName` function.
    
    ```kotlin
    override fun onSetUserName(jsonObject: JSONObject) {
        super.onSetUserName(jsonObject)
        Log.d(TAG, "userId=${jsonObject.get("userId")}, userName=${jsonObject.get("userName")}")
    }
    ```

<br>

+ While streaming live, It is possible to manage chats from the Shoplive admin.<br>
  + [[Shoplive Admin Guide - Using the Control Console - Chat Management](https://en.shoplive.guide/docs/manage-chat)]

<br>

### Changing the chat font

+ Change the default font of the Shoplive Player app.

+ Default: System font
  
  <image src="../images/default-font.png" width="50%" height="50%"></image>

<br>

+ Change font
  * res/font/font.xml
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <font-family xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto">
    
        <!-- NanumGothic -->
        <font
            android:font="@font/nanumgothic"
            android:fontStyle="normal"
            android:fontWeight="400"
    
            app:font="@font/nanumgothic"
            app:fontStyle="normal"
            app:fontWeight="400" />
    </font-family>
    ```
    
    ```kotlin
    val nanumGothic = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            resources.getFont(R.font.nanumgothic)
        } else {
            ResourcesCompat.getFont(requireContext(), R.font.nanumgothic)
        }
    
    ShopLive.setChatViewTypeface(nanumGothic)
    ```
    
    <image src="../images/custom-font.png" width="50%" height="50%"></image>

<br>

+ API Reference
  > [ShopLive.setChatViewTypeface](./api-document.md#ShopLive.setChatViewTypeface)

<br>

## `Step 4` Using a coupon

+ General coupons and custom coupons (pop-up) are available.
  
  <image src="../images/coupon.png" width="20%" height="20%"></image>

* General coupon
  
  + Users can select the general coupon registered by the administrator on the campaign view and apply for the benefits set in the coupon (eg, discount, etc.). Coupon information is delivered using a `handleDownloadCoupon` function.
    ```kotlin
    override fun handleDownloadCoupon(
            context: Context,
            couponId: String,
            callback: ShopLiveHandlerCallback
    ) {
        callback.couponResult(
            isDownloadSuccess = true, 
            message = "Coupon download success!", 
            couponStatus = ShopLive.CouponPopupStatus.HIDE, 
            alertType = ShopLive.CouponPopupResultAlertType.ALERT
        )
    }
    ```
  + Issue coupons to users, and use `callback` to deliver coupon issuance results.

<br>

* Custom Coupon (Pop-up)
  
  + Users can select the custom coupon (pop-up) registered by the administrator on the campaign view and apply for the benefits set in the coupon ((eg, discount, etc.). Custom coupon information is delivered using a `handleCustomAction` function.
    
    ```kotlin
     override fun handleCustomAction(
            context: Context, 
            id: String, 
            type: String, 
            payload: String,
            callback: ShopLiveHandlerCallback) {
    
        callback.customActionResult(
            isSuccess = true, 
            message = "success!", 
            couponStatus = ShopLive.CouponPopupStatus.HIDE, 
            alertType = ShopLive.CouponPopupResultAlertType.ALERT
        )
    }
    ```
  
  + Issue coupons to users, and use `callback` to deliver coupon issuance results.

<br>

## `Step 5` Managing products

> The event that occurs when a product is selected or a shopping cart is selected on the Shoplive Player view and the selected information are sent to the client by the Shoplive Android SDK.

<image src="../images/product.png" width="20%" height="20%"></image>

* CLICK\_PRODUCT\_DETAIL
  
  + When the user selects a product from the product list on the Shoplive Player view, the Shoplive Android SDK uses the `handleReceivedCommand` function to deliver the selected product information to the client.
    
    ```kotlin
    override fun onReceivedCommand(
        context: Context, 
        command: String, 
        data: JSONObject) {
    
            when(command) {
                "CLICK_PRODUCT_DETAIL" -> {
    
                }
            }
    }
    ```

* CLICK\_PRODUCT\_CART
  
  + When the user selects a shopping cart button from the product list on the Shoplive Player view, the Shoplive Android SDK uses  the `handleReceivedCommand` function to deliver the shopping cart information to the client.
    
    ```kotlin
    override fun onReceivedCommand(
        context: Context, 
        command: String, 
        data: JSONObject) {
    
            when(command) {
                "CLICK_PRODUCT_CART" -> {
    
                }
            }
    }
    ```

* handleNavigation
  
  + When a user selects a product, banner, etc. on the campaign view being played, Shoplive Android SDK uses the  `handleNavigation` function to deliver the URL information of the selected item to the client.
    
    ```kotlin
    override fun handleNavigation(context: Context, url: String) {
        Log.d(TAG, "url=$url")
    }
    ```

<br>

## `Step 6` Sharing a campaign link

+ Users can easily share the `scheme` or `URL` of a campaign through Shoplive's Sharing API.

+ scheme setting
  
  ```xml
  <activity 
          android:name=".SchemeActivity"
          android:theme="@style/Transparent"
          android:launchMode="singleTask"
          android:screenOrientation="portrait">
      <intent-filter>
          <action android:name="android.intent.action.VIEW" />
          <category android:name="android.intent.category.DEFAULT" />
          <category android:name="android.intent.category.BROWSABLE" />
          <data android:scheme="your_scheme" android:host="your_host" />
      </intent-filter>
  </activity>
  ```
  
  ```kotlin
  val scheme = "{your_scheme}://{your_host}?ak=xxx&ck=yyy"
  ShopLive.setShareScheme(scheme)
  ```

+ URL setting
  
  ```kotlin
  val url = "https://www.share_link.com/?ak=xxx&ck=yyy"
  ShopLive.setShareScheme(url)
  ```

+ By sharing the campaign link being played on social media or other platforms users can induce potential users to install the app or watch the campaign just by clicking the shared link. (However, users have to handle deeplink separately in the app.)
  
  <image src="../images/share.png" width="20%" height="20%"></image> <image src="../images/share.gif" width="24%" height="24%"></image>

+ Using the custom system sharing UI
  
  ```kotlin
  override fun handleShare(context: Context, shareUrl: String) {
      val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
      val view = inflater.inflate(R.layout.custom_share_dialog, null)
  
      val builder = AlertDialog.Builder(context)
      builder.setView(view)
  
      val dialog = builder.create()
      dialog.show()
  }
  ```
  
  <image src="../images/custom_share.png" width="20%" height="20%"></image>

+ API Reference
  
  > [ShopLive.setShareScheme](./api-document.md#ShopLive.setShareScheme)

<br>

## `Step 7` Switching to PIP(Picture-in-Picture) mode

+ Switches to PIP(picture-in-picture) mode when performing other operations while watching a campaign.
  
  <image src="../images/pip.gif" width="20%" height="20%"></image>
  
  ```kotlin
  // Switch to PIP mode
  ShopLive.startPictureInPicture()
  
  // Switch to full view mode
  ShopLive.stopPictureInPicture()
  ```

<br>

+ Setting the aspect ratio for PIP mode
  
  > If the aspect ratio of PIP mode is not set, it will be applied in 9:16 ratio (default).
  
  | 1:1| 1:2| 2:3| 3:4| 9:16 <br> (Default)
  |----------|----------|----------|----------|----------
  | <image src="../images/pip_1_1.png"/>| <image src="../images/pip_1_2.png" />| <image src="../images/pip_2_3.png" />| <image src="../images/pip_3_4.png" />| <image src="../images/pip_9_16.png" />

  ```kotlin
  /*
  ShopLivePIPRatio.RATIO_1X1
  ShopLivePIPRatio.RATIO_1X2
  ShopLivePIPRatio.RATIO_2X3
  ShopLivePIPRatio.RATIO_3X4
  ShopLivePIPRatio.RATIO_9X16
  */
  ShopLive.setPIPRatio(ShopLivePIPRatio.RATIO_3X4)
  ```

<br>

+ API Reference
  > [ShopLive.startPictureInPicture](./api-document.md#ShopLive.startPictureInPicture) <br> 
    [ShopLive.stopPictureInPicture](./api-document.md#ShopLive.stopPictureInPicture) <br> 
    [ShopLive.setPIPRatio](./api-document.md#ShopLive.setPIPRatio)

<br>

## `Step 8` Setting the progress bar

+ Change the style of the progress bar displayed while entering the campaign and loading the video.

+ Change progress bar color (Default: white)
  
  ```kotlin
  ShopLive.setLoadingProgressColor("#FF0000")
  ```
  
  <image src="../images/progress.gif" width="20%" height="20%"></image> <image src="../images/progress_color.png" width="50%" height="50%"></image>

<br>

+ Setting the image animation progress bar
  
  * res/drawable/progress\_animation.xml
    
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <animation-list xmlns:android="http://schemas.android.com/apk/res/android"
        android:oneshot="false">
    
        <item android:drawable="@drawable/loading1" android:duration="100" />
        <item android:drawable="@drawable/loading2" android:duration="100" />
        <item android:drawable="@drawable/loading3" android:duration="100" />
        <item android:drawable="@drawable/loading4" android:duration="100" />
        <item android:drawable="@drawable/loading5" android:duration="100" />
        <item android:drawable="@drawable/loading6" android:duration="100" />
        <item android:drawable="@drawable/loading7" android:duration="100" />
        <item android:drawable="@drawable/loading8" android:duration="100" />
    </animation-list>
    ```
    
    ```kotlin
    ShopLive.setLoadingAnimation(R.drawable.progress_animation)
    ```
    
    <image src="../images/progress_image_animation.gif" width="20%" height="20%"></image>
  
    <br>

+ API Reference
  
  > [ShopLive.setLoadingProgressColor](./api-document.md#ShopLive.setLoadingProgressColor) <br> 
  [ShopLive.setLoadingAnimation](./api-document.md#ShopLive.setLoadingAnimation)

<br>

## `Step 9` Setting interrupt options while playing Shoplive Player

> When an interrupt event occurs due to a call or earphone connection loss during playing, the user can set the Shoplive Player playing action after the interrupt event ended.

<br>

+ #### Interrupt due to earphone (or headset) disconnection
  
  > When the earphone (or headset) is disconnected, set to play the campaign automatically when reconnected.   
    (If this is not set, the video paused when the earphone (or headset) is disconnected.)
  
  ```kotlin
  // play the campaign automatically
  ShopLive.setKeepPlayVideoOnHeadphoneUnplugged(true)
  
  // play the campaign automatically + mute
  ShopLive.setKeepPlayVideoOnHeadphoneUnplugged(true, true) 
  ```

<br>

+ #### Interrupt due to call connection
  
  > Set to pause the campaign when returning to the campaign after the call ends.   
`android.permission.READ_PHONE_STATE` permission is required to use this feature.   
(If this is not set, the video will be played automatically when you return to the video after the call ends.)
  
  ```kotlin
  // Pause campaign when connecting a call
  ShopLive.setAutoResumeVideoOnCallEnded(false)
  ```

<br>

+ API Reference
  > [ShopLive.setKeepPlayVideoOnHeadphoneUnplugged](./api-document.md#ShopLive.setKeepPlayVideoOnHeadphoneUnplugged) <br> [ShopLive.setAutoResumeVideoOnCallEnded](./api-document.md#ShopLive.setAutoResumeVideoOnCallEnded)

<br>

## `Step 10` Setting the aspect ratio in tablet portrait mode

> Maintains the aspect ratio of Shoplive view in portrait mode on tablet.   
(If this is not set, the tablet will be set to fill the view in portrait mode.)

| fill the view| keep aspect ratio
|----------|----------
| <image src="../images/tablet_aspect_false.jpg"/>| <image src="../images/tablet_aspect_true.jpg" />

```kotlin
// Play with aspect ratio maintained
ShopLive.setKeepAspectOnTabletPortrait(true)

```

<br>

+ API Reference
  > [ShopLive.setKeepAspectOnTabletPortrait](./api-document.md#ShopLive.setKeepAspectOnTabletPortrait)

<br>



## `Step 11` Setting Shoplive Player's next action when a user selects a link

> When a user selects a link, such as a product, announcement, or banner, the following actions of Shoplive Player can be set.

<br>

+ Switch to PIP

    <image src="../images/nextAction_pip.gif" width="20%"/>

    ```kotlin
    ShopLive.setNextActionOnHandleNavigation(ActionType.PIP)
    ```

+ Keep in status

    <image src="../images/nextAction_keep.gif" width="20%"/>

    ```kotlin
    ShopLive.setNextActionOnHandleNavigation(ActionType.KEEP)
    ```

+ Close

    <image src="../images/nextAction_close.gif" width="20%"/>

    ```kotlin
    ShopLive.setNextActionOnHandleNavigation(ActionType.CLOSE)
    ```
<br>

+ API Reference
    > [ShopLive.setNextActionOnHandleNavigation](./api-document.md#shoplivesetnextactiononhandlenavigation)

<br>
