package com.mmga.litedo.Aty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mmga.litedo.R;
import com.mmga.litedo.Util.SharedPrefsUtil;
import com.mmga.litedo.Util.StatusBarCompat;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private RelativeLayout mShowTime, mPullToAdd, mLicense;
    private TextView mSuggest, mRating;
    private CheckBox mShowTimeCheckbox, mPullToAddCheckbox;

    private boolean isShowTimeChecked;
    public static final int PULL_TO_ADD = 0;
    public static final int PULL_TO_DO_NOTHING = 1;
    private int pullToAddState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimaryDark));

        isShowTimeChecked = SharedPrefsUtil.getValue(this, "settings", "isShowTime", false);
        pullToAddState = SharedPrefsUtil.getValue(this, "settings", "pullToAddState", PULL_TO_DO_NOTHING);
        Log.d("mmga", "" + pullToAddState);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.setting);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mShowTime = (RelativeLayout) findViewById(R.id.showtime);
        mPullToAdd = (RelativeLayout) findViewById(R.id.pull_to_add);
        mSuggest = (TextView) findViewById(R.id.suggest);
        mRating = (TextView) findViewById(R.id.rating);
        mLicense = (RelativeLayout) findViewById(R.id.license_layout);
        mShowTimeCheckbox = (CheckBox) findViewById(R.id.showtime_checkbox);
        mShowTimeCheckbox.setChecked(isShowTimeChecked);
        mPullToAddCheckbox = (CheckBox) findViewById(R.id.pull_to_add_checkbox);
        mPullToAddCheckbox.setChecked(pullToAddState == PULL_TO_ADD);


        mShowTime.setOnClickListener(this);
        mPullToAdd.setOnClickListener(this);
        mSuggest.setOnClickListener(this);
        mRating.setOnClickListener(this);
        mLicense.setOnClickListener(this);
        mShowTimeCheckbox.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showtime_checkbox:
                switchShowTimeCheckbox();
            case R.id.showtime:
                switchShowTimeCheckbox();
                break;
            case R.id.pull_to_add_checkbox:
                switchPullToAddCheckbox();
            case R.id.pull_to_add:
                switchPullToAddCheckbox();
                break;
            case R.id.suggest:
                sendEmail();
//                ToastUtil.showShort("假的");
                break;
            case R.id.rating:
                openMarket();
//                ToastUtil.showShort("是假的");
                break;
            case R.id.license_layout:
//                ToastUtil.showShort("是头发的特技");
                break;
        }
    }

    private void sendEmail() {
        Uri uri = Uri.parse("mailto:1034752946@qq.com");
        String[] email = {"1034752946@qq.com"};
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分"); // 正文
        startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
    }


    private void openMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void switchPullToAddCheckbox() {
        if (!mPullToAddCheckbox.isChecked()) {
            mPullToAddCheckbox.setChecked(true);
            pullToAddState = PULL_TO_ADD;
        } else {
            mPullToAddCheckbox.setChecked(false);
            pullToAddState = PULL_TO_DO_NOTHING;
        }
    }

    private void switchShowTimeCheckbox() {
        if (!mShowTimeCheckbox.isChecked()) {
            mShowTimeCheckbox.setChecked(true);
            isShowTimeChecked = true;
        } else {
            mShowTimeCheckbox.setChecked(false);
            isShowTimeChecked = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putBoolean("isShowTime", isShowTimeChecked);
        editor.putInt("pullToAddState", pullToAddState);
        editor.apply();

//        SharedPrefsUtil.putValue(this, "settings", "isShowTime", isShowTimeChecked);
//        SharedPrefsUtil.putValue(this, "settings", "pullToAddState", pullToAddState);
//        Log.d("mmga", "" + pullToAddState);

    }
}
