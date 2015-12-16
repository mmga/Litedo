package com.mmga.litedo.widget;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

import com.mmga.litedo.R;

public class CustomFab extends FloatingActionButton {

    FloatingActionButton fab;

    public CustomFab(Context context) {
        super(context);
        init(context);
    }

    public CustomFab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomFab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        fab = (FloatingActionButton) findViewById(R.id.fab_add);
    }

    @Override
    public void show() {
        super.show();
    }
}
