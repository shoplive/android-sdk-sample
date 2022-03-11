# Change Log

- v1.2.2 (2022-03-10)
  + 진동, 효과음 등 퀴즈 기능을 연계할 수 있도록 개선하였습니다.
  + 미리보기 때 무음 재생하도록 개선하였습니다.
  + 상품 또는 공지사항을 사용자가 선택하였을 때 앱 내 PIP로 강제 전환되던 기능에 다음 동작을 선택할 수 있는 API를 추가하였습니다.
  + Endpoint를 설정할 수 있는 API를 추가하였습니다.
  
<br>

- v1.2.1 (2022-01-13)
  + 라이브 방송 진입시 네트워크 환경에 따라 간헐적으로 영상 재생이 원활하지 않는 이슈 해결

<br>

- v1.2.0 (2021-12-15)
  + ShopLive.getAuthToken() - JWT 인증키 가져오기
  + ShopLive.isSuccessCampaignJoin() - 방송에 성공적으로 진입했는지 확인
  + ShopLive.startPictureInPicture() - PIP 모드로 진입하기
  + ShopLive.stopPictureInPicture() - PIP 모드 해제(FULL 화면으로 복귀)

<br>

- v1.1.2 (2021-11-15)
  + SDK의 web으로부터 수신된 command 정보를 전달하기 위한 onReceivedCommand 핸들러 함수 추가

<br>

- v1.1.1 (2021-11-08)
  + coupon download와 custom action의 성공 또는 실패에 대한 콜백 추가
  + 사용자 이름 설정 또는 변경시 알림 추가

<br>

- v1.0.15 (2021-11-03)
  + 이미지 애니메이션 프로그레스 인터페이스 추가
  + 루트 액티비티가 종료 되어도 방송이 자동으로 종료되지 않도록 옵션 추가

<br>

- v1.0.13 (2021-10-14)
  + 프리뷰(다른 앱 위 표시 옵션 사용시) 재생중에 앱이 background/foreground 전환 시, 자동으로 hide/show 되도록 적용
  + 프리뷰(다른 앱 위 표시 옵션 사용시) 재생중에 잠시 숨길 수 있도록 ShopLive.hidePreview() 인터페이스 추가
  + 프리뷰가 재생도중 잠시 숨겨졌을 때, 다시 재개할 수 있도록 ShopLive.resumePreview() 인터페이스 추가

<br>

- v1.0.11 (2021-09-30)
  + 태블릿 모드에서 화면 비율 설정하기 옵션 추가 (영상 꽉 채우기 또는 영상 비율에 맞추기)
  + userScore 추가
  + onChangedPlayerStatus interface 추가

<br>

- v1.0.10 (2021-09-10)
  + onPause 이벤트를 받았을 때, 채팅 입력창과 키보드가 떠있는 경우에 채팅 입력창과 키보드를 숨김.

<br>

- v1.0.9 (2021-09-08)
  + 방송 진입시에만 로딩 프로그레스 출력. 그 이후, 버퍼가 발생한 경우에는 로딩 프로그레스 출력하지 않도록 변경.

<br>

- v1.0.8 (2021-09-06)
  + 방송 진입시, 로딩 프로그레스 출력 (색상 적용)

<br>

- v1.0.7 (2021-09-01)
  + PIP 모드에서 Full 화면으로 올라오면서 백그라운드로 내려간 앱의 activity를 포그라운드로 올라오도록 함.

<br>

- v1.0.6 (2021-08-31)
  + ShopLive.showPreview(campaignKey) 추가 (다른 앱 위에 표시 권한 필요)
  + ShopLive.showPreview(campaignKey, isOsPip = true) 추가 (방송 진입 후, PIP로 전환)
  + handlePreview(context, campaignKey) 인터페이스 추가
  + 오류 수정

<br>

- v1.0.5 (2021-08-25)
  + handleDownloadCoupon(context, couponId, callback) 인터페이스 추가

<br>

- v1.0.3 (2021-08-18)
  + 시청중인 방송에서 강제로 나가기 기능 추가
  + 방송 진입 시, 캠페인 정보 전달
  + 캠페인 상태 변경 시, 상태값 전달
  + 오류 상황 발생 시, 코드/메시지 전달
  + 통화 종료 후 영상 복귀 시, 기본값을 '영상 멈춤'에서 '영상 자동 재생'으로 변경
  
  <br>

- v1.0.2 (2021-08-09)
  + 이어폰/헤드셋 연결 끊겼을 때에 계속 재생과 함께 mute 옵션 추가

<br>

- v1.0.1 (2021-08-05)
  + custom payload 전달 지원 (CUSTOM_ACTION)
  + 대화 입력창 폰트 세팅 지원

<br>

- v1.0.0 (2021-08-04)
  + android SDK 런칭

<br>
<br>