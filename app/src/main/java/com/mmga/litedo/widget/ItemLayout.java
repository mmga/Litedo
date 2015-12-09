package com.mmga.litedo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.mmga.litedo.MyApplication;


public class ItemLayout extends LinearLayout {

    public ItemLayout(Context context) {
        super(context);
    }

    public ItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);


//        setMeasuredDimension(getWindowWidth() + menuNumber * 52, heightMeasureSpec);

//        measureChildren(widthMeasureSpec, heightMeasureSpec);
//
//        int cCount = getChildCount();
//        int cWidth = 0;
//
//        View childView = getChildAt(1);
//        cWidth = childView.getMeasuredWidth();

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);


    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;

        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();

            int cl = 0, ct = 0, cr = 0, cb = 0;

            switch (i) {
                case 0:
                    cl = 0;
                    break;
                case 1:
                    cl = getWindowWidth();
                    break;
                case 2:
                    cl = getWidth() - cWidth;
                    break;
            }
            cr = cl + cWidth;
            cb = ct + cHeight;
            childView.layout(cl, ct, cr, cb);
        }
    }

    private static int getWindowWidth() {
        return MyApplication.getContext().getResources().getDisplayMetrics().widthPixels;
    }

}
