package com.mmga.litedo.widget;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

public class CustomFab extends FloatingActionButton {
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
    }

    @Override
    public void show() {
        super.show();
    }
}
