# Shoplive Android SDK guide 
<br>

- [Shoplive Android SDK](#1-shoplive-android-sdk)
  - [소개](#소개)
  - [특징](#특징)
  - [요구사항](#요구사항)
  - [변경사항](#변경사항)
- [시작하기 전에](#2-시작하기-전에)
- [시작하기](#3-시작하기)
  - [`Step 1` 설치하기](#step-1-설치하기)
  - [`Step 2` Shoplive Player 실행하기](#step-2-shoplive-player-실행하기)
  - [`Step 3` Shoplive 채팅 사용하기](#step-3-shoplive-채팅-사용하기)
  - [`Step 4` 쿠폰 사용하기](#step-4-쿠폰-사용하기)
  - [`Step 5` 상품 관리하기](#step-5-상품-관리하기)
  - [`Step 6` 방송 링크 공유하기](#step-6-방송-링크-공유하기)
  - [`Step 7` PIP 화면 모드로 전환하기](#step-7-pip-화면-모드로-전환하기)
  - [`Step 8` 프로그레스 바 설정하기](#step-8-프로그레스-바-설정하기)
  - [`Step 9` Shoplive Player 재생 중 인터럽트 옵션 설정하기](#step-9-shoplive-player-재생-중-인터럽트-옵션-설정하기)
  - [`Step 10` 태블릿 세로 모드에서 화면 비율 설정하기](#step-10-태블릿-세로-모드에서-화면-비율-설정하기)
  - [`Step 11` 사용자 링크 선택 시 Shoplive Player의 다음 동작 설정하기](#step-11-사용자-링크-선택-시-shoplive-player의-다음-동작-설정하기)

- 참고 - [API 문서](https://github.com/shoplive/android-sdk-sample/blob/main/doc/kr/api-document.md)

<br>

<br>


# 1. Shoplive Android SDK

<br>

## 소개
Shoplive Android SDK는 간단한 `Java` 또는 `Kotlin` 코드만 삽입하여 여러분의 앱을 사용하는 고객에게 쉽고 빠르게 라이브 방송을 제공할 수 있는 모바일 SDK입니다. Shoplive PIP(Picture-in-Picture), Native Keyboard UI 등 스마트폰에서 원활한 모바일 경험을 가능하게 합니다.

<image src="../images/guide.gif" width="20%" height="20%"></image>

<br>

## 특징

+ Shoplive Player를 사용하여 Shoplive 방송을 재생할 수 있습니다.
+ 저화질/무음으로 설정한 Shoplive 방송을 PIP 화면 모드로 나타내어 사용자가 Shoplive 방송을 시청하도록 유도할 수 있습니다.
+ 사용자가 Shoplive 방송을 시청하면서 채팅에 참여하고 상품을 검색하도록 할 수 있습니다.
+ 미인증 사용자 모드로 채팅에 참여하고, 채팅에 참여한 사용자 정보를 실시간 전달받을 수 있습니다.
+ 인증 사용자로 채팅에 참여하고, 간편 인증/보안 인증으로 사용자 계정을 통합할 수 있습니다.
+ 채팅 대화명 및 채팅 폰트를 변경할 수 있습니다.
+ 일반 쿠폰 및 커스텀 쿠폰(팝업)을 사용할 수 있습니다.
+ 여러 기능을 사용하여 상품을 관리할 수 있습니다.
+ 기본 시스템 공유 UI를 사용하거나 커스텀 시스템 공유 UI를 사용할 수 있습니다.
+ 재생 중인 방송 링크를 공유할 수 있습니다.
+ Shoplive Player 재생 중 인터럽트 옵션을 설정할 수 있습니다.
+ 태블릿 세로 모드에서 영상 비율을 설정할 수 있습니다.
+ 재생 중인 방송에 관한 각종 정보를 실시간 전달받을 수 있습니다.
+ Shoplive Android SDK에 관한 각종 정보를 실시간 전달받을 수 있습니다.

<br>

## 요구사항

Shoplive Android SDK를 사용하기 위한 최소 요구 사양입니다. 이 요구사항을 충족하지 않으면 Shoplive Android SDK를 사용할 수 없습니다.

+ `Android 4.4` 이상
+ `targetSdkVersion 30` 이상

<br>

## 변경사항

+ v1.2.2
  * 진동, 효과음 등 퀴즈 기능을 연계할 수 있도록 개선하였습니다.
  * 미리보기 때 무음 재생하도록 개선하였습니다.
  * 상품 또는 공지사항을 사용자가 선택하였을 때 앱 내 PIP로 강제 전환되던 기능에 다음 동작을 선택할 수 있는 API를 추가하였습니다.
  * Endpoint를 설정할 수 있는 API를 추가하였습니다.
+ [이전 버전 업데이트](change-log.md)

<br>

# 2. 시작하기 전에

+ Shoplive Android SDK를 사용하려면 Shoplive 담당자에게 관리자 계정과 비밀번호를 요청하세요. 
  + [[문의하기](mailto:ask@Shoplive.cloud)]
+ Shoplive 어드민에서 방송을 추가하고, 방송 키(Campaign Key)를 기록하세요.
  + [[Shoplive 어드민 계정 발급받기 가이드](https://docs.shoplive.kr/docs)]

<br>

# 3. 시작하기

Shoplive Android SDK를 설치하여, 간단히 Shoplive Player에 적용하세요.

<br>

## `Step 1` 설치하기

1. `project/build.gradle`에 아래 코드를 추가하세요.

    ```gradle
    allprojects {
        repositories
            ...
            maven { url 'https://shoplivesdk.jfrog.io/artifactory/shoplive-sdk/' }
            ...
        }
    }
    ```

<br>

2. `app/build.gradle`에 아래 코드를 추가하세요.

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

<br>

3. `AndroidManifest.xml`에 아래 Application을 등록하세요.

    ```xml
    <application
            android:name=".YourApplication"
           .
           .>
    </application>
    ```

<br>

4. `YourApplication.kt`에 아래 코드를 추가하세요.

    ```kotlin
    class YourApplication: Application() {

        override fun onCreate() {
            super.onCreate()

            ShopLive.init(this)
        }
    }
    ```

<br>

+ API Reference
    > [ShopLive.setAccessKey](./api-document.md#shopLivesetaccesskey)

<br>

### Handler 등록하기
> Shoplive Android SDK로부터 여러 이벤트를 수신할 Handler를 등록하세요.

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

## `Step 2` Shoplive Player 실행하기

+ Shoplive 담당자에게 받은 액세스 키(Access Key)를 사용하여, Shoplive Android SDK를 초기화하세요.
    ```kotlin
    ShopLive.setAccessKey("{accessKey}")
    ```
    * API Reference
        > [ShopLive.setAccessKey](./api-document.md#shoplivesetaccesskey)

<br>

### 방송 재생하기

+ 캠페인 키(Campaign) Key를 사용하여, 방송을 재생하세요.
    ```kotlin
    ShopLive.play("{campaignKey}")
    ```
    * API Reference
        > [ShopLive.play](./api-document.md#shopliveplay)
        
<br>

+ 잘 실행되었다면 아래와 같은 화면을 볼 수 있습니다.

    <image src="../images/run.png" width="20%" height="20%"></image>

<br>

### 방송 미리보기
> 방송에 진입하지 않아도 앱 내에 미리보기 화면을 띄울 수 있습니다.\
> 이 기능을 사용하려면 `android.permission.SYSTEM_ALERT_WINDOW` 권한이 필요합니다. 

<image src="../images/preview.png" width="20%" height="20%"/> 

```kotlin
ShopLive.showPreview({campaignKey})
```

+ 미리보기 화면을 선택(탭)하면 본 방송으로 진입합니다. 본 방송으로 진입하지 않고 다른 처리를 하려면 `handlePreview` 함수를 `override`하여 직접 구현하세요.
    ```kotlin
    override fun handlePreview(context: Context, campaignKey: String) {
        Log.d(TAG, "campaignKey=$campaignKey")
    }
    ```

+ API Reference
    > [ShopLive.showPreview](./api-document.md#shopliveshowpreview) \
    > [ShopLive.hidePreview](./api-document.md#shoplivehidepreview) \
    > [ShopLive.resumePreview](./api-document.md#shopliveresumepreview) 

<br>

## `Step 3` Shoplive 채팅 사용하기

<br>

<image src="../images/chat.png" width="20%" height="20%"></image>

+ 채팅 사용을 위한 준비하기 
    * Shoplive 어드민에서 채팅에 접속할 수 있는 사용자 접근 권한을 설정할 수 있습니다.<br>
    [[Shoplive 어드민 사용 가이드 - 방송 준비하기 - 대화 설정](https://docs.shoplive.kr/docs/preps-for-campaign#대화-설정)]

        <image src="../images/admin_campaign_chat_settings.png" width="50%" height="50%"></image>

<br>

+ 미인증 사용자로 채팅하기
    * 별도 인증 없이 채팅에 연결합니다.
    * 미인증 사용자 채팅을 허용하지 않으면 채팅 버튼을 선택하더라도, 응답이 발생하지 않습니다.
    * 미인증 사용자 대화명을 필수로 설정하지 않으면, 미인증 사용자 대화명은 무작위로 지정됩니다.
  
        <image src="../images/guest_auto_nickname.png" width="20%" height="20%"></image>
    * 미인증 사용자 대화명을 필수로 설정하면, 사용자가 대화명을 입력해야 합니다.
  
        <image src="../images/chat_nickname.png" width="20%" height="20%"></image>

<br>

+ 인증된 사용자로 채팅하기
    * #### 간편 인증으로 연동하기
        - 사용자 ID와 로그인 사용자 대화명을 설정하는 기능을 통해 사용자 계정을 통합할 수 있습니다.
        - 사용자 ID, 이름, 나이, 성별, 사용자 점수 및 기타 사용자 정의 데이터 정보를 설정할 수 있습니다.
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
            <br>

        - API Reference
            > [ShopLive.setUser](./api-document.md#shoplivesetuser) 
        
        <br>

    * #### 보안 인증으로 연동하기
        - Shoplive 어드민에서 발급한 비밀 키(Secret Key)로 부여된 보안 인증토큰(JWT)을 사용하여, 사용자 ID, 이름 등 정보를 Shoplive Android SDK에서 설정할 수 있습니다.

        <br>
        

        <B>[Shoplive 담당자에게 보안 인증토큰(JWT) 발급 요청]</B> 

         - 클라이언트 서버에서 보안 인증토큰(JWT)을 생성하고, Shoplive Android SDK API를 통해 앱 클라이언트에 제공해야 합니다.)
         - Shoplive 어드민에서 발급한 비밀 키(Secret Key)를 사용하여 사용자를 인증할 수 있습니다. 

         <br>

         [[인증 토큰 생성 가이드](https://docs.ShopLive.kr/docs/use-jwt-auth)]


         ```kotlin
            ShopLive.setAuthToken("{jwt}")
         ```
        - API Reference
            > [ShopLive.setAuthToken](./api-document.md#shoplivesetauthtoken) 

        <br>

+ 대화명 변경하기
    * 사용자가 Shoplive Player에서 대화명을 변경하면, Shoplive Android SDK는 `onSetUserName` 함수를 사용하여 해당 변경 사항을 클라이언트에 전달합니다.


        ```kotlin
        override fun onSetUserName(jsonObject: JSONObject) {
            super.onSetUserName(jsonObject)
            Log.d(TAG, "userId=${jsonObject.get("userId")}, userName=${jsonObject.get("userName")}")
        }
        ```

<br>

+ 라이브 스트리밍을 하는 동안, Shoplive 어드민에서 채팅을 관리할 수 있습니다.<br>
  + [[Shoplive 어드민 사용 가이드 - 방송 준비하기 - 대화 설정](https://docs.shoplive.kr/docs/preps-for-campaign#대화-설정)]
  
<br>

### 채팅 폰트 변경하기
+ Shoplive Player 앱의 기본 폰트를 변경할 수 있습니다.
+ 기본값: 시스템 폰트

    <image src="../images/default-font.png" width="50%" height="50%"></image>

<br>

+ 폰트 변경
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
    > [ShopLive.setChatViewTypeface](./api-document.md#shoplivesetchatviewtypeface) 

<br>

## `Step 4` 쿠폰 사용하기

+ 일반 쿠폰과 커스텀 쿠폰(팝업)을 사용할 수 있습니다.

    <image src="../images/coupon.png" width="20%" height="20%"></image>

* 일반 쿠폰
    
    +  사용자는 방송 화면에서 관리자가 등록한 일반 쿠폰을 선택하여 쿠폰에 설정된 혜택(예: 할인 등)을 신청할 수 있습니다. 쿠폰 정보는 `handleDownloadCoupon` 함수로 전달됩니다.
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
    + 사용자에게 쿠폰 발행 후, `callback`을 통해 쿠폰 발행 결과를 전달할 수 있습니다.


<br>

* 커스텀 쿠폰(팝업)

    + 사용자는 방송 화면에서 관리자가 등록한 커스텀 쿠폰(팝업)을 선택하여 쿠폰에 설정된 혜택((예: 할인 등)을 신청할 수 있습니다. 사용자 정의 쿠폰 정보는 `handleCustomAction` 함수로 전달됩니다. 

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
    + 사용자에게 쿠폰 발행 후, `callback`을 통해 쿠폰 발행 결과를 전달할 수 있습니다.

<br>

## `Step 5` 상품 관리하기

> Shoplive Player 화면에서 상품을 선택하거나 장바구니 등을 선택하였을 때 발생하는 이벤트와 선택한 정보를 Shoplive Android SDK가 클라이언트로 전달합니다.

<image src="../images/product.png" width="20%" height="20%"></image>

* CLICK_PRODUCT_DETAIL
    + Shoplive Player 화면의 상품 목록에서 상품을 선택하면, Shoplive Android SDK는 `handleReceivedCommand` 함수를 사용하여 선택한 상품 정보를 클라이언트에 전달합니다.

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

* CLICK_PRODUCT_CART
    + Shoplive Player 화면의 상품 목록에서 장바구니 버튼을 클릭하면, Shoplive Android SDK는 `handleReceivedCommand` 함수를 사용하여 장바구니 정보를 클라이언트에 전달합니다.

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
    + 재생 중인 방송 화면에서 상품, 배너 등을 선택했을 때, Shoplive Android SDK는 `handleNavigation` 함수를 사용하여 선택 항목의 URL 정보를 클라이언트에 전달합니다.

        ```kotlin
        override fun handleNavigation(context: Context, url: String) {
            Log.d(TAG, "url=$url")
        }
        ```

<br>

## `Step 6` 방송 링크 공유하기
+ Shoplive의 공유하기 API를 사용하여 방송의 `scheme` 또는 `URL`을 손쉽게 공유할 수 있습니다.

+ scheme 설정
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

+ URL 설정
    ```kotlin
    val url = "https://www.share_link.com/?ak=xxx&ck=yyy"
    ShopLive.setShareScheme(url)
    ```

+ 재생 중인 방송 링크를 주변 또는 SNS에 공유하여, 공유된 링크 클릭만으로 앱 설치 또는 방송 시청을 유도할 수 있습니다. (단, 앱에서 별도 deeplink를 처리해야 합니다.)

     <image src="../images/share.png" width="20%" height="20%"></image>
     <image src="../images/share.gif" width="24%" height="24%"></image>

+ 커스텀 시트템 공유 UI 사용하기
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
    > [ShopLive.setShareScheme](./api-document.md#shoplivesetsharescheme)

<br>

## `Step 7` PIP 화면 모드로 전환하기
+ 방송을 시청하다가 다른 작업을 수행할 때, PIP 화면 모드로 전환할 수 있습니다.

    <image src="../images/pip.gif" width="20%" height="20%"></image>

    ```kotlin
    // PIP 화면 모드로 전환하기
    ShopLive.startPictureInPicture()

    // 전체 화면 모드으로 전환하기 (PIP 화면 모드 종료)
    ShopLive.stopPictureInPicture()
    ```
<br>

+ PIP 화면 모드의 화면 비율 설정하기 
    > PIP 화면 모드의 화면 비율을 설정하지 않으면 9:16 비율(기본값)으로 적용됩니다.

    | 1:1 | 1:2 | 2:3 | 3:4 | 9:16 <br> (기본값) |
    | ---------- | --- | --- | --- | --- |
    | <image src="../images/pip_1_1.png"/> | <image src="../images/pip_1_2.png" />|<image src="../images/pip_2_3.png" />|<image src="../images/pip_3_4.png" />|<image src="../images/pip_9_16.png" />|


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
    > [ShopLive.startPictureInPicture](./api-document.md#shoplivestartpictureinpicture) <br>
    > [ShopLive.stopPictureInPicture](./api-document.md#shoplivestoppictureinpicture) <br>
    > [ShopLive.setPIPRatio](./api-document.md#shoplivesetpipratio)

<br>
<br>

## `Step 8` 프로그레스 바 설정하기
+ 방송에 진입하여 영상 로딩 중 보이는 프로그레스 바 스타일을 변경할 수 있습니다.

+ 프로그레스 바 색 변경 (기본값: 흰색)


    ```kotlin
    ShopLive.setLoadingProgressColor("#FF0000")
    ```

    <image src="../images/progress.gif" width="20%" height="20%"></image>
    <image src="../images/progress_color.png" width="50%" height="50%"></image>

<br>

+ 이미지 애니메이션 프로그레스 바 설정하기
    * res/drawable/progress_animation.xml
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
    > [ShopLive.setLoadingProgressColor](./api-document.md#shoplivesetloadingprogresscolor) <br>
    > [ShopLive.setLoadingAnimation](./api-document.md#shoplivesetloadinganimation)

<br>

## `Step 9` Shoplive Player 재생 중 인터럽트 옵션 설정하기
> 재생 중 통화 또는 이어폰 연결 유실 등으로 인한 인터럽트 이벤트 발생 시, 인터럽트 이벤트가 끝난 후 Shoplive Player 재생 동작을 설정할 수 있습니다.

<br>

+ #### 이어폰 (또는 헤드셋) 연결 끊김으로 인한 인터럽트
    > 이어폰 (또는 헤드셋) 연결이 끊겼을 때 다시 연결되면 자동으로 방송이 재생되도록 설정할 수 있습니다. \
    (이렇게 설정하지 않으면,  이어폰 (또는 헤드셋) 연결이 끊겼을 때 영상이 정지됩니다.)

    ```kotlin
    // 자동 재생
    ShopLive.setKeepPlayVideoOnHeadphoneUnplugged(true)

    // 자동 재생 + 음소거
    ShopLive.setKeepPlayVideoOnHeadphoneUnplugged(true, true) 
    ```

<br>

+ #### 통화 연결로 인한 인터럽트
    > 통화 종료 후 방송으로 돌아왔을 때, 방송이 일시 정지 되도록 설정할 수 있습니다. \
    이 기능을 사용하려면 `android.permission.READ_PHONE_STATE` 권한이 필요합니다. \
    (이렇게 설정하지 않으면, 통화 종료 후 영상으로 돌아왔을 때 영상이 자동 재생됩니다.)

    ```kotlin
    // 통화 연결 시, 방송 일시 정지
    ShopLive.setAutoResumeVideoOnCallEnded(false)
    ```

<br>

+ API Reference
    > [ShopLive.setKeepPlayVideoOnHeadphoneUnplugged](./api-document.md#shoplivesetkeepplayvideoonheadphoneunplugged) <br>
    > [ShopLive.setAutoResumeVideoOnCallEnded](./api-document.md#shoplivesetautoresumevideooncallended)

<br>

## `Step 10` 태블릿 세로 모드에서 화면 비율 설정하기

> 태블릿 세로 모드에서 Shoplive 화면 비율을 유지하도록 할 수 있습니다. \
(이렇게 설정하지 않으면, 태블릿 세로 모드에서 화면을 가득 채우도록 설정됩니다.)

| 화면 채움  | 비율 유지  |
| ---------- | --- |
| <image src="../images/tablet_aspect_false.jpg"/> | <image src="../images/tablet_aspect_true.jpg" />|

```kotlin
// 화면 비율 유지하여 재생
ShopLive.setKeepAspectOnTabletPortrait(true)

```

<br>

+ API Reference
    > [ShopLive.setKeepAspectOnTabletPortrait](./api-document.md#shoplivesetkeepaspectontabletportrait)

<br>



## `Step 11` 사용자 링크 선택 시 Shoplive Player의 다음 동작 설정하기

> 사용자가 상품, 공지사항, 배너 등 링크를 선택하였을 때, Shoplive Player의 다음 동작을 설정할 수 있습니다.

<br>

+ PIP 전환하기

    <image src="../images/nextAction_pip.gif" width="20%"/>

    ```kotlin
    ShopLive.setNextActionOnHandleNavigation(ActionType.PIP)
    ```

+ 상태 유지하기

    <image src="../images/nextAction_keep.gif" width="20%"/>

    ```kotlin
    ShopLive.setNextActionOnHandleNavigation(ActionType.KEEP)
    ```

+ 종료하기 

    <image src="../images/nextAction_close.gif" width="20%"/>

    ```kotlin
    ShopLive.setNextActionOnHandleNavigation(ActionType.CLOSE)
    ```
<br>

+ API Reference
    > [ShopLive.setNextActionOnHandleNavigation](./api-document.md#shoplivesetnextactiononhandlenavigation)

<br>


<br>

