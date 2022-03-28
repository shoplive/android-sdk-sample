package cloud.shoplive.sample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONObject;

import cloud.shoplive.sdk.ShopLive;
import cloud.shoplive.sdk.ShopLiveHandler;
import cloud.shoplive.sdk.ShopLiveHandlerCallback;
import cloud.shoplive.sdk.ShopLiveUser;
import cloud.shoplive.sdk.ShopLiveUserGender;

/**
 * sample for java
 * */
public class SampleActivity extends AppCompatActivity {

    private final String TAG = SampleActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        ShopLive.init(getApplication());
        ShopLive.setAccessKey("yourAccessKey");
        ShopLive.setHandler(handler);

        ShopLiveUser user = new ShopLiveUser();
        user.setUserId("testId");
        user.setUserName("kate");
        user.setAge(20);
        user.setGender(ShopLiveUserGender.Female);
        user.setUserScore(100);

        ShopLive.setUser(user);

        // jwt authorize
        //ShopLive.setAuthToken("abcdefghijklmnopqrstuvwxyz==");
    }

    private void play() {
        ShopLive.setShareScheme("https://www.your.com");
        ShopLive.setLoadingProgressColor("#ff0000");
        //ShopLive.set ...
        // .
        // .
        // etc

        ShopLive.play("yourCampaignKey");
    }

    private ShopLiveHandler handler = new ShopLiveHandler() {

        @Override
        public void handleNavigation(@NonNull Context context, @NonNull String url) {
            ShopLive.ActionType type = ShopLive.ActionType.getType(Options.INSTANCE.playerNextAction(context));
            switch (type) {
                case PIP:
                case CLOSE:
                    Intent intent = new Intent(SampleActivity.this, WebViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("url", url);
                    startActivity(intent);
                    break;
                case KEEP:
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    DialogFragment webDialogFragment = new WebViewDialogFragment();
                    webDialogFragment.setArguments(bundle);
                    ShopLive.showDialogFragment(webDialogFragment);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void handleDownloadCoupon(@NonNull Context context, @NonNull String couponId, @NonNull ShopLiveHandlerCallback callback) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(getString(R.string.sample_coupon_download));
            builder.setMessage(getString(R.string.sample_coupon_download_id, couponId));
            builder.setPositiveButton(getString(R.string.success), (dialog, which) -> {
                callback.couponResult(
                        true,
                        getString(R.string.alert_coupon_download_success),
                        ShopLive.CouponPopupStatus.HIDE,
                        ShopLive.CouponPopupResultAlertType.TOAST);
            });

            builder.setNegativeButton(getString(R.string.fail), (dialog, which) -> {
                callback.couponResult(
                        false,
                        getString(R.string.alert_coupon_download_fail),
                        ShopLive.CouponPopupStatus.SHOW,
                        ShopLive.CouponPopupResultAlertType.ALERT);
            });

            builder.setCancelable(false);

            Dialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public void onChangeCampaignStatus(@NonNull Context context, @NonNull String campaignStatus) {
            Log.d(TAG, "onChangeCampaignStatus >> " + campaignStatus);
        }

        @Override
        public void onCampaignInfo(@NonNull JSONObject campaignInfo) {
            Log.d(TAG, "onCampaignInfo >> " + campaignInfo.toString());
        }

        @Override
        public void onError(@NonNull Context context, @NonNull String code, @NonNull String message) {
            Log.d(TAG, "code:" + code + ", message:" + message);
        }

        @Override
        public void handleCustomAction(@NonNull Context context, @NonNull String id, @NonNull String type, @NonNull String payload, @NonNull ShopLiveHandlerCallback callback) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom_action_dialog, null);
            TextView tvId = view.findViewById(R.id.tvId);
            TextView tvType = view.findViewById(R.id.tvType);
            TextView tvPayload = view.findViewById(R.id.tvPayload);

            tvId.setText(getString(R.string.sample_custom_action_id, id));
            tvType.setText(getString(R.string.sample_custom_action_type, type));
            tvPayload.setText(getString(R.string.sample_custom_action_payload, payload));

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(getString(R.string.sample_custom_action));
            builder.setView(view);
            builder.setPositiveButton(getString(R.string.success),  (dialog, which) -> {
                callback.customActionResult(
                        true,
                        getString(R.string.alert_custom_action_success),
                        ShopLive.CouponPopupStatus.HIDE,
                        ShopLive.CouponPopupResultAlertType.TOAST);
            });

            builder.setNegativeButton(getString(R.string.fail),  (dialog, which) -> {
                callback.customActionResult(
                        false,
                        getString(R.string.alert_custom_action_fail),
                        ShopLive.CouponPopupStatus.SHOW,
                        ShopLive.CouponPopupResultAlertType.ALERT);
            });

            builder.setCancelable(false);

            Dialog dialog = builder.create();
            dialog.show();
        }

        /**
         * @param isPipMode - pipMode:true, fullMode:false
         * @param state - 'CREATE' or 'DESTROY'
         * */
        @Override
        public void onChangedPlayerStatus(@NonNull Boolean isPipMode, @NonNull String state) {
            Log.d(TAG, "isPipMode:" + isPipMode + ", state:" + state);
        }

        @Override
        public void handlePreview(@NonNull Context context, @NonNull String campaignKey) {
            Log.d(TAG, "ck=" + campaignKey);
        }

        @Override
        public void handleShare(Context context, String shareUrl) {
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

        @Override
        public void onReceivedCommand(@NonNull Context context, @NonNull String command, @NonNull JSONObject data) {
            Log.d(TAG, "onReceivedCommand >> command:" + command + "data:" + data.toString());

            if (command.equals("LOGIN_REQUIRED")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(getString(R.string.alert_need_login));
                builder.setPositiveButton(getString(R.string.yes), ((dialog, which) -> {

                }));

                Dialog dialog = builder.create();
                dialog.show();
            }
        }
    };
}
