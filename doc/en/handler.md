# interface ShopLiveHandler

<br>

## ShopLiveHandler.handleNavigation
> Handles url set by the administrator on product or banner clicks in Shoplive

<br>

### Declaration
```java
/**
  * @param url - redirecting url
  */
void handleNavigation(@NonNull String url);
```

<br>
<br>
<br>
<br>

## ShopLiveHandler.handleDownloadCoupon
> Handler on coupon click in ShopLive\
> \
> @Deprecated\
> ~~default void handleDownloadCoupon(@NonNull String couponId, @NonNull > ShopLiveHandlerCallback callback){}~~

<br>

### Declaration
```java
/**
  * @param context - Context
  * @param couponId - clicked coupon ID
  * @param callback - callback to send to ShopLive on coupon handling completion
  */
void handleDownloadCoupon(@NonNull Context context, @NonNull String couponId, @NonNull ShopLiveHandlerCallback callback);

```

<br>
<br>
<br>
<br>

## ShopLiveHandler.onChangeCampaignStatus
> Handler on campaign status change\
> \
> @Deprecated\
> ~~void changeCampaignStatus(@NonNull Context context, @NonNull String campaignStatus);~~

<br>

### Declaration
```java
/**
  * @param context - Context
  * @param campaignStatus - READY | ONAIR | CLOSED
  */
void onChangeCampaignStatus(@NonNull Context context, @NonNull String campaignStatus);
```

<br>
<br>
<br>
<br>

## ShopLiveHandler.onCampaignInfo
> Campaign Information in JSONObject Format\
> \
> @Deprecated \
> ~~void campaignInfo(@NonNull JSONObject campaignInfo);~~

<br>

### Declaration
```java
/**
  * @param campaignInfo - ex) {'title':'Campaign Title'}
  */
void onCampaignInfo(@NonNull JSONObject campaignInfo);
```

<br>
<br>
<br>
<br>

## ShopLiveHandler.onError
> message on errors before or during broadcast\
> \
> @Deprecated\
> ~~void error(@NonNull Context context, @NonNull String code, @NonNull String message);~~

<br>

### Declaration
```java
/**
  * @param context - Context
  * @param code - Error Code
  * @param message - Error Message
  */
void onError(@NonNull Context context, @NonNull String code, @NonNull String message);
```

<br>
<br>
<br>
<br>

## ShopLiveHandler.handleCustomAction
> you can receive user defined value when clicking banner or coupon on ShopLive.

<br>

### Declaration
```java
/**
  * @param context - Context
  * @param id - coupon or banner id
  * @param type - coupon or banner type
  * @param payload - use defined payload
  * @param callback - shoplive delivered to Shoplive on custom handling completion
  */
void handleCustomAction(@NonNull Context context, @NonNull String id, @NonNull String type, @NonNull String payload, @NonNull ShopLiveHandlerCallback callback);
```

<br>
<br>
<br>
<br>

## ShopLiveHandler.handlePreview
> Handler on ShopLive preview screen click\
> \
> Enters corresponding campaign on preview click. Overriding is necessary when manually implementing instead of entering campaign. But, even is only delivered in previews that are provided as 'Display over other apps'.

<br>

### Declaration
```java
/**
  * @param context - Context
  * @param campaignKey - Campaign Key
  */
default void handlePreview(@NonNull Context context, @NonNull String campaignKey) {}
```

<br>
<br>
<br>
<br>

## ShopLiveHandler.handleShare
> Handler when sharing is clicked during broadcast \
> Overriding is necessary when manually implementing without using Android Share Sheet.

<br>

### Declaration
```java
/**
  * @param context - Context
  * @param shareUrl - url or scheme to share
  */
default void handleShare(Context context, String shareUrl) {}
```

<br>
<br>
<br>
<br>

## ShopLiveHandler.onChangedPlayerStatus
> Receive Play status.

<br>

### Declaration
```java
/**
  * @param isPipMode - true:PIP, false:FULL
  * @param state - "CREATED" or "DESTROYED"
  */
default void onChangedPlayerStatus(@NonNull Boolean isPipMode, @NonNull String state) {}
```

<br>
<br>
<br>
<br>

## ShopLiveHandler.onSetUserName
> called when user name is changed.

<br>

### Declaration
```java
/**
  * @param jsonObject - ex) {'userId':'123', 'userName':'test'}
  */
default void onSetUserName(JSONObject jsonObject) {}
```

<br>
<br>
<br>
<br>

## ShopLiveHandler.onReceivedCommand
> called when a command is received

- [See COMMAND Lists](received-command.md)

### Declaration
```java
/**
  * function to receive command and payload
  * @param context - Context
  * @param command - delivered command
  * @param data - delivered data
  * */
default void onReceivedCommand(@NonNull Context context, @NonNull String command, @NonNull JSONObject data) {}

```

<br>
<br>
<br>
<br>
