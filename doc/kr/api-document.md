# Android ShopLive API Document

## 목차
<br>

- [API - Play](#api---play)
    - [ShopLive.init](#shopliveinit)
    - [ShopLive.setAccessKey](#shoplivesetaccesskey)
    - [ShopLive.play](#shopliveplay)
    - [ShopLive.showPreview](#shopliveshowpreview)  
    - [ShopLive.hidePreview](#shoplivehidepreview)
    - [ShopLive.resumePreview](#shopliveresumepreview)
    
    <br>
- [API - Option](#api---option)
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
        - [ReceivedCommand 목록](#receivedcommand-목록)
            - [CLICK_PRODUCT_DETAIL](#click_product_detail)
            - [CLICK_PRODUCT_CART](#click_product_cart)
            - [LOGIN_REQUIRED](#login_required)
            - [ON_SUCCESS_CAMPAIGN_JOIN](#on_success_campaign_join)
    - [ShopLive.setNextActionOnHandleNavigation](#shoplivesetnextactiononhandlenavigation)
    - [ShopLive.showDialogFragment](#shopliveshowdialogfragment)
    - [ShopLive.setEndpoint](#shoplivesetendpoint)
    - [ShopLiveHandler.onError](#shoplivehandleronerror)

<br>










---

<br>

## API - Play

<br>

- ### ShopLive.init

    > Shoplive Android SDK를 초기화합니다.

    ```java
    void init(@NonNull Application app)
    ```
    
    | 매개변수 | 설명 |
    | ---------- | --- |
    | app  | Application 인스턴스  |

    <br>

    > 샘플 코드
    ```java
    public class application extends Application {
        ...
        @Override
        public void onCreate() {
            // Shoplive Android SDK 인스턴스 초기화
            ShopLive.init();
        }
        ...
    }
    ```
    <br>

    > 적용 가이드  
    - [설치하기](./index.md#step-1-설치하기)
    
    <br>

- ### ShopLive.setAccessKey

    > Shoplive Android SDK를 사용할 수 있도록 `AccessKey`를 설정합니다.

    <br>

    ```java
    void setAccessKey(@NonNull String accessKey)
    ```

    | 매개변수 | 설명  |
    | ---------- | --- |
    | accesskey  | ShopLive 담당자에게 받은 AccessKey  |

    <br>

    > 샘플 코드
    ```java
    ShopLive.setAccessKey("{AccessKey}");
    ```
    
    <br>

    > 적용 가이드  
    - [Shoplive Player 실행하기](./index.md#step-2-shoplive-player-실행하기)

    <br>

- ### ShopLive.play

    > `CampaignKey`를 사용하여 동영상을 재생합니다.

    ```java
    void play()
    void play(@NonNull String campaignKey)
    ```

    | 매개변수 | 설명 |
    | ---------- | --- |
    | campaignKey | 재생할 동영상의 캠페인(방송) 키 <br>  재생할 동영상의 캠페인(방송) 키 없이 `play` 함수를 호출하면 기본 캠페인(방송)을 재생합니다. |

    <br>

    > 샘플 코드
    ```java
    ShopLive.play("{CampaignKey}");
    ```

    <br>

    > 적용 가이드  
    - [Shoplive Player 실행하기](./index.md#step-2-shoplive-player-실행하기)

    <br>

- ### ShopLive.showPreview
    > 캠페인(방송) 키를 사용하여 미리보기 화면으로 동영상을 무음 재생합니다.\
    > `isOsPip`를 `true`로 설정하고 방송 화면에 진입하면  PIP 화면 모드로 전환됩니다.

    `안드로이드 권한 요청이 필요한 API입니다.` 
    

    ```java
    void showPreview(@NonNull String campaignKey)
    // 필요 권한: 다른 앱 위에 표시
    void showPreview(@NonNull String campaignKey, Boolean isOsPip)
    ```

    | 매개변수 | 설명 |
    | ---------- | --- |
    | campaignKey | 캠페인(방송) 키 |
    | isOsPip | OS PIP로 전환 여부<br>`true`: 방송 화면 진입 후 PIP 화면 모드로 전환 |

    `isOsPip`는 `true`만 설정할 수 있습니다.

    <br>

    > 샘플 코드
    ```java
    // 일반 View로 미리보기를 재생합니다.
    ShopLive.showPreview("{CampaignKey}");

    // PIP View로 미리보기를 재생합니다.
    ShopLive.showPreview("{CampaignKey}", true);
    ```

    > 적용 가이드  
    - [방송 미리보기](./index.md#step-3-방송-미리보기)

    <br>

- ### ShopLive.hidePreview

    > 재생 중인 미리보기 화면을 숨깁니다.

    ```java
    void hidePreview()
    ```

    <br>

    > 샘플 코드
    ```java
    ShopLive.hidePreview();
    ```
    <br>

    > 적용 가이드  
    - [방송 미리보기](./index.md#step-3-방송-미리보기)

<br>

- ### ShopLive.resumePreview

    > 숨겨진 미리보기 화면을 다시 표시합니다.

    ```java
    void resumePreview()
    ```

    <br>

    > 샘플 코드
    ```java
    ShopLive.resumePreview();
    ```

    <br>

    > 적용 가이드  
    - [방송 미리보기](./index.md#step-3-방송-미리보기)

    <br>

---
<br>

## API - Option

<br>

- ### ShopLive.setUser
    > Shoplive를 사용하는 인증 사용자를 설정합니다.\
    > 사용자 인증을 하려면 사용자 정보를 입력하세요.

    ```java
    void setUser(@NonNull ShopLiveUser user)
    ```

    <br>

    > 샘플 코드

    
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

    
    -  `enum` **ShopLiveUserGender** 

        | 타입 | 설명 |
        | ---------- | --- |
        | Male | 남성 |
        | Female | 여성 |
        | Neutral | 성별 지정 안 함 |

    <br>

    > 적용 가이드  
    - [간편 인증으로 연동하기](./index.md#간편-인증으로-연동하기)

    <br>

- ### ShopLive.setAuthToken
    > Shoplive를 사용하는 인증된 사용자의 보안 인증토큰(JWT) string입니다. \
    > 사용자 인증을 위해 인증된 사용자의 보안 인증토큰(JWT) string을 입력합니다.

    ```java
    void setAuthToken(@NonNull String authToken)
    ```

    <br>
    
    > 샘플 코드
    ```java
    String generatedJWT = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTA1NzA1dfadsfasfadsfasdfwO"

    ShopLive.setAuthToken(generatedJWT);
    ```

    <br>

    > 적용 가이드  
    - [보안 인증으로 연동하기](./index.md#보안-인증으로-연동하기)


    <br>

- ### ShopLive.getAuthToken
    > Shoplive를 사용하는 인증된 사용자의 보안 인증토큰(JWT) string입니다.\
    > 사용자 인증을 위해 인증된 사용자의 보안 인증토큰(JWT) string을 반환합니다.

    ```java
    String getAuthToken()
    ```

    <br>

    > 샘플 코드
    ```java
    Log.d("TAG", "authToken = " + ShopLive.getAuthToken());
    ```
    
    <br>
    
- ### ShopLive.setPIPRatio
    > PIP 화면 모드를 시작할 때 기본 화면 배율입니다. \
    > 처음 재생 후 PIP 화면 모드를 시작하면 지정된 화면 배율로 PIP 화면 모드를 시작합니다.

    ```java
    void setPIPRatio(@NonNull ShopLivePIPRatio ratio)
    ```

    <br>

    > 샘플 코드
    ```java
    ShopLive.setPIPRatio(ShopLivePIPRatio.RATIO_9X16);
    ```


    - `enum` **ShopLivePIPRatio** 

        | 타입 | 화면 배율 |
        | --- | --- |
        | RATIO_1X1 | 1 X 1 |
        | RATIO_1X2 | 1 X 2 |
        | RATIO_2X3 | 2 X 3 |
        | RATIO_3X4 | 3 X 4 |
        | RATIO_9X16 | 9 X 16 |

    <br>

    > 적용 가이드  
    - [PIP 화면 모드로 전환하기](./index.md#step-9-pip-화면-모드로-전환하기)

    <br>

- ### ShopLive.setKeepAspectOnTabletPortrait
    > 태블릿 세로모드에서 Shoplive 화면 비율을 설정합니다. \
    > `true`: 화면 비율 유지 (기본값) \
    > `false`: 화면 가득 채움


    ```java
    void setKeepAspectOnTabletPortrait(Boolean isKeepAspectOnTabletPortrait)
    ```

    | 매개변수 | 설명 |
    | --- | --- |
    | isKeepAspectOnTabletPortrait | `true`: 화면 비율 유지<br> `false`: 화면 가득 채움 |

    <br>

    > 샘플 코드
    ```java
    ShopLive.setKeepAspectOnTabletPortrait(true);
    ```

    <br>

    > 적용 가이드  
    - [태블릿 세로 모드에서 화면 비율 설정하기](./index.md#step-12-태블릿-세로-모드에서-화면-비율-설정하기)

    <br>

- ### ShopLive.setKeepPlayVideoOnHeadphoneUnplugged
    > 이어폰 (또는 헤드셋) 연결이 끊겼을 때 동영상이 멈춥니다.\
    > 하지만, 필요한 경우 동영상을 계속 재생하도록 설정할 수 있고, 음소거 상태로 처리할 수 있습니다.

    ```java
    void setKeepPlayVideoOnHeadphoneUnplugged(Boolean isKeepPlayVideoOnHeadphoneUnplugged)
    void setKeepPlayVideoOnHeadphoneUnplugged(Boolean isKeepPlayVideoOnHeadphoneUnplugged, Boolean isMute)
    ```

    | 매개변수 | 설명 |
    | --- | --- |
    | isKeepPlayVideoOnHeadphoneUnplugged | `true`: 동영상 계속 재생 <br>  `false`: 동영상 일시 정지 |
    | isMute | `true`: 시스템 볼륨 음소거 처리 <br> `false`: 시스템 볼륨 재생 유지 |

    <br>

    > 샘플 코드
    ```java
    // 이어폰 (또는 헤드셋) 연결이 끊겼을 때 동영상 일시 정지로 설정
    ShopLive.setKeepPlayVideoOnHeadphoneUnplugged(false);

    // 이어폰 (또는 헤드셋) 연결이 끊겼을 때 동영상 계속 재생으로 설정
    ShopLive.setKeepPlayVideoOnHeadphoneUnplugged(true);

    // 이어폰 (또는 헤드셋) 연결이 끊겼을 때 동영상 계속 재생으로 설정하고, 음소거 처리
    ShopLive.setKeepPlayVideoOnHeadphoneUnplugged(false, true));
    ```

    <br>

    > 적용 가이드  
    - [이어폰 (또는 헤드셋) 연결 끊김으로 인한 인터럽트](./index.md#이어폰-또는-헤드셋-연결-끊김으로-인한-인터럽트)

    <br>

- ### ShopLive.setAutoResumeVideoOnCallEnded
    > 통화 종료 후 동영상으로 되돌아 왔을 때 계속 동영상이 재생됩니다.\
    > 하지만, 필요한 경우 동영상을 일시 정지되도록 설정할 수 있습니다.

    `안드로이드 권한 요청이 필요한 API`  
    - 필요 권한  
            `READ_PHONE_STATE`

    <br>

    ```java
    void setAutoResumeVideoOnCallEnded(Boolean isAutoResumeVideoOnCallEnded)
    ```

     | 매개변수 | 설명 |
    | --- | --- |
    | isAutoResumeVideoOnCallEnded | `true`: 동영상 계속 재생<br> `false`: 동영상 일시 정지 |

    <br>

    > 샘플 코드
    ```java
    ShopLive.setAutoResumeVideoOnCallEnded(true);
    ```

    <br>

    > 적용 가이드
    - [통화 연결로 인한 인터럽트](./index.md#통화-연결로-인한-인터럽트)

    <br>

- ### ShopLive.setShareScheme
    > 공유하기를 선택(탭) 했을 때 전달할 url 또는 scheme을 설정합니다.

    ```java
    void setShareScheme(String shareUrl)
    ```

    | 매개변수 | 설명 |
    | --- | --- |
    | shareUrl | 공유할 scheme 또는 URL |

    <br>

    > 샘플 코드
    ```java
    String scheme = "shoplive://live";
    String scheme = "https://shoplive.cloud/live";

    ShopLive.setShareScheme(scheme);
    ```

    > 적용 가이드
    - [방송 링크 공유하기](./index.md#step-8-방송-링크-공유하기)

    <br>

- ### ShopLive.setLoadingProgressColor
    > 동영상 로딩 프로그레스 바 색을 설정합니다.

    ```java
    void setLoadingProgressColor(String hexColor)
    ```

    | 매개변수 | 설명 |
    | --- | --- |
    | hexColor | 프로그레스 바에 설정할 색<br> hex 값으로 입력 |

    <br>

    > 샘플 코드
    ```java
    // 색의 hex 값을 입력하세요. ex) "#FFFFFF"
    ShopLive.setLoadingProgressColor("#FFFFFF");
    ```

    <br>

    > 적용 가이드
    - [프로그레스 바 설정하기](./index.md#step-10-프로그레스-바-설정하기)

    <br>

- ### ShopLive.setLoadingAnimation

    > 동영상 로딩 프로그레스 바를 이미지 애니메이션으로 설정합니다.

    <br>

    ```java
    void setLoadingAnimation(int animationDrawable)
    ```

    <br>

    | 매개변수 | 설명 |
    | --- | --- |
    | animationDrawable | 이미지 배열 리소스 |

    <br>

    > 샘플 코드
    ```java
    // 이미지 애니메이션 설정
    ShopLive.setLoadingAnimation(R.drawable.progress_animation);

    // 이미지 애니메이션 해제
    ShopLive.setLoadingAnimation(0);
    ```

    <br>

    > 적용 가이드
    - [프로그레스 바 설정하기](./index.md#step-10-프로그레스-바-설정하기)
    
    <br>

- ### ShopLive.setChatViewTypeface
    > 채팅 폰트와 채팅 전송 버튼 폰트를 설정합니다.\
    > 채팅 폰트와 채팅 전송 버튼 폰트를 모두 같게 적용하려면 `setChatViewTypeface(typeface)` 함수를 호출합니다.\
    > 채팅 폰트와 채팅 전송 버튼 폰트를 다르게 적용하려면 `setChatViewTypeface(inputBoxTypeface, sendButtonTypeface)` 함수를 호출합니다.

    ```java
    void setChatViewTypeface(Typeface typeface)
    void setChatViewTypeface(Typeface inputBoxTypeface, Typeface sendButtonTypeface)
    ```

    | 매개변수 | 설명 |
    | --- | --- |
    | typeface | 채팅 폰트와 채팅 전송 버튼 폰트를 공통으로 설정할 폰트 |
    | inputBoxTypeface | 채팅 폰트 |
    | sendButtonTypeface | 채팅 전송 버튼 폰트 |
    
    <br>

    > 샘플 코드
    ```java
    Typeface nanumGothic = getResources().getFont(R.font.nanumgothic);
    Typeface nanumGothicBold = getResources().getFont(R.font.nanumgothicbold);
    
    /*
        채팅 폰트 및 채팅 전송 버튼 폰트 공통 설정
        setChatViewTypeface(Typeface typeface) 
    */
    ShopLive.setChatViewTypeface(nanumGothic);

    /*
        채팅 폰트 및 채팅 전송 버튼 폰트 개별 설정
        setChatViewTypeface(Typeface inputBoxTypeface, Typeface sendButtonTypeface)
    */
    ShopLive.setChatViewTypeface(nanumGothic, nanumGothicBold);
    ```

    > 적용 가이드
    - [채팅 폰트 변경하기](./index.md#step-5-채팅-폰트-변경하기)

    <br>

- ### ShopLive.setEnterPipModeOnBackPressed
    > 동영상 시청 중, 뒤로 버튼을 선택(탭) 하여 화면을 빠져 나가면, 동영상이 종료됩니다.\
    > 하지만, 이 옵션을 사용하여 PIP 화면 모드로 전환하여 계속 동영상을 시청하도록 설정할 수 있습니다.
    
    ```java
    void setEnterPipModeOnBackPressed(Boolean isEnterPipModeOnBackPressed)
    ```

    | 매개변수 | 설명 |
    | --- | --- |
    | isEnterPipModeOnBackPressed | `true` PIP 화면 모드로 전환 <br> `false` 동영상 종료 |

    <br>

    > 샘플 코드
    ```java
    ShopLive.setEnterPipModeOnBackPressed(true);
    ```

    <br>

- ### ShopLive.close
    > 시청 중인 방송을 종료합니다.
    
    ```java
    void close()
    ```

    <br>

    > 샘플 코드 
    ```java
    ShopLive.close();
    ```

    <br>

- ### ShopLive.setAutoCloseWhenAppDestroyed

    > 루트 액티비티가 종료되어도 방송이 종료되지 않도록 설정할 수 있습니다.\
    > \
    > 이 옵션을 사용하는 이유는 `Intent.FLAG_ACTIVITY_NEW_TASK` 옵션을 사용하여 루트 액티비티를 다시 실행시킬 때 방송이 종료될 수 있으므로 앱이 종료되는 적절한 시점에 `ShopLive.close` 함수를 직접 호출하여 종료할 수 있도록 하기 위한 것입니다.\
    > 기본값: `자동 종료` \
     설정하지 않으면, 앱이 종료될 때 방송이 함께 종료됩니다.
    

    ```java
    void setAutoCloseWhenAppDestroyed(Boolean isAutoClose)
    ```

    | 매개변수 | 설명 |
    | --- | --- |
    | isAutoClose | `true`: 앱 종료 시 방송 종료 (기본값) <br> `false`: 앱 종료 시 방송 종료되지 않음 |

    <br>

    > 샘플 코드
    ```java
    ShopLive.setAutoCloseWhenAppDestroyed(false);
    ```

    <br>

- ### ShopLive.startPictureInPicture
    > 전체 화면 모드에서 PIP 화면 모드로 전환합니다.
    
    ```java
    void startPictureInPicture()
    ```

    <br>

    > 샘플 코드
    ```java
    ShopLive.startPictureInPicture();
    ```

    <br>

    > 적용 가이드
    - [PIP 화면 모드로 전환하기](./index.md#step-9-pip-화면-모드로-전환하기)

    <br>

- ### ShopLive.stopPictureInPicture
    > PIP 화면 모드를 전체 화면 모드로 전환합니다.

    ```java
    void stopPictureInPicture()
    ```

    <br>

    > 샘플 코드
    ```java
    ShopLive.stopPictureInPicture();
    ```

    <br>

    > 적용 가이드
    - [PIP 화면 모드로 전환하기](./index.md#step-9-pip-화면-모드로-전환하기)

    <br>

- ### ShopLive.getStatus
    > 현재 Shoplive Player 화면 상태입니다. 

    ```java
    Status getStatus()
    ```

    - `enum` **Status** 
        | Status | 설명 |
        | ---------- | --- |
        | None | ShopLive 상태 없음 |
        | PIP | PIP 화면 모드 |
        | Full | 전체 화면 모드 |

    <br>

    > 샘플 코드
    ```java
    Log.d("TAG", "status = " + ShopLive.getStatus());
    ```

    <br>

- ### ShopLive.isSuccessCampaignJoin
    > 방송 진입에 성공했을 때 전달되는 정보입니다.

    <br>

    ```java
    boolean isSuccessCampaignJoin()
    ```

    <br>

    > 샘플 코드
    ```java
    Log.d("TAG", "isSuccessCampaignJoin = " + ShopLive.isSuccessCampaignJoin());
    ```

    <br>
---
<br>

## Handler
> Shoplive Player에서 발생한 알림을 클라이언트에서 Handler 함수를 통해 전달받고 필요한 처리를 합니다.

- ### ShopLive.setHandler
    > ShopLive event handler입니다.

    ```java
    void setHandler(@NonNull ShopLiveHandler handler)
    ```

    <br>

    > 샘플 코드
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

    > 적용 가이드
    - [Handler 등록하기](./index.md#handler-등록하기)

    <br>

- ### ShopLiveHandler.handleNavigation
    > Shoplive에서 상품, 배너 등을 선택(탭) 했을 때, 선택한 상품 또는 배너 정보를 전달합니다.

    ```java
    void handleNavigation(Context context, @NonNull String url) {}
    ``` 

    | 매개변수 | 설명 |
    | --- | --- |
    | url | 상품 또는 배너 선택 시 이동할 URL |

    <br>

    > 샘플 코드
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

    > 적용 가이드
    - [Handler 등록하기](./index.md#handler-등록하기)

    <br>

- ### ShopLiveHandler.handleDownloadCoupon
    > Shoplive에서 쿠폰을 선택(탭) 했을 때 클라이언트로 쿠폰 정보를 전달합니다. 클라이언트의 쿠폰 처리 결과를 Shoplive Android SDK로 다시 전달하는 `callback`을 통해 Shoplive Player에서의 쿠폰 상태를 설정합니다.
    
    ```java
    void handleDownloadCoupon(@NonNull Context context, @NonNull String couponId, @NonNull ShopLiveHandlerCallback callback) {}
    ```

    | 매개변수 | 설명 |
    | --- | --- |
    | context | Context |
    | couponId | 선택(탭) 한 쿠폰 ID |
    | callback | 쿠폰 처리 완료 시 ShopLive로 알려줄 `callback` |

    <br>

    - #### `callback` ShopLiveHandlerCallback.couponResult
        > 쿠폰 다운로드를 완료하였을 때, 결과의 성공 또는 실패 여부, 메시지 등을 전달하기 위해 호출합니다.

        ```java
        /**
        * @param isDownloadSuccess - true: 성공, false: 실패
        * @param message - 성공 또는 실패 메시지
        * @param couponStatus - SHOW: 쿠폰 재활성, HIDE: 쿠폰 숨김, KEEP: 상태 유지
        * @param alertType - ALERT: 팝업, TOAST: 메시지
        * */
        default void couponResult(
            @NonNull Boolean isDownloadSuccess, 
            @Nullable String message, 
            @NonNull ShopLive.CouponPopupStatus couponStatus, 
            @Nullable ShopLive.CouponPopupResultAlertType alertType
        ) {}
        ```

        <br>

    > 샘플 코드
    ```java
    private ShopLiveHandler handler = new ShopLiveHandler() {
        ...
        @Override
        public void handleDownloadCoupon(@NonNull Context context, @NonNull String couponId, @NonNull ShopLiveHandlerCallback callback) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("쿠폰 다운로드");
            builder.setMessage("couponId " + couponId);
            builder.setPositiveButton("성공", (dialog, which) -> {

                // 성공했을 때 callback 설정
                callback.couponResult(
                        true,
                        "쿠폰을 다운로드했습니다.",
                        ShopLive.CouponPopupStatus.HIDE,
                        ShopLive.CouponPopupResultAlertType.TOAST
                );
            });

            builder.setNegativeButton("실패", (dialog, which) -> {

                // 실패했을 때 callback 설정
                callback.couponResult(
                        false,
                        "쿠폰을 다운로드하지 못했습니다.",
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

    > 적용 가이드
    - [쿠폰 사용하기](./index.md#Step-6-쿠폰-사용하기)

    <br>

- ### ShopLiveHandler.handleCustomAction
    > 팝업에서 선택 이벤트를 `custom`으로 지정하고, 팝업을 선택(탭) 했을 때 팝업 정보를 전달합니다. 팝업의 `id`, `type`, `payload`를 전달합니다.
    
    ```java
    void handleCustomAction(@NonNull Context context, @NonNull String id, @NonNull String type, @NonNull String payload, @NonNull ShopLiveHandlerCallback callback) {}
    ```

    | 매개변수 | 설명 |
    | --- | --- |
    | context | Context |
    | id | 쿠폰 또는 배너 ID |
    | type | 쿠폰 또는 배너 type |
    | payload | 사용자가 정의한 payload |
    | callback | 커스텀 처리를 완료했을 때, ShopLive로 알려줄 callback |
    
    <br>

    - #### `callback` ShopLiveHandlerCallback.customActionResult
        > `customAction` 처리가 완료 되었을 때, 성공 또는 실패 결과 여부, 메시지 등을 전달하기 위해 호출합니다.

        <br>

        ```java
        /**
        * @param isSuccess - true: 성공, false: 실패
        * @param message - 성공 또는 실패 메시지
        * @param couponStatus - SHOW: 재활성, HIDE: 숨김, KEEP: 상태 유지
        * @param alertType - ALERT: 팝업, TOAST: 메시지
        * */
        default void customActionResult(
            @NonNull Boolean isSuccess, 
            @Nullable String message, 
            @NonNull ShopLive.CouponPopupStatus couponStatus, 
            @Nullable ShopLive.CouponPopupResultAlertType alertType
        ) {}
        ```

        <br>

    > 샘플 코드
    ```java
    private ShopLiveHandler handler = new ShopLiveHandler() {
        ...
        @Override
        public void handleCustomAction(@NonNull Context context, @NonNull String id, @NonNull String type, @NonNull String payload, @NonNull ShopLiveHandlerCallback callback) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("커스텀 액션(팝업)");
            builder.setMessage("id: " + id + " type: " + type + " payload: " + payload);
            builder.setPositiveButton("성공", (dialog, which) -> {

                // 성공했을 때 콜백 설정
                callback.couponResult(
                        true,
                        "Custom action(Popup) 처리가 성공하였습니다.",
                        ShopLive.CouponPopupStatus.HIDE,
                        ShopLive.CouponPopupResultAlertType.TOAST
                );
            });

            builder.setNegativeButton("실패", (dialog, which) -> {

                // 실패했을 때 콜백 설정
                callback.couponResult(
                        false,
                        "Custom action(Popup) 처리가 실패하였습니다.",
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

    > 적용 가이드
    - [쿠폰 사용하기](./index.md#step-6-쿠폰-사용하기)

    <br>

- ### ShopLiveHandler.handleShare
    > 방송 중 공유하기를 선택(탭) 했을 때 Handler입니다. \
    > 안드로이드 Share Sheet를 사용하지 않고 직접 구현하려면 반드시 `override`해야 합니다.

    

    ```java
    default void handleShare(Context context, String shareUrl) {}
    ```

    | 매개변수 | 설명 |
    | --- | --- |
    | context | Context |
    | shareUrl | 공유할 URL 또는 scheme  |    

    <br>

    > 샘플 코드
    ```java
    private ShopLiveHandler handler = new ShopLiveHandler() {
        ...
        @Override
        public void handleShare(Context context, String shareUrl) {
            // 커스텀 공유 팝업 다이얼로그
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

    > 적용 가이드
    - [방송 링크 공유하기](./index.md#step-8-방송-링크-공유하기)


    <br>

- ### ShopLiveHandler.onChangeCampaignStatus
    > 방송 상태가 변경되었을 때 Handler입니다.

    ```java
    /**
    * @param context - Context
    * @param campaignStatus - READY | ONAIR | CLOSED
    */
    void onChangeCampaignStatus(@NonNull Context context, @NonNull String campaignStatus) {}
    ```

    | 매개변수 | 설명 |
    | --- | --- |
    | context | Context |
    | campaignStatus | campaignStatus<br> `READY`, `ONAIR`, `CLOSED` |

    <br>

    > 샘플 코드
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
    > JSON Object 형태의 방송 정보입니다.

    

    ```java
    void onCampaignInfo(@NonNull JSONObject campaignInfo) {}
    ```

    

    | 매개변수 | 설명 |
    | --- | --- |
    | campaignInfo | campaignInfo<br> 예) `{'title':'방송 제목'}` |

    <br>

    > 샘플 코드
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
    > Shoplive Player 상태를 전달 받습니다.


    ```java
    /**
    * @param isPipMode - true:PIP, false:FULL
    * @param state - "CREATED" or "DESTROYED"
    */
    default void onChangedPlayerStatus(@NonNull Boolean isPipMode, @NonNull String state) {}
    ```

    | 매개변수 | 설명 |
    | --- | --- |
    | isPipMode | 현재 PIP 화면 모드<br> 예) `{'title':'방송 제목'}` |
    | state | Shoplive Player 상태<br> `CREATED`, `DESTROYED` |

    <br>

    > 샘플 코드
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
    > 사용자 이름이 변경되었을 때 호출됩니다.

    ```java
    default void onSetUserName(JSONObject jsonObject) {}
    ```

    | 매개변수 | 설명 |
    | --- | --- |
    | jsonObject | 사용자 정보<br> 예) `{'userId':'123', 'userName':'test'}` |

    <br>

    > 샘플 코드
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
    > ShopLive 미리보기 화면을 선택(탭) 했을 때 Handler입니다.
    > 
    > 미리보기 화면을 선택(탭) 하면 해당 방송으로 진입합니다. (기본값)  
    > 방송에 진입하지 않고, 직접 구현하려면 반드시 `override` 해야 합니다.  
    > 단, `다른 앱 위에 표시`로 제공 되는 미리보기 화면에서만 이벤트가 전달됩니다.   
    > `OS PIP`에서는 미리보기 이벤트가 전달되지 않고, 자동으로 전체 화면 방송으로 진입합니다.
    
    ```java
    default void handlePreview(@NonNull Context context, @NonNull String campaignKey) {}
    ```

    | 매개변수 | 설명 |
    | --- | --- |
    | context | Context |
    | campaignKey | 캠페인(방송) 키    |          

    <br>

    > 샘플 코드
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

    > 적용 가이드
    - [방송 미리보기](./index.md#step-3-방송-미리보기)

    <br>

- ### ShopLiveHandler.onReceivedCommand
    > `command` 명령어를 받았을 때 호출됩니다.

    ```java
    default void onReceivedCommand(@NonNull Context context, @NonNull String command, @NonNull JSONObject data) {}
    ```


    | 매개변수 | 설명 |
    | --- | --- |
    | context | Context |
    | command | 전달된 명령어 |
    | data | 전달된 데이터 |

    <br>

    - #### ReceivedCommand 목록
        - ##### CLICK_PRODUCT_DETAIL
            > 사용자가 방송 화면에서 상품 목록을 열고, 상품을 선택하면 전달되는 상품 정보입니다.
                
            ```javascript
            command:
            CLICK_PRODUCT_DETAIL

            payload:
            {
                action = 링크;
                brand = 브랜드;
                sku = product01234;
                url = "http://product.com";
            }
            ```
        <br>

        - ##### CLICK_PRODUCT_CART
            > 사용자가 방송 화면에서 상품 목록을 열고, 장바구니 버튼을 선택하면 전달되는 장바구니 정보입니다.
            
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

        - ##### LOGIN_REQUIRED
            > Shoplive Android SDK가 적용되는 앱에 로그인이 필요할 때 전달되는 로그인 동작을 요청합니다.
            
            ```javascript
            command:
            LOGIN_REQUIRED

            payload: 없음
            ```
            <br>

        - ##### ON_SUCCESS_CAMPAIGN_JOIN
            > 방송 진입에 성공했을 때 전달되는 정보입니다.

            ```javascript
            command:
            ON_SUCCESS_CAMPAIGN_JOIN

            payload:
            {
            isGuest: 1 // 1: 미인증 사용자, 0: 인증 사용자
            }
            ```

            <br>

          > 샘플 코드

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
    <br>

- ### ShopLive.setNextActionOnHandleNavigation
    > 사용자가 상품, 공지사항, 배너 등 링크를 선택하였을 때, Shoplive Player의 다음 동작을 설정합니다.

    ```java
    void setNextActionOnHandleNavigation(ActionType type)
    ```
    
    - `enum` **ActionType** 

    | 매개변수 | 설명 |
    | --- | --- |
    | PIP | PIP 전환 |
    | KEEP | 상태 유지 |
    | CLOSE | 종료 |

    <br>

    > 샘플 코드
    ```kotlin
    ShopLive.setNextActionOnHandleNavigation(ActionType.PIP)
    ```

    > 적용 가이드
    - [사용자 링크 선택 시 Shoplive Player의 다음 동작 설정하기](./index.md#step-11-사용자-링크-선택-시-shoplive-player의-다음-동작-설정하기)

    <br>

- ### ShopLive.showDialogFragment
    > Shoplive Player 화면에 사용자의 `DialogFragment`를 표시하기 위해 사용합니다.

    ```java
    void showDialogFragment(DialogFragment dialogFragment)
    ```
    
    | 매개변수 | 설명 |
    | --- | --- |
    | dialogFragment | 사용자의 DialogFragment |

    <br>

    > 샘플 코드
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

    > 적용 가이드
    - [사용자 링크 선택 시 Shoplive Player의 다음 동작 설정하기](./index.md#step-11-사용자-링크-선택-시-shoplive-player의-다음-동작-설정하기)
    
    <br>

- ### ShopLive.setEndpoint
    > Shoplive Player의 Web Landing 페이지가 아닌 특정 Landing 페이지 또는 URL에서 Shoplive 서비스를 사용해야 할 때 사용합니다.  
    > 예를 들어, 보안으로 인해 Shoplive Landing 페이지를 사용할 수 없는 경우, 특정 도메인으로 Landing  페이지를 생성할 수 있습니다. <br>
    > ※ Shoplive Player의 Web Landing 페이지는 Shoplive와 협의 후 적용할 수 있습니다.
        
    ```java
    void setEndpoint(String url)
    ```
    
    | 매개변수 | 설명 |
    | --- | --- |
    | url | Shoplive Player의 Web Landing url |

    <br>

    > 샘플 코드
    ```kotlin
    ShopLive.setEndpoint("https://www.your_landingpage.com/")
    ```
    
    <br>

- ### ShopLiveHandler.onError
    > 방송 전 또는 방송 중 발생하는 오류 상황에 관한 메시지입니다.


    ```java
    void onError(@NonNull Context context, @NonNull String code, @NonNull String message) {}
    ```

    | 매개변수 | 설명 |
    | --- | --- |
    | context | Context |
    | code | 오류 코드 |
    | message | 오류 메시지 |

    <br>

    > 샘플 코드
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
