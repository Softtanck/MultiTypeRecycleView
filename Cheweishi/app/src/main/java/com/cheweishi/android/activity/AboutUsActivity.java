package com.cheweishi.android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.widget.CustomDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 关于我们
 *
 * @author chenrunguo
 */
public class AboutUsActivity extends BaseActivity implements OnClickListener {
    @ViewInject(R.id.title)
    private TextView tvTitle;
    @ViewInject(R.id.left_action)
    private Button tvLeft;
    @ViewInject(R.id.right_action)
    private TextView tvRight;
    @ViewInject(R.id.tv_version)
    private TextView tvVersion;// 显示版本号
    @ViewInject(R.id.tv_show_update)
    private TextView tvShowUpdate;// 显示是否需要跟新
    private CustomDialog.Builder updateBuilder;
//    private UpdateResponse updateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        tvTitle.setText(R.string.about_us);
        tvRight.setVisibility(View.INVISIBLE);
        tvLeft.setText(R.string.back);
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            tvVersion.setText(getText(R.string.banbenhao) + pi.versionName);
        } catch (NameNotFoundException e) {
        }

//        UmengUpdateAgent.setUpdateAutoPopup(false);
//        UmengUpdateAgent.setUpdateListener(updateListener);
//        UmengUpdateAgent.update(AboutUsActivity.this);
    }

//    UmengUpdateListener updateListener = new UmengUpdateListener() {
//
//        @Override
//        public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
//            switch (updateStatus) {
//                case UpdateStatus.Yes: // has update
//                    AboutUsActivity.this.updateInfo = updateInfo;
//                    tvShowUpdate.setText(AboutUsActivity.this
//                            .getString(R.string.check_update_new)
//                            + getText(R.string.zhi)
//                            + updateInfo.version
//                            + getText(R.string.banben));
//                    break;
//                case UpdateStatus.No: // has no update
//                    // Toast.makeText(mContext, "没有更新", Toast.LENGTH_SHORT).show();
//                    break;
//                case UpdateStatus.NoneWifi: // none wifi
//                    showToast(R.string.updata_wifi_hint);
//                    break;
//                case UpdateStatus.Timeout: // time out
//                    showToast(R.string.chaoshi);
//                    break;
//            }
//
//        }
//    };

    @OnClick({R.id.left_action, R.id.llayout_check_update,
            R.id.llayout_function, R.id.llayout_team, R.id.bt_officialTel,
            R.id.bt_officialWebsite, R.id.bt_officialEmail})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:
                AboutUsActivity.this.finish();
                break;
            case R.id.llayout_check_update:
//                if (updateInfo != null) {
//                    showUpdateBuilder();
//                    // UmengUpdateAgent.startDownload(mContext, updateInfo);
//                    // tvShowUpdate.setText("已经是最新版本");
//                }
                break;
            case R.id.llayout_function:
                startActivity(new Intent(AboutUsActivity.this,
                        FunctionIntroductionActivity.class));
                break;
            case R.id.llayout_team:
                startActivity(new Intent(AboutUsActivity.this,
                        TeamIntroduceActivity.class));
                break;
            case R.id.bt_officialTel:

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + "4007930888"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
            case R.id.bt_officialWebsite:
                // if (hasBrowser(AboutUsActivity.this)) {
                try {
                    Uri uri = Uri.parse("http://www.chcws.com");
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    showToast(R.string.no_browser);
                }
                // } else {
                // Toast.makeText(AboutUsActivity.this, "", Toast.LENGTH_LONG)
                // .show();
                // }
                break;
            case R.id.bt_officialEmail:
                // if (hasEmail(AboutUsActivity.this)) {
                try {
                    Intent data = new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto:official@chcws.com"));
                    startActivity(data);
                } catch (Exception e) {
                    try {
                        Uri uri = Uri.parse("http://www.official@chcws.com");
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } catch (Exception el) {
                        showToast(R.string.no_browser);
                    }
                }
                // } else {
                // if (hasBrowser(AboutUsActivity.this)) {
                // uri = Uri.parse("http://www.official@chcws.com");
                // intent = new Intent(Intent.ACTION_VIEW, uri);
                // startActivity(intent);
                // } else {
                // Toast.makeText(AboutUsActivity.this,
                // "没有在您的手机上发现邮箱应用，请使用其他方式联系我们", Toast.LENGTH_LONG)
                // .show();
                // }
                // }
                break;
            default:
                break;
        }
    }

    /**
     * 更新提示
     */
    protected void showUpdateBuilder() {
        updateBuilder = new CustomDialog.Builder(AboutUsActivity.this);
        updateBuilder.setMessage(R.string.determine_updata);
        updateBuilder.setTitle(R.string.remind);
        updateBuilder.setPositiveButton(getText(R.string.sure) + "",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        UmengUpdateAgent.startDownload(AboutUsActivity.this,
//                                updateInfo);
//                        updateInfo = null;
                    }
                });

        updateBuilder.setNegativeButton(getText(R.string.cancel) + "",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        updateBuilder.create().show();
    }
}
