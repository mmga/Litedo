package com.mmga.litedo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.mmga.litedo.Util.LogUtil;

public class TextInputActivity extends Activity {

    private EditText mEditText;

    public final static int RESULT_CODE_NEW = 1000;
    public final static int RESULT_CODE_EDIT = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        ImageButton mButtonOk = (ImageButton) findViewById(R.id.btn_ok);
        mEditText = (EditText) findViewById(R.id.edit_text);

        Intent intent = getIntent();
        final String data = intent.getStringExtra("data");
        final int position = intent.getIntExtra("position", -1);


        if (data != null) {
            mEditText.setText(data);
        } else {
            mEditText.setText("");
        }


        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMemo(position);
            }
        });

        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);


        //设置默认输入法为中文
        mEditText.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        //监听输入法按键，改回车按钮为保存
        mEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    saveMemo(position);
                    return true;
                }
                return false;
            }
        });

        mEditText.setImeOptions(EditorInfo.IME_ACTION_NONE);
    }

    private void saveMemo(int position) {
        Intent intent = new Intent();
        intent.putExtra("content", mEditText.getText().toString());
        intent.putExtra("time", System.currentTimeMillis());
        if (position == -1) {
            setResult(RESULT_CODE_NEW, intent);
            finish();
        } else {
            intent.putExtra("position", position);
            setResult(RESULT_CODE_EDIT, intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        openKeyboard(MyApplication.getContext(), mEditText);
        mEditText.requestFocus();
        super.onResume();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        closeKeyboard(MyApplication.getContext(), mEditText);
    }

    private void openKeyboard(Context context, View editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
        LogUtil.d("mmga", "openKeyboard");

    }

    private void closeKeyboard(Context context, View editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        LogUtil.d("mmga", "closeKeyboard");
    }

}



