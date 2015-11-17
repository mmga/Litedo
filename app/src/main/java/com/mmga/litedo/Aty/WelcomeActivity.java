package com.mmga.litedo.Aty;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mmga.litedo.R;
import com.mmga.litedo.Util.DBUtil;


public class WelcomeActivity extends Activity {

    private final int TIME_DELAY = 500;

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
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void initInfo() {

        DBUtil.addData("开始使用吧");
        DBUtil.addData("向右滑动删除");
        DBUtil.addData("长按拖动");
        DBUtil.addData("点击项目查看更多操作");
        DBUtil.addData("右下角按钮添加新便签");

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
