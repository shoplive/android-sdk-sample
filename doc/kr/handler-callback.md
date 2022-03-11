# interface ShopLiveHandlerCallback

<br>

## ~~ShopLiveHandlerCallback.complete~~
> 쿠폰 다운로드가 완료 되었을 때, 호출합니다.

<br>

### Declaration
```java
@Deprecated
void complete()
```

<br>
<br>
<br>
<br>

## ShopLiveHandlerCallback.couponResult
> 쿠폰 다운로드가 완료 되었을 때, 성공 또는 실패 결과 그리고 메시지 등을 전달하기 위해 호출합니다.\
> \
> @Deprecated \
> ~~default void couponResult(\
    @NonNull Boolean isDownloadSuccess, \
    @NonNull String message, \
    @NonNull String couponStatus, \
    @NonNull String alertType) {}~~

<br>

### Declaration
```java
/**
  * @param isDownloadSuccess - 성공:true, 실패:false
  * @param message - 성공 또는 실패 메시지
  * @param couponStatus - 쿠폰다시활성:SHOW, 쿠폰사라짐:HIDE, 상태유지:KEEP
  * @param alertType - 팝업:ALERT, 메시지:TOAST
  * */
default void couponResult(
    @NonNull Boolean isDownloadSuccess, 
    @Nullable String message, 
    @NonNull ShopLive.CouponPopupStatus couponStatus, 
    @Nullable ShopLive.CouponPopupResultAlertType alertType
) {}
```

<br>
<br>
<br>
<br>

## ShopLiveHandlerCallback.customActionResult
> customAction 처리가 완료 되었을 때, 성공 또는 실패 결과 그리고 메시지 등을 전달하기 위해 호출합니다.\
> \
> @Deprecated \
> ~~default void customActionResult( \
    @NonNull Boolean isSuccess, \
    @NonNull String message,  \
    @NonNull String couponStatus, \
    @NonNull String alertType) {}~~

<br>

### Declaration
```java
/**
  * @param isSuccess - 성공:true, 실패:false
  * @param message - 성공 또는 실패 메시지
  * @param couponStatus - 다시활성:SHOW, 사라짐:HIDE, 상태유지:KEEP
  * @param alertType - 팝업:ALERT, 메시지:TOAST
  * */
default void customActionResult(
    @NonNull Boolean isSuccess, 
    @Nullable String message, 
    @NonNull ShopLive.CouponPopupStatus couponStatus, 
    @Nullable ShopLive.CouponPopupResultAlertType alertType
) {}
```

<br>
<br>
<br>
<br>
