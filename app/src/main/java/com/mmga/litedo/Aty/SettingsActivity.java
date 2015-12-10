package com.mmga.litedo.Aty;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mmga.litedo.R;
import com.mmga.litedo.Util.SharedPrefsUtil;
import com.mmga.litedo.Util.StatusBarCompat;
import com.mmga.litedo.Util.ToastUtil;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private RelativeLayout mShowTime, mDarkTheme, mQuickMode;
    private TextView mSuggest, mRating, mLicense;
    private CheckBox mShowTimeCheckbox, mQuickModeCheckbox;

    private boolean isShowTimeChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimaryDark));


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
        mDarkTheme = (RelativeLayout) findViewById(R.id.dark_theme);
        mQuickMode = (RelativeLayout) findViewById(R.id.quick_mode);
        mSuggest = (TextView) findViewById(R.id.suggest);
        mRating = (TextView) findViewById(R.id.rating);
        mLicense = (TextView) findViewById(R.id.license);
        mShowTimeCheckbox = (CheckBox) findViewById(R.id.showtime_checkbox);
        mShowTimeCheckbox.setChecked(SharedPrefsUtil.getValue(this, "settings", "isShowTime", false));
        mQuickModeCheckbox = (CheckBox) findViewById(R.id.quick_mode_checkbox);


        mShowTime.setOnClickListener(this);
        mDarkTheme.setOnClickListener(this);
        mQuickMode.setOnClickListener(this);
        mSuggest.setOnClickListener(this);
        mRating.setOnClickListener(this);
        mLicense.setOnClickListener(this);
        mShowTimeCheckbox.setOnClickListener(this);

        isShowTimeChecked = SharedPrefsUtil.getValue(this, "settings", "isShowTime", false);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showtime_checkbox:
                switchShowTimeCheckbox();
            case R.id.showtime:
                switchShowTimeCheckbox();
                break;
            case R.id.dark_theme:
                ToastUtil.showShort("这个技能还没有准备好");
                break;
            case R.id.quick_mode:
                ToastUtil.showShort("冷却中");
                break;
            case R.id.quick_mode_checkbox:
                ToastUtil.showShort("冷却中");
                break;
            case R.id.suggest:
                ToastUtil.showShort("我还不能使用这个技能");
                break;
            case R.id.rating:
                ToastUtil.showShort("我还不能使用这个技能");
                break;
            case R.id.license:
                ToastUtil.showShort("我还不能使用这个技能");
                break;
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
        SharedPrefsUtil.putValue(this, "settings", "isShowTime", isShowTimeChecked);
    }
}
