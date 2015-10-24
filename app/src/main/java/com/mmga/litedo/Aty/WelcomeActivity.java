package com.mmga.litedo.Aty;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mmga.litedo.R;
import com.mmga.litedo.db.DBUtil;

/**
 * Created by mmga on 2015/10/23.
 */
public class WelcomeActivity extends Activity {

    private final int TIME_DELAY = 1000;

    private boolean isFirstIn = false;

    private final int NEW_USER = 1001;
    private final int OLD_USER = 1002;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEW_USER:
                    initInfo();
                    break;
                case OLD_USER:
                    goListAty();
                    break;
            }
        }
    };

    private void goListAty() {
        Intent i = new Intent(WelcomeActivity.this, ListActivity.class);
        startActivity(i);
        finish();
    }

    private void initInfo() {

        DBUtil.addMemo("右下角按钮添加新便签");
        DBUtil.addMemo("滑动删除");
        DBUtil.addMemo("长按置顶");
        DBUtil.addMemo("开始使用吧");

        goListAty();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        SharedPreferences preferences = getSharedPreferences("firstIn", MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        if (!isFirstIn) {
            mHandler.sendEmptyMessageDelayed(OLD_USER, TIME_DELAY);
        } else {
            mHandler.sendEmptyMessageDelayed(NEW_USER, TIME_DELAY);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstIn", false);
            editor.apply();
        }
    }
}
