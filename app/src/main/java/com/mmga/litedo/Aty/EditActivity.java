package com.mmga.litedo.Aty;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.mmga.litedo.R;
import com.mmga.litedo.db.DBUtil;

/**
 * Created by mmga on 2015/10/19.
 * 临时的Activity 等学会自定义view再修改
 */
public class EditActivity extends AppCompatActivity {

    private EditText mEditText;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_new_memo);
        mEditText = (EditText) findViewById(R.id.edit_text);
        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBUtil.addMemo(String.valueOf(mEditText.getText()));
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
