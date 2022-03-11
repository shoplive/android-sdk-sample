# interface ShopLiveHandlerCallback

<br>

## ~~ShopLiveHandlerCallback.complete~~
> called when coupon download is complete.

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
> called to deliver success/failure and message on coupon download completion.\
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
  * @param isDownloadSuccess - success:true, failure:false
  * @param message - success or failure message
  * @param couponStatus - couponReactivate:SHOW, couponHidden:HIDE, maintainStatus:KEEP
  * @param alertType - popup:ALERT, message:TOAST
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
> called to deliver success/failure and message on customAction handling completion.\
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
  * @param isSuccess - success:true, failure:false
  * @param message - success or failure message
  * @param couponStatus - couponReactivate:SHOW, couponHidden:HIDE, maintainStatus:KEEP
  * @param alertType - popup:ALERT, message:TOAST
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
