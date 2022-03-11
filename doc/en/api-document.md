- [API - Play](#api---play)
  
  - [ShopLive.init](#shopliveinit)
  - [ShopLive.setAccessKey](#shoplivesetaccesskey)
  - [ShopLive.play](#shopliveplay)
  - [ShopLive.showPreview](#shopliveshowpreview)
  - [ShopLive.hidePreview](#shoplivehidepreview)
  - [ShopLive.resumePreview](#shopliveresumepreview)
  
    <br>

- [API - Option](#api---Option)
  
  - [ShopLive.setUser](#shoplivesetuser)
  - [ShopLive.setAuthToken](#shoplivesetauthtoken)
  - [ShopLive.getAuthToken](#shoplivegetauthtoken)
  - [ShopLive.setPIPRatio](#shoplivesetpipratio)
  - [ShopLive.setKeepAspectOnTabletPortrait](#shoplivesetkeepaspectontabletportrait)
  - [ShopLive.setKeepPlayVideoOnHeadphoneUnplugged](#shoplivesetkeepplayvideoonheadphoneunplugged)
  - [ShopLive.setAutoResumeVideoOnCallEnded](#shoplivesetautoresumevideooncallended)
  - [ShopLive.setShareScheme](#shoplivesetsharescheme)
  - [ShopLive.setLoadingProgressColor](#shoplivesetloadingprogresscolor)
  - [ShopLive.setLoadingAnimation](#shoplivesetloadinganimation)
  - [ShopLive.setChatViewTypeface](#shoplivesetchatviewtypeface)
  - [ShopLive.setEnterPipModeOnBackPressed](#shoplivesetenterpipmodeonbackpressed)
  - [ShopLive.close](#shopliveclose)
  - [ShopLive.setAutoCloseWhenAppDestroyed](#shoplivesetautoclosewhenappdestroyed)
  - [ShopLive.startPictureInPicture](#shoplivestartpictureinpicture)
  - [ShopLive.stopPictureInPicture](#shoplivestoppictureinpicture)
  - [ShopLive.getStatus](#shoplivegetstatus)
  - [ShopLive.isSuccessCampaignJoin](#shopliveissuccesscampaignjoin)
  
    <br>

- [Handler](#handler)
  
  - [ShopLive.setHandler](#shoplivesethandler)
  - [ShopLiveHandler.handleNavigation](#shoplivehandlerhandlenavigation)
  - [ShopLiveHandler.handleDownloadCoupon](#shoplivehandlerhandledownloadcoupon)
    - [`callback` ShopLiveHandlerCallback.couponResult](#callback-shoplivehandlercallbackcouponresult)
  - [ShopLiveHandler.handleCustomAction](#shoplivehandlerhandlecustomaction)
    - [`callback` ShopLiveHandlerCallback.customActionResult](#callback-shoplivehandlercallbackcustomactionresult)
  - [ShopLiveHandler.handleShare](#shoplivehandlerhandleshare)
  - [ShopLiveHandler.onChangeCampaignStatus](#shoplivehandleronchangecampaignstatus)
  - [ShopLiveHandler.onCampaignInfo](#shoplivehandleroncampaigninfo)
  - [ShopLiveHandler.onChangedPlayerStatus](#shoplivehandleronchangedplayerstatus)
  - [ShopLiveHandler.onSetUserName](#shoplivehandleronsetusername)
  - [ShopLiveHandler.handlePreview](#shoplivehandlerhandlepreview)
  - [ShopLiveHandler.onReceivedCommand](#shoplivehandleronreceivedcommand)
    - [ReceivedCommand List](#receivedcommand-list)
      - [CLICK\_PRODUCT\_DETAIL](#click_product_detail)
      - [CLICK\_PRODUCT\_CART](#click_product_cart)
      - [LOGIN\_REQUIRED](#login_required)
      - [ON\_SUCCESS\_CAMPAIGN\_JOIN](#on_success_campaign_join)
  - [ShopLive.setNextActionOnHandleNavigation](#shoplivesetnextactiononhandlenavigation)
  - [ShopLive.showDialogFragment](#shopliveshowdialogfragment)
  - [ShopLive.setEndpoint](#shoplivesetendpoint)
  - [ShopLiveHandler.onError](#shoplivehandleronerror)

<br>
<br>


# Android ShopLive API Document

<br>

## API - Play

- ### ShopLive.init
  
  > Initialize the Shoplive Android SDK.
  
  ```java
  void init(@NonNull Application app)
  ```
  
  | Parameter| Description
  |----------|----------
  | app| Application instance

    <br>
    
  > Sample code
  
  ```java
  public class application extends Application {
      ...
      @Override
      public void onCreate() {
          // Initializing the Shoplive Android SDK instance
          ShopLive.init();
      }
      ...
  }
  ```
  
    <br>

  > Application guide
  
  - [Installing](./index.md#step-1-installing)
  
    <br>

- ### ShopLive.setAccessKey
  
  > Set the `AccessKey` to use the Shoplive Android SDK.
  
  ```java
  void setAccessKey(@NonNull String accessKey)
  ```
  
  | Parameter| Description
  |----------|----------
  | accesskey| AccessKey from ShopLive representative

    <br>

  > Sample code
  
  ```java
  ShopLive.setAccessKey("{AccessKey}");
  ```
  
    <br>

  > Application guide
  
  - [Running Shoplive Player](./index.md#step-2-running-shoplive-player)
  
    <br>

- ### ShopLive.play
  
  > Play the video using `CampaignKey`.
  
  ```java
  void play()
  void play(@NonNull String campaignKey)
  ```
  
  | Parameter| Description
  |----------|----------
  | campaignKey| Campaign key of the video to be played <br>  However, if the `play` function is called without the campaign key, the default campaign is played.

    <br>

  > Sample code
  
  ```java
  ShopLive.play("{CampaignKey}");
  ```
  
    <br>

  > Application guide
  
  - [Running Shoplive Player](./index.md#step-2-running-shoplive-player)
  
    <br>

- ### ShopLive.showPreview
  
  > Use the campaign key to play muted the video with the preview.  
When the user sets `isOsPip` to  `true` and enters the campaign, it switches to the PIP mode.
  
  `An API that requires Android permission requests.`
  
  ```java
  void showPreview(@NonNull String campaignKey)
  // Required Permissions: Display over other apps
  void showPreview(@NonNull String campaignKey, Boolean isOsPip)
  ```
  
  | Parameter| Description
  |----------|----------
  | campaignKey| Campaign key
  | isOsPip| Whether to switch to OS PIP<br>`true`: Enter the campaign view and switch to the PIP mode

  The `isOsPip` setting is `true` only.
  
    <br>

  > Sample code
  
  ```java
  // Play the preview in normal view.
  ShopLive.showPreview("{CampaignKey}");
  
  // Play the preview with PIP View.
  ShopLive.showPreview("{CampaignKey}", true);
  ```
  
  > Application guide
  
  - [Campaign preview](./index.md#step-3-campaign-preview)
  
    <br>

- ### ShopLive.hidePreview
  
  > Hides the preview that is playing.
  
  ```java
  void hidePreview()
  ```
  
    <br>

  > Sample code
  
  ```java
  ShopLive.hidePreview();
  ```
  
    <br>

  > Application guide
  
  - [Campaign preview](./index.md#step-3-campaign-preview)

<br>

- ### ShopLive.resumePreview
  
  > Resume the preview.
  
  ```java
  void resumePreview()
  ```
  
    <br>

  > Sample code
  
  ```java
  ShopLive.resumePreview();
  ```
  
    <br>

  > Application guide
  
  - [Campaign preview](./index.md#step-3-campaign-preview)
  
    <br>


## API - Option

- ### ShopLive.setUser
  
  > Set the authenticated Shoplive user.  
Enter the user information for user authentication.
  
  ```java
  void setUser(@NonNull ShopLiveUser user)
  ```
  
    <br>

  > Sample code
  
  ```java
  ShopLiveUser user = new ShopLiveUser();
  user.setUserId("testId");
  user.setUserName("kate");
  user.setAge(20);
  user.setGender(ShopLiveUserGender.Female);
  user.setUserScore(100);
  
  ShopLive.setUser(user);
  ```
  
    <!-- [kotlin]
    ```kotlin
    val user = ShopLiveUser().apply {
        userId = "{USER_ID}"
        userName = "{USER_NAME}"
        gender = ShopLiveUserGender.Male
        age = 25
        userScore = 0
    }
    ShopLive.setUser(user)
    ``` -->
  - `enum` **ShopLiveUserGender**
    
    | Type| Description
    |----------|----------
    | Male| Male
    | Female| Female
    | Neutral| Neutral

  
  
    <br>
  > Application guide
  
  - [Integrate with simple authentication](./index.md#integrate-with-simple-authentication)
  
    <br>

- ### ShopLive.setAuthToken
  
  > A secure authentication token (JWT) string for authenticated users using Shoplive.   
Enter the security authentication token (JWT) string of the authenticated user for user authentication.
  
  ```java
  void setAuthToken(@NonNull String authToken)
  ```
  
    <br>

  > Sample code
  
  ```java
  String generatedJWT = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTA1NzA1dfadsfasfadsfasdfwO"
  
  ShopLive.setAuthToken(generatedJWT);
  ```
  
    <br>

  > Application guide
  
  - [Integrate with secure authentication](./index.md#integrate-with-secure-authentication)
  
    <br>

- ### ShopLive.getAuthToken
  
  > A secure authentication token (JWT) string for authenticated users using Shoplive.  
Returns the security authentication token (JWT) string of the authenticated user for user authentication.
  
  ```java
  String getAuthToken()
  ```
  
    <br>

  > Sample code
  
  ```java
  Log.d("TAG", "authToken = " + ShopLive.getAuthToken());
  ```
  
    <br>

- ### ShopLive.setPIPRatio
  
  > Default view ratio when starting PIP(Picture-in-Picture) mode.   
When playing for the first time and starting PIP(picture-in-picture) mode, PIP(picture-in-picture) mode starts with the specified view ratio.
  
  ```java
  void setPIPRatio(@NonNull ShopLivePIPRatio ratio)
  ```
  
    <br>

  > Sample code
  
  ```java
  ShopLive.setPIPRatio(ShopLivePIPRatio.RATIO_9X16);
  ```
  
  - `enum` **ShopLivePIPRatio**
    
    | Type| View ratio
    |----------|----------
    | RATIO\_1X1| 1 X 1
    | RATIO\_1X2| 1 X 2
    | RATIO\_2X3| 2 X 3
    | RATIO\_3X4| 3 X 4
    | RATIO\_9X16| 9 X 16

  
  
    <br>
  > Application guide
  
  - [Switching to PIP(Picture-in-Picture) mode](./index.md#step-9-switching-to-pippicture-in-picture-mode)
  
    <br>

- ### ShopLive.setKeepAspectOnTabletPortrait
  
  > Sets the aspect ratio of the Shoplive view in tablet portrait mode.   
`true`: Keep Aspect Ratio (default)  
`false`: Change Aspect Ratio to the maximum supported by the tablet
  
  ```java
  void setKeepAspectOnTabletPortrait(Boolean isKeepAspectOnTabletPortrait)
  ```
  
  | Parameter| Description
  |----------|----------
  | isKeepAspectOnTabletPortrait| `true`: Keep Aspect Ratio <br> `false`: Change Aspect Ration to the maximum supported by the tablet

    <br>

  > Sample code
  
  ```java
  ShopLive.setKeepAspectOnTabletPortrait(true);
  ```
  
    <br>

  > Application guide
  
  - [Setting the aspect ratio in tablet portrait mode](./index.md#step-12-setting-the-aspect-ratio-in-tablet-portrait-mode)
  
    <br>

- ### ShopLive.setKeepPlayVideoOnHeadphoneUnplugged
  
  > The video paused when the earphone (or headset) is unplugged.  
However, set the video to keep playing and handle it in a muted if necessary.
  
  ```java
  void setKeepPlayVideoOnHeadphoneUnplugged(Boolean isKeepPlayVideoOnHeadphoneUnplugged)
  void setKeepPlayVideoOnHeadphoneUnplugged(Boolean isKeepPlayVideoOnHeadphoneUnplugged, Boolean isMute)
  ```
  
  | Parameter| Description
  |----------|----------
  | isKeepPlayVideoOnHeadphoneUnplugged| `true`: keep playing video <br>  `false`: Pause video
  | isMute| `true`: system volume muting <br> `false`: keep system volume playing

    <br>

  > Sample code
  
  ```java
  // Set video to pause playing when the earphone (or headset) is disconnected.
  ShopLive.setKeepPlayVideoOnHeadphoneUnplugged(false);
  
  // Set video to keep playing when the earphone (or headset) is disconnected.
  ShopLive.setKeepPlayVideoOnHeadphoneUnplugged(true);
  
  // Set video to keep playing and muted when the earphone (or headset) is disconnected. 
  ShopLive.setKeepPlayVideoOnHeadphoneUnplugged(false, true));
  ```
  
    <br>

  > Application guide
  
  - [Interrupt due to earphone (or headset) disconnection](./index.md#interrupt-due-to-earphone-or-headset-disconnection)
  
    <br>

- ### ShopLive.setAutoResumeVideoOnCallEnded
  
  > When returning to the video after the end a call, the video auto resume to play.  
However, set the video to pause if necessary.
  
  `An API that requires Android permission requests.`
  
  - Required permission  
    `READ_PHONE_STATE`
  
    <br>
    
  ```java
  void setAutoResumeVideoOnCallEnded(Boolean isAutoResumeVideoOnCallEnded)
  ```
  
  | Parameter| Description
  |----------|----------
  | isAutoResumeVideoOnCallEnded| `true`: Keep playing video <br>  `false`: Pause video

    <br>

  > Sample code
  
  ```java
  ShopLive.setAutoResumeVideoOnCallEnded(true);
  ```
  
    <br>

  > Application guide
  
  - [Interrupt due to call connection](./index.md#interrupt-due-to-call-connection)
  
    <br>

- ### ShopLive.setShareScheme
  
  > Set the URL or scheme to be delivered when sharing is selected.
  
  ```java
  void setShareScheme(String shareUrl)
  ```
  
  | Parameter| Description
  |----------|----------
  | shareUrl| Scheme or URL to share

    <br>

  > Sample code
  
  ```java
  String scheme = "shoplive://live";
  String scheme = "https://shoplive.cloud/live";
  
  ShopLive.setShareScheme(scheme);
  ```
  
  > Application guide
  
  - [Sharing a Campaign link](./index.md#step-8-sharing-a-campaign-link)
  
    <br>

- ### ShopLive.setLoadingProgressColor
  
  > Sets the video loading progress bar color.
  
  ```java
  void setLoadingProgressColor(String hexColor)
  ```
  
   | Parameter| Description
   |----------|----------
   | hexColor| Color to set for progress bar  <br> Input as hex value

    <br>

  > Sample code
  
  ```java
  // Enter the hex value of the color. ex) "#FFFFFF"
  ShopLive.setLoadingProgressColor("#FFFFFF");
  ```
  
    <br>

  > Application guide
  
  - [Setting the progress bar](./index.md#step-10-setting-the-progress-bar)
  
    <br>

- ### ShopLive.setLoadingAnimation
  
  > Set the video loading progress bar to an image animation.
 
  ```java
  void setLoadingAnimation(int animationDrawable)
  ```
  
  | Parameter| Description
  |----------|----------
  | animationDrawable| Image array resource

    <br>

  > Sample code
  
  ```java
  // Enable image animation
  ShopLive.setLoadingAnimation(R.drawable.progress_animation);
  
  // Disable image animation
  ShopLive.setLoadingAnimation(0);
  ```
  
    <br>

  > Application guide
  
  - [Setting the progress bar](./index.md#step-10-setting-the-progress-bar)
  
    <br>

- ### ShopLive.setChatViewTypeface
  
  > Set the chat font and chat send button font.  
Call the `setChatViewTypeface(typeface)` function to apply the same chat font and chat send button font.  
Call the `setChatViewTypeface(inputBoxTypeface, sendButtonTypeface)` function to apply the different chat font and chat send button font.
  
  ```java
  void setChatViewTypeface(Typeface typeface)
  void setChatViewTypeface(Typeface inputBoxTypeface, Typeface sendButtonTypeface)
  ```
  
  | Parameter| Description
  |----------|----------
  | typeface| Font to set the chat font and chat send button font in common
  | inputBoxTypeface| chat font
  | sendButtonTypeface| chat send button font

    <br>

  > Sample code
  
  ```java
  Typeface nanumGothic = getResources().getFont(R.font.nanumgothic);
  Typeface nanumGothicBold = getResources().getFont(R.font.nanumgothicbold);
  
  /*
      common setting for chat font and chat send button font 
      setChatViewTypeface(Typeface typeface) 
  */
  ShopLive.setChatViewTypeface(nanumGothic);
  
  /*
      Individual setting for chat font and chat send button font
      setChatViewTypeface(Typeface inputBoxTypeface, Typeface sendButtonTypeface)
  */
  ShopLive.setChatViewTypeface(nanumGothic, nanumGothicBold);
  ```
  
    <br>

  > Application guide
  
  - [Changing the chat font](./index.md#step-5-changing-the-chat-font)
  
    <br>

- ### ShopLive.setEnterPipModeOnBackPressed
  
  > If the user exits the view by selecting the Back button while watching the video, the video ends.  
However, use this option to switch to PIP mode to keep watching the video. 
  
  ```java
  void setEnterPipModeOnBackPressed(Boolean isEnterPipModeOnBackPressed)
  ```
  
  | Parameter| Description
  |----------|----------
  | isEnterPipModeOnBackPressed| `true` Switch to PIP mode <br> `false` Video end

    <br>

  > Sample code
  
  ```java
  ShopLive.setEnterPipModeOnBackPressed(true);
  ```
  
    <br>

- ### ShopLive.close
  
  > Close the Campaign the user is watching.
  
  ```java
  void close()
  ```
  
    <br>

  > Sample code
  
  ```java
  ShopLive.close();
  ```
  
    <br>

- ### ShopLive.setAutoCloseWhenAppDestroyed
  
  > Sets the Campaign not to end even when the root activity ends.  
  
  The reason for using this option is to terminate by directly calling the `ShopLive.close` function at the appropriate time when the app is closed using the `Intent.FLAG_ACTIVITY_NEW_TASK` option because the Campaign is able to end when you re-run the root activity.  
  - Default: `true`  
  - If not set, the Campaign ends when the app is closed.
  
  <br>

  ```java
  void setAutoCloseWhenAppDestroyed(Boolean isAutoClose)
  ```
  
  | Parameter| Description
  |----------|----------
  | isAutoClose| `true`: Campaign end when the app is closed (default) <br> `false`: Campaign does not end when the app is closed

    <br>

  > Sample code
  
  ```java
  ShopLive.setAutoCloseWhenAppDestroyed(false);
  ```
  
    <br>

- ### ShopLive.startPictureInPicture
  
  > Switch from full-view mode to PIP mode.
  
  ```java
  void startPictureInPicture()
  ```
  
    <br>

  > Sample code
  
  ```java
  ShopLive.startPictureInPicture();
  ```
  
    <br>

  > Application guide
  
  - [Switching to PIP(Picture-in-Picture) mode](./index.md#step-9-switching-to-pippicture-in-picture-mode)
  
    <br>

- ### ShopLive.stopPictureInPicture
  
  > Switch the PIP mode to full-view mode.
  
  ```java
  void stopPictureInPicture()
  ```
  
    <br>

  > Sample code
  
  ```java
  ShopLive.stopPictureInPicture();
  ```
  
    <br>

  > Application guide
  
  - [Switching to PIP(Picture-in-Picture) mode](./index.md#step-9-switching-to-pippicture-in-picture-mode)
  
    <br>

- ### ShopLive.getStatus
  
  > This is the current Shoplive Player view status.
  
  ```java
  Status getStatus()
  ```
  
  - `enum` **Status** 
  
    | Status | Description | 
    | ---------- | --- | 
    | None | ShopLive status None | 
    | PIP | PIP(picture-in-picture) mode | 
    | Full | Full view mode |
  
    <br>

  > Sample code
  
  ```java
  Log.d("TAG", "status = " + ShopLive.getStatus());
  ```
  
    <br>

- ### ShopLive.isSuccessCampaignJoin
  
  > This information is delivered when the campaign join is successful.
  
  ```java
  boolean isSuccessCampaignJoin()
  ```
  
    <br>

  > Sample code
  
  ```java
  Log.d("TAG", "isSuccessCampaignJoin = " + ShopLive.isSuccessCampaignJoin());
  ```
  
    <br>


## Handler

> The client receives the notification generated by Shoplive Player through the handler function and performs the necessary handling.

- ### ShopLive.setHandler
  
  > This is the ShopLive event handler.
  
  ```java
  void setHandler(@NonNull ShopLiveHandler handler)
  ```
  
    <br>

  > Sample code
  
  ```java
  import cloud.shoplive.sdk.ShopLive;
  import cloud.shoplive.sdk.ShopLiveHandler;
  import cloud.shoplive.sdk.ShopLiveHandlerCallback;
  
  public class MainActivity extends AppCompatActivity {
  
      private ShopLiveHandler handler = new ShopLiveHandler() {
          @Override
          public void handleNavigation(Context context, @NonNull String url) {
              Log.d(TAG, "handleNavigation >> url : " + url);
          }
  
          @Override
          public void onChangeCampaignStatus(@NonNull Context context, @NonNull String campaignStatus) {
              Log.d(TAG, "onChangeCampaignStatus >> " + campaignStatus);
          }
          ...
      };
  
      private void init() {
          ShopLive.setHandler(handler);
      }
      ...
  }
  ```
  
    <br>

  > Application guide
  
  - [Registering handler](./index.md#registering-handler)
  
    <br>

- ### ShopLiveHandler.handleNavigation
  
  > When selecting a product, banner, etc. in Shoplive, deliver the selected product or banner information.
  
  ```java
  void handleNavigation(Context context, @NonNull String url) {}
  ```
  
  | Parameter| Description
  |----------|----------
  | url| URL to go to when product or banner is selected

    <br>

  > Sample code
  
  ```java
  private ShopLiveHandler handler = new ShopLiveHandler() {
      ...
      @Override
      public void handleNavigation(Context context, @NonNull String url) {
          ShopLive.ActionType type = {"your_action_type"};
          switch (type) {
              case PIP:
              case CLOSE:
                  Intent intent = new Intent(SampleActivity.this, WebViewActivity.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  intent.putExtra("url", url);
                  startActivity(intent);
                  break;
              case KEEP:
                  DialogFragment webDialogFragment = new WebViewDialogFragment(url);
                  ShopLive.showDialogFragment(webDialogFragment);
                  break;
              default:
                  break;
          }
      }
      ...
  }
  ```
  
    <br>

  > Application guide
  
  - [Registering handler](./index.md#registering-handler)
  
    <br>

- ### ShopLiveHandler.handleDownloadCoupon
  
  > When a coupon is selected in Shoplive, deliver the coupon information to the client. Set the coupon status in Shoplive Player through the `callback`, which delivers the client's coupon handling result back to the Shoplive Android SDK.
  
  ```java
  void handleDownloadCoupon(@NonNull Context context, @NonNull String couponId, @NonNull ShopLiveHandlerCallback callback) {}
  ```
  
  | Parameter| Description
  |----------|----------
  | context| Context
  | couponId| Selected Coupon ID
  | callback| `callback` to notify ShopLive when coupon handling is complete

    <br>

  - #### `callback` ShopLiveHandlerCallback.couponResult
    
    > When the coupon download is completed, it is called to deliver the success or failure of the result, a message, etc.
    
    ```java
    /**
    * @param isDownloadSuccess - true: success, false: failure
    * @param message - Success or failure message
    * @param couponStatus - SHOW: coupon reactivation, HIDE: coupon hide, KEEP: keep status
    * @param alertType - ALERT: pop-up, TOAST: message
    * */
    default void couponResult(
        @NonNull Boolean isDownloadSuccess, 
        @Nullable String message, 
        @NonNull ShopLive.CouponPopupStatus couponStatus, 
        @Nullable ShopLive.CouponPopupResultAlertType alertType
    ) {}
    ```
    
      <br>
  
  
     > Sample code
  
      ```java
     private ShopLiveHandler handler = new ShopLiveHandler() {
      ...
          @Override
          public void handleDownloadCoupon(@NonNull Context context, @NonNull String couponId, @NonNull ShopLiveHandlerCallback callback) {
              AlertDialog.Builder builder = new AlertDialog.Builder(context);
             builder.setTitle("Coupon Download");
             builder.setMessage("couponId " + couponId);
              builder.setPositiveButton("success", (dialog, which) -> {
  
                  // Set callback on success
                  callback.couponResult(
                          true,
                          "Success to download coupon.",
                          ShopLive.CouponPopupStatus.HIDE,
                          ShopLive.CouponPopupResultAlertType.TOAST
                  );
              });

              builder.setNegativeButton("failure", (dialog, which) -> {

                  // Set callback on failure
                  callback.couponResult(
                          false,
                          "Failed to download coupon.",
                          ShopLive.CouponPopupStatus.SHOW,
                          ShopLive.CouponPopupResultAlertType.ALERT
                  );
              });
  
              builder.setCancelable(false);
  
              Dialog dialog = builder.create();
              dialog.show();
          }
          ...
      }
      ```
  
    <br>

     > Application guide
  
      - [Using a coupon](./index.md#step-6-using-a-coupon)
  
    <br>

- ### ShopLiveHandler.handleCustomAction
  
  > When the selection event is designated as `custom` in the popup and the popup is selected, popup information is delivered. Delivers `id`, `type`, `payload`.
  
  ```java
  void handleCustomAction(@NonNull Context context, @NonNull String id, @NonNull String type, @NonNull String payload, @NonNull ShopLiveHandlerCallback callback) {}
  ```
  
  | Parameter| Description
  |----------|----------
  | context| Context
  | id| Coupon or banner ID
  | type| Coupon or banner type
  | payload| Custom payload
  | callback| A callback to notify ShopLive when custom handling is complete

  

  - #### `callback` ShopLiveHandlerCallback.customActionResult
    
    > When `customAction` handling is complete, it is called to deliver a message, whether the result of success or failure, etc.
    
  

    ```java
    /**
    * @param isSuccess - true: success, false: failure
    * @param message - Success or failure message
    * @param couponStatus - SHOW: coupon reactivation, HIDE: coupon hide, KEEP: keep status
    * @param alertType - ALERT: pop-up, TOAST: message
    * */
    default void customActionResult(
        @NonNull Boolean isSuccess, 
        @Nullable String message, 
        @NonNull ShopLive.CouponPopupStatus couponStatus, 
        @Nullable ShopLive.CouponPopupResultAlertType alertType
    ) {}
    ```
    
      <br>
    
     > Sample code
  
    ```java
    private ShopLiveHandler handler = new ShopLiveHandler() {
        ...
        @Override
        public void handleCustomAction(@NonNull Context context, @NonNull String id, @NonNull String type, @NonNull String payload, @NonNull ShopLiveHandlerCallback callback) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Custom Actions (Popup))");
            builder.setMessage("id: " + id + " type: " + type + " payload: " + payload);
            builder.setPositiveButton("success", (dialog, which) -> {
              
                // Set a callback on success
                callback.couponResult(
                        true,
                        "Custom action (Popup) processing was successful.",
                        ShopLive.CouponPopupStatus.HIDE,
                        ShopLive.CouponPopupResultAlertType.TOAST
                );
            });
  
            builder.setNegativeButton("failure", (dialog, which) -> {
              
                // Set a callback on failure
                callback.couponResult(
                        false,
                        "Custom action (Popup) processing failed.",
                        ShopLive.CouponPopupStatus.SHOW,
                        ShopLive.CouponPopupResultAlertType.ALERT
                );
            });
  
            builder.setCancelable(false);

            Dialog dialog = builder.create();
            dialog.show();
        }
        ...
    }
    ```
  
    <br>

    > Application guide
  
    - [Using a coupon](./index.md#step-6-using-a-coupon)
  
    <br>

- ### ShopLiveHandler.handleShare
  
  > Handler when the user selects to share during the Campaign.   
Without using Android's Share Sheet, you must `override` it to implement directly.
  
  ```java
  default void handleShare(Context context, String shareUrl) {}
  ```
  
  | Parameter| Description
  |----------|----------
  | context| Context
  | shareUrl| URL or scheme to share

    <br>

  > Sample code
  
  ```java
  private ShopLiveHandler handler = new ShopLiveHandler() {
      ...
      @Override
      public void handleShare(Context context, String shareUrl) {
          // Custom Share Popup Dialog
          LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
          View view = inflater.inflate(R.layout.custom_share_dialog, null);
          TextView tvMessage = view.findViewById(R.id.tvMessage);
          tvMessage.setText(shareUrl);
  
          Button btCopy = view.findViewById(R.id.btCopy);
          Button btKakao = view.findViewById(R.id.btKakao);
          Button btLine = view.findViewById(R.id.btLine);
  
          AlertDialog.Builder builder = new AlertDialog.Builder(context);
          builder.setTitle(getString(R.string.sample_share));
          builder.setView(view);
  
          Dialog dialog = builder.create();
          dialog.show();
  
          btCopy.setOnClickListener(v -> {
              Toast.makeText(context, getString(R.string.sample_copy_link), Toast.LENGTH_SHORT).show();
              dialog.dismiss();
          });
  
          btKakao.setOnClickListener(v -> {
              Toast.makeText(context, getString(R.string.sample_share_kakao), Toast.LENGTH_SHORT).show();
              dialog.dismiss();
          });
  
          btLine.setOnClickListener(v -> {
              Toast.makeText(context, getString(R.string.sample_share_line), Toast.LENGTH_SHORT).show();
              dialog.dismiss();
          });
      }
      ...
  }
  ```
  
    <br>

  > Application guide
  
   - [Sharing a Campaign link](./index.md#step-8-sharing-a-campaign-link)


    <br>

- ### ShopLiveHandler.onChangeCampaignStatus
  
  > A Handler when the campaign status has changed.
  
  ```java
  /**
  * @param context - Context
  * @param campaignStatus - READY | ONAIR | CLOSED
  */
  void onChangeCampaignStatus(@NonNull Context context, @NonNull String campaignStatus) {}
  ```
  
  
    | Parameter| Description 
    |----------|---------- 
    | context| Context 
    | campaignStatus| campaignStatus<br> `READY`, `ONAIR`, `CLOSED` 

    <br>

  > Sample code
  
  ```java
  private ShopLiveHandler handler = new ShopLiveHandler() {
      ...
      @Override
      public void onChangeCampaignStatus(@NonNull Context context, @NonNull String campaignStatus) {
          Log.d(TAG, "onChangeCampaignStatus >> " + campaignStatus);
      }
      ...
  }
  ```
  
    <br>

- ### ShopLiveHandler.onCampaignInfo
  
  > This is campaign information in JSON Object format.
  
  ```java
  void onCampaignInfo(@NonNull JSONObject campaignInfo) {}
  ```
  
  | Parameter| Description
  |----------|----------
  | campaignInfo| campaignInfo<br> ex) `{'title':'Campaign Title'}`

    <br>

  > Sample code
  
  ```java
  private ShopLiveHandler handler = new ShopLiveHandler() {
      ...
      @Override
      public void onCampaignInfo(@NonNull JSONObject campaignInfo) {
          Log.d(TAG, "onCampaignInfo >> " + campaignInfo.toString());
      }
      ...
  }
  ```
  
    <br>

- ### ShopLiveHandler.onChangedPlayerStatus
  
  > Receive the Shoplive Player status.
  
  ```java
  /**
  * @param isPipMode - true:PIP, false:FULL
  * @param state - "CREATED" or "DESTROYED"
  */
  default void onChangedPlayerStatus(@NonNull Boolean isPipMode, @NonNull String state) {}
  ```
  
  | Parameter| Description
  |----------|----------
  | isPipMode| Current PIP mode<br> ex) `{'title':'Campaign Title'}`
  | state| Shoplive Player state<br> `CREATED`, `DESTROYED`

    <br>

  > Sample code
  
  ```java
  private ShopLiveHandler handler = new ShopLiveHandler() {
      ...
      @Override
      public void onChangedPlayerStatus(@NonNull Boolean isPipMode, @NonNull String state) {
          Log.d(TAG, "isPipMode:" + isPipMode + ", state:" + state);
      }
      ...
  }
  ```
  
    <br>

- ### ShopLiveHandler.onSetUserName
  
  > Called when changing the user name.
  
  ```java
  default void onSetUserName(JSONObject jsonObject) {}
  ```
  
  | Parameter| Description
  |----------|----------
  | jsonObject| User information<br> ex) `{'userId':'123', 'userName':'test'}`

    <br>

  > Sample code
  
  ```java
  private ShopLiveHandler handler = new ShopLiveHandler() {
      ...
      @Override
      public void onSetUserName(JSONObject jsonObject) {
          Log.d(TAG, "onSetUserName >> " + jsonObject.toString());
          try {
              Toast.makeText(SampleActivity.this,
                      "userId=" + jsonObject.get("userId") + ", userName=" + jsonObject.get("userName"),
                      Toast.LENGTH_SHORT).show();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
      ...
  }
  ```
  
    <br>

- ### ShopLiveHandler.handlePreview
  
  > Handler when selecting the ShopLive preview.
  > 
  > Select the preview to enter the Campaign. (Default)  
Without entering the campaign, you must `override` if you want to implement it directly.  
However, the event is delivered only in the preview provided as `Draw over other apps`\.  
In `OS PIP` mode, preview events are not delivered and automatically enters full-view Campaign.
  
  ```java
  default void handlePreview(@NonNull Context context, @NonNull String campaignKey) {}
  ```
  
  | Parameter| Description
  |----------|----------
  | context| Context
  | campaignKey| Campaign key

    <br>

  > Sample code
  
  ```java
  private ShopLiveHandler handler = new ShopLiveHandler() {
      ...
      @Override
      public void handlePreview(@NonNull Context context, @NonNull String campaignKey) {
          Log.d(TAG, "ck=" + campaignKey);
      }
      ...
  }
  ```
  
    <br>

  > Application guide
  - [Campaign preview](./index.md#step-3-campaign-preview)
  
    <br>

- ### ShopLiveHandler.onReceivedCommand
  
  > Called when a `command` is received.
  
  ```java
  default void onReceivedCommand(@NonNull Context context, @NonNull String command, @NonNull JSONObject data) {}
  ```
  
  | Parameter| Description
  |----------|----------
  | context| Context
  | command| Delivered command
  | data| Delivered data
    
  - #### ReceivedCommand List
    
    - ##### CLICK\_PRODUCT\_DETAIL
      
      > This is product information delivered when a user opens a product list on the campaign view and selects a product.
      
      ```javascript
      command:
      CLICK_PRODUCT_DETAIL
      
      payload:
      {
          action = Link;
          brand = Brand;
          sku = product01234;
          url = "http://product.com";
      }
      ```
    
      <br>
    - ##### CLICK\_PRODUCT\_CART
      
      > This is shopping cart information delivered when a user opens a product list on the campaign view and selects a shopping cart button.
      
      ```javascript
      command:
      CLICK_PRODUCT_CART
      
      payload:
      {
      action = LINK;
          brand = "company brand";
          ...
          url = "https://company.com/product/12345/detail";
      }
      ```
      
        <br>  
    
    - ##### LOGIN\_REQUIRED
      
      > Request a sign-in action that is delivered when an app applied by the Shoplive Android SDK requires sign-in.
      
      ```javascript
      command:
      LOGIN_REQUIRED
      
      payload: None
      ```
      
        <br>
    
    - ##### ON\_SUCCESS\_CAMPAIGN\_JOIN
      
      > This information is delivered when the campaign join is successful.
      
      ```javascript
      command:
      ON_SUCCESS_CAMPAIGN_JOIN
      
      payload:
      {
      isGuest: 1 // 1: unauthenticated user, 0: authenticated user
      }
      ```
      
        <br>

      > Sample code
      
      ```java
      private ShopLiveHandler handler = new ShopLiveHandler() {
          ...
           @Override
           public void onReceivedCommand(@NonNull Context context, @NonNull String command, @NonNull JSONObject data) {
               Log.d(TAG, "onReceivedCommand >> command:" + command + "data:" + data.toString());
      
                switch(command) {
                    case "LOGIN_REQUIRED" :
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(getString(R.string.alert_need_login));
                            builder.setPositiveButton(getString(R.string.yes), ((dialog, which) -> {
      
                           }));
      
                            Dialog dialog = builder.create();
                         dialog.show();
                     }
                       break;
                }
            }
            ...
           }
           ```
      ```
  
    <br>

- ### ShopLive.setNextActionOnHandleNavigation
    > Set next Shoplive Player action when a user selects a link, such as a product, announcement, or banner.

    ```java
    void setNextActionOnHandleNavigation(ActionType type)
    ```
    
    - `enum` **ActionType** 

    | Parameter | Description |
    | --- | --- |
    | PIP | Switch to PIP |
    | KEEP | Keep in status |
    | CLOSE | Close |

    <br>

    > Sample code
    ```kotlin
    ShopLive.setNextActionOnHandleNavigation(ActionType.PIP)
    ```

    > Application guide
    - [Setting Shoplive Player's next action when a user selects a link](./index.md#step-11-setting-shoplive-players-next-action-when-a-user-selects-a-link)

    <br>

- ### ShopLive.showDialogFragment
    > Shoplive Player 화면에 사용자의 `DialogFragment`를 표시하기 위해 사용합니다.

    ```java
    void showDialogFragment(DialogFragment dialogFragment)
    ```
    
    | Parameter | Description |
    | --- | --- |
    | dialogFragment | User's  `DialogFragment` |

    <br>

    > Sample code
    ```kotlin
    override fun handleNavigation(context: Context, url: String) {
        val nexcActionType = "{your_action_type}"
        when(nexcActionType) {
            ShopLive.ActionType.KEEP -> {
                ShopLive.showDialogFragment(WebViewDialogFragment(url))
            }
        }            
    }
    ```

    > Application guide
    - [Setting Shoplive Player's next action when a user selects a link](./index.md#step-11-setting-shoplive-players-next-action-when-a-user-selects-a-link)
    
    <br>

- ### ShopLive.setEndpoint
    > Use this when you need to use the Shoplive service on a specific landing page or URL that is not the Shoplive Web landing page.  
    > For example, if the Shoplive landing page is not available due to security reasons, create a landing page with a specific domain.  
    > ※ Using the Shoplive service on a specific landing page or URL is applicable only after consultation with Shoplive.
        
    ```java
    void setEndpoint(String url)
    ```
    
    | Parameter | Description |
    | --- | --- |
    | url | Shoplive Player's Web landing URL |

    <br>

    > Sample code
    ```kotlin
    ShopLive.setEndpoint("https://www.your_landingpage.com/")
    ```
    
    <br>

- ### ShopLiveHandler.onError
  
  > Delivers a message about an error status that occurs before or during the campaign.
  
  ```java
  void onError(@NonNull Context context, @NonNull String code, @NonNull String message) {}
  ```
  
  | Parameter| Description
  |----------|----------
  | context| Context
  | code| Error code
  | message| Error message

    <br>

  > Sample code
  
  ```java
  private ShopLiveHandler handler = new ShopLiveHandler() {
      ...
      @Override
      public void onError(@NonNull Context context, @NonNull String code, @NonNull String message) {
          Log.d(TAG, "code:" + code + ", message:" + message);
      }
      ...
  }
  ```
  
    <br>
