package com.mmga.litedo.Widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mmga.litedo.MyApplication;
import com.mmga.litedo.R;
import com.mmga.litedo.Util.DBUtil;

public class CustomDialogAty extends Activity{

    private EditText mEditText;
    private Context mContext;
    private ImageButton mButtonOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        mButtonOk = (ImageButton) findViewById(R.id.btn_ok);
        mEditText = (EditText) findViewById(R.id.edit_text);
//        Typeface type = Typeface.createFromAsset(mContext.getAssets(), "fonts/yuehei.ttf");
//        mEditText.setTypeface(type);

        mEditText.setText("");
        mEditText.requestFocus();

        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveMemo();
            }
        });

        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);

        //打开软键盘
        InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);
        Log.d(">>>>>", "1");

        //设置默认输入法为中文
        mEditText.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        /**
         * 监听输入法按键，改回车按钮为保存
         */
        mEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    saveMemo();
                    return true;
                }
                return false;
            }
        });

        /**
         * 改变输入法中回车按钮de显示内容为“完成”
         */
        mEditText.setImeOptions(EditorInfo.IME_ACTION_NONE);

    }

    private void saveMemo() {

        DBUtil.addMemo(mEditText.getText().toString());
        finish();
    }


}



