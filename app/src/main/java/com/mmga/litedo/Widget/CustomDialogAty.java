package com.mmga.litedo.Widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mmga.litedo.R;
import com.mmga.litedo.Util.DBUtil;

public class CustomDialogAty extends Activity{

    private EditText mEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        ImageButton mButtonOk = (ImageButton) findViewById(R.id.btn_ok);
        mEditText = (EditText) findViewById(R.id.edit_text);

        Intent intent = getIntent();
        final String data = intent.getStringExtra("data");
        final int position = intent.getIntExtra("position",-1);

        if (data != null) {
            mEditText.setText(data);
        } else {
            mEditText.setText("");
        }

        //限制字符长度
        // TODO: 怎么限制15个中文字符30个英文呢？
//        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});

        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMemo(data, position);
            }
        });

        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);



        //设置默认输入法为中文
        mEditText.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        /**
         * 监听输入法按键，改回车按钮为保存
         */
        mEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    saveMemo(data,position);
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

    private void saveMemo(String data,int position) {

        if (data == null) {
            DBUtil.addData(mEditText.getText().toString());
            finish();
        } else {
//            DBUtil.updateDataByContent(mEditText.getText().toString(), data);
            DBUtil.updateDataByPosition(mEditText.getText().toString(),position);
            finish();
        }
    }

    @Override
    protected void onResume() {
//        //打开软键盘
//        InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);

        mEditText.requestFocus();
        super.onResume();
    }
}



