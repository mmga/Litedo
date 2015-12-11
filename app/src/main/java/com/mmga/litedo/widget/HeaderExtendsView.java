package com.mmga.litedo.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mmga.litedo.R;


public class HeaderExtendsView extends RelativeLayout {


    RecyclerView mRecyclerView;
    LinearLayout headerView;
    RecyclerView bodyRecyclerView;
    private boolean isReady;
    private float mStartY;
    private float mOffsetY;
    private int mHeaderViewHeight;


    public HeaderExtendsView(Context context) {
        super(context);
        init(context);
    }

    public HeaderExtendsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeaderExtendsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        Log.d("mmga", "init");
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        headerView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.header_view, null, false);
        measureView(headerView);
        mHeaderViewHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -mHeaderViewHeight / 2, 0, 0);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isReady) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mStartY = event.getY();
                    Log.d("mmga", "down + " + mStartY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mOffsetY = event.getY() - mStartY;
                    headerView.setPadding(0, (int) (-mHeaderViewHeight + mOffsetY), 0, 0);
                    Log.d("mmga", "move + " + mOffsetY);
                    invalidate();

            }
        }
        return true;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = 2;
        int cWidth = 0;
        int cHeight = 0;
        int cHeaderHeight = 0;

        for (int i = 0; i < 2; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();

            int cl = 0, ct = 0, cr = 0, cb = 0;

            switch (i) {
                case 0:
                    ct = 0;
                    cHeaderHeight = cHeight;
                    break;
                case 1:
                    ct = cHeaderHeight;
            }

            cr = cl + getWidth();
            cb = ct + cHeight;
            childView.layout(cl, ct, cr, cb);
        }





        super.onLayout(changed, l, t, r, b);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int childHeightSpec;
        int lpHeight = p.height;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);

    }


}
