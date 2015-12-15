package com.mmga.litedo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;



//暂时没用
public class TextInputDialog extends Dialog {

    private EditText mEditText;
    private Context mContext;
    private ImageButton mButtonOk;

    public TextInputDialog(Context context) {
        this(context, R.style.CustomDialog);
        mContext = context;
    }

    public TextInputDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public TextInputDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        mButtonOk = (ImageButton) findViewById(R.id.btn_ok);
        mEditText = (EditText) findViewById(R.id.edit_text);

        mEditText.setText("");
        mEditText.requestFocus();

        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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
                    dismiss();
                    return true;
                }
                return false;
            }
        });


    }


}
