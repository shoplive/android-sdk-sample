# Change Log

- v1.2.2 (2022-03-10)
  + We improved to link quiz functions such as vibration and sound effects.
  + We improved to play silently when previewing.
  + Added API that is selectable the next action instead of forcibly switching to PIP in the app when the user selects a product or notices.
  + Added API to set endpoint.

<br>

- v1.2.1 (2022-01-13)
  + We fixed the unstable video stream issue when the player launches that arose in low network speed environments.

<br>

- v1.2.0 (2021-12-15)
  + ShopLive.getAuthToken() - Retrieve JWT Authentication Key
  + ShopLive.isSuccessCampaignJoin() - Verify Campaign Entry
  + ShopLive.startPictureInPicture() - Entering PIP mode
  + ShopLive.stopPictureInPicture() - Leaving PIP mode(returning to FULL screen)

<br>

- v1.1.2 (2021-11-15)
  + added onReceivedCommand handler function used to deliver command information received from SDK's web

<br>

- v1.1.1 (2021-11-08)
  + added callback for success and failure of coupon download and custom action
  + added alert for setting or modifying user name

<br>

- v1.0.15 (2021-11-03)
  + added image animation progress interface
  + added an option to prevent campaign automatically end on root activity end

<br>

- v1.0.13 (2021-10-14)
  + applied automatic hide/show when app background/foreground switches is playing preview (if display over other apps option is enabled)
  + added ShopLive.hidePreview() interface to tempoarily hide preview during play (if display over other apps option is enabled)
  + added ShopLive.resumePreview() interface to resume when preview is hidden during play

<br>

- v1.0.11 (2021-09-30)
  + added screen ratio settings on tablet mode (match to fit screen or video ratio)
  + userScore 추가
  + onChangedPlayerStatus interface 추가

<br>

- v1.0.10 (2021-09-10)
  + hide chat input and keyboard if being displayed on onPause event receiveal.

<br>

- v1.0.9 (2021-09-08)
  + displays loading progress only on campaign entry. does not display loading progress if a buffer occurs.

<br>

- v1.0.8 (2021-09-06)
  + displays loading progress on campaign entry (with color applied)

<br>

- v1.0.7 (2021-09-01)
  + returning to Full screen from PIP mode brings app's activity to foregroundPIP from the background.

<br>

- v1.0.6 (2021-08-31)
  + added ShopLive.showPreview(campaignKey) (require display over other apps option to be enabled)
  + added ShopLive.showPreview(campaignKey, isOsPip = true) (switch to PIP on campaign entry)
  + added handlePreview(context, campaignKey) interface
  + fixed errors

<br>

- v1.0.5 (2021-08-25)
  + added a handleDownloadCoupon(context, couponId, callback) interface

<br>

- v1.0.3 (2021-08-18)
  + added an option to force leave from current campaign
  + deliver campaign information on campaign entry
  + deliver state change on campaign status change
  + deliver code/message on error
  + changed default value of video play after phone call to "resume video" from "stop video"
  
  <br>

- v1.0.2 (2021-08-09)
  + added an option to resume playing during earphone/headset disconnection and to mute volume

<br>

- v1.0.1 (2021-08-05)
  + supports custom payload delivery (CUSTOM_ACTION)
  + supports chat input font settings

<br>

- v1.0.0 (2021-08-04)
  + launched android SDK

<br>
<br>