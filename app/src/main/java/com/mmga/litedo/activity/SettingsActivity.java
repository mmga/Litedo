package com.mmga.litedo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mmga.litedo.R;
import com.mmga.litedo.util.SharedPrefsUtil;
import com.mmga.litedo.util.StatusBarCompat;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarLayout appBarLayout;
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
        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorPrimaryDark));

        isShowTimeChecked = SharedPrefsUtil.getValue(this, "settings", "isShowTime", false);
        pullToAddState = SharedPrefsUtil.getValue(this, "settings", "pullToAddState", PULL_TO_DO_NOTHING);
        Log.d("mmga", "" + pullToAddState);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showtime:
                switchShowTimeCheckbox();
                break;
            case R.id.pull_to_add:
                switchPullToAddCheckbox();
                break;
            case R.id.suggest:
                sendEmail();
                break;
            case R.id.rating:
                openMarket();
                break;
            case R.id.license_layout:
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
        } else {
            mPullToAddCheckbox.setChecked(false);
        }
    }

    private void switchShowTimeCheckbox() {
        if (!mShowTimeCheckbox.isChecked()) {
            mShowTimeCheckbox.setChecked(true);
        } else {
            mShowTimeCheckbox.setChecked(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putBoolean("isShowTime", mShowTimeCheckbox.isChecked());
        editor.putInt("pullToAddState", mPullToAddCheckbox.isChecked()?PULL_TO_ADD:PULL_TO_DO_NOTHING);
        editor.apply();


    }
}
