# interface ShopLiveHandler

<br>

## ShopLiveHandler.handleNavigation
> ShopLive 에서 상품, 배너등을 터치 했을 때 관리자에서 설정한 url을 처리하기 위한 Handler

<br>

### Declaration
```java
/**
  * @param url - 이동할 url
  */
void handleNavigation(@NonNull String url);
```

<br>
<br>
<br>
<br>

## ShopLiveHandler.handleDownloadCoupon
> ShopLive 에서 쿠폰을 터치 했을 때 Handler\
> \
> @Deprecated\
> ~~default void handleDownloadCoupon(@NonNull String couponId, @NonNull > ShopLiveHandlerCallback callback){}~~

<br>

### Declaration
```java
/**
  * @param context - Context
  * @param couponId - 터치한 쿠폰의 ID
  * @param callback - 쿠폰 처리를 완료했을 때, ShopLive로 알려줄 callback
  */
void handleDownloadCoupon(@NonNull Context context, @NonNull String couponId, @NonNull ShopLiveHandlerCallback callback);

```

<br>
<br>
<br>
<br>

## ShopLiveHandler.onChangeCampaignStatus
> 방송 상태가 변경되었을 때 Handler\
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
> JSONObject 형태의 방송 정보\
> \
> @Deprecated \
> ~~void campaignInfo(@NonNull JSONObject campaignInfo);~~

<br>

### Declaration
```java
/**
  * @param campaignInfo - ex) {'title':'방송 제목'}
  */
void onCampaignInfo(@NonNull JSONObject campaignInfo);
```

<br>
<br>
<br>
<br>

## ShopLiveHandler.onError
> 방송 전 또는 방송 중 발생하는 오류 상황에 대한 메시지\
> \
> @Deprecated\
> ~~void error(@NonNull Context context, @NonNull String code, @NonNull String message);~~

<br>

### Declaration
```java
/**
  * @param context - Context
  * @param code - 오류 코드
  * @param message - 오류 메시지
  */
void onError(@NonNull Context context, @NonNull String code, @NonNull String message);
```

<br>
<br>
<br>
<br>

## ShopLiveHandler.handleCustomAction
> ShopLive 에서 배너 또는 쿠폰을 클릭했을 때, 사용자가 직접 정의한 값을 받을 수 있습니다.

<br>

### Declaration
```java
/**
  * @param context - Context
  * @param id - 쿠폰 또는 배너 id
  * @param type - 쿠폰 또는 배너 type
  * @param payload - 사용자가 정의한 payload
  * @param callback - 커스텀 처리를 완료했을 때, ShopLive로 알려줄 callback
  */
void handleCustomAction(@NonNull Context context, @NonNull String id, @NonNull String type, @NonNull String payload, @NonNull ShopLiveHandlerCallback callback);
```

<br>
<br>
<br>
<br>

## ShopLiveHandler.handlePreview
> ShopLive의 프리뷰 화면을 터치했을 때 Handler\
> \
> 프리뷰를 터치하면 기본적으로 해당 방송으로 진입합니다. 방송으로 진입하지 않고 직접 구현하려면 반드시 오버라이딩을 해야합니다. 단, '다른 앱 위에 표시'로 제공 되는 프리뷰에서만 이벤트가 전달됩니다. 'OS PIP'는 프리뷰 이벤트가 전달되지 않고 자동으로 Full 화면의 방송으로 진입합니다.

<br>

### Declaration
```java
/**
  * @param context - Context
  * @param campaignKey - 캠페인 키
  */
default void handlePreview(@NonNull Context context, @NonNull String campaignKey) {}
```

<br>
<br>
<br>
<br>

## ShopLiveHandler.handleShare
> 방송중 공유하기를 클릭 했을 때 Handler \
> 안드로이드의 Share Sheet를 사용하지 않고 직접 구현하려면 반드시 오버라이딩을 해야합니다.

<br>

### Declaration
```java
/**
  * @param context - Context
  * @param shareUrl - 공유할 url 또는 scheme
  */
default void handleShare(Context context, String shareUrl) {}
```

<br>
<br>
<br>
<br>

## ShopLiveHandler.onChangedPlayerStatus
> 플레이어의 상태를 전달 받습니다.

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
> 사용자 이름이 변경 되었을 시에 호출됩니다.

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
> 커맨드 명령어를 받았을 때 호출됩니다.

- [COMMAND 목록 보기](received-command.md)

### Declaration
```java
/**
  * command와 payload를 전달받기 위한 함수
  * @param context - Context
  * @param command - 전달 된 명령어
  * @param data - 전달 된 데이터
  * */
default void onReceivedCommand(@NonNull Context context, @NonNull String command, @NonNull JSONObject data) {}

```

<br>
<br>
<br>
<br>