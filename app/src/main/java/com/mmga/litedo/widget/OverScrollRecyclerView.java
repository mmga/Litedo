package com.mmga.litedo.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;


public class OverScrollRecyclerView extends RecyclerView{

    private static final int MAX_OVERSCROLL_Y = 200;
    private int newMaxOverScrollY;

    public OverScrollRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public OverScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OverScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float density = metrics.density;
        newMaxOverScrollY = (int) (density * MAX_OVERSCROLL_Y);
        this.setOverScrollMode(OverScrollRecyclerView.OVER_SCROLL_ALWAYS);
    }


    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, newMaxOverScrollY, isTouchEvent);
    }
}
