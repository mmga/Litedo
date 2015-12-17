package com.mmga.litedo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mmga.litedo.Adapter.RecyclerViewAdapter;
import com.mmga.litedo.R;
import com.mmga.litedo.Util.DensityUtil;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;


public class CustomPtrHeader extends RelativeLayout implements PtrUIHandler {


    private ImageView imageView;
    private final int ADD_NEW_MEMO = 0;
    private final int CANCEL_NEW_MEMO = 1;
    private int flagAdd;
    private RecyclerViewAdapter adapter;

    public CustomPtrHeader(Context context, RecyclerViewAdapter adapter) {
        super(context);
        this.adapter = adapter;
        init(context);
    }

    public CustomPtrHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomPtrHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.header_view, this);
        imageView = (ImageView) view.findViewById(R.id.header_view);
        flagAdd = CANCEL_NEW_MEMO;

    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
//        Log.d("mmga", "onUIReset");
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
//        Log.d("mmga", "onUIRefreshPrepare");
        imageView.setVisibility(VISIBLE);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
//        Log.d("mmga", "onUIRefreshComplete");
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        float percent = Math.min(1f, ptrIndicator.getCurrentPercent());
        if (isUnderTouch) {
            if (percent > 0.5) {
                flagAdd = ADD_NEW_MEMO;
            } else {
                flagAdd = CANCEL_NEW_MEMO;
            }

            imageView.setTranslationY(DensityUtil.dip2px(getContext(), 60) * (1 - 2 * percent));
            imageView.setScaleX((float) (Math.min(0.8 + 0.4 * percent, 1f)));
            imageView.setScaleY((float) (Math.min(0.9 + 0.2 * percent, 1f)));
        } else {
            int flag = flagAdd;
            if (flag == ADD_NEW_MEMO) {
                imageView.setVisibility(GONE);
            } else {
                imageView.setTranslationY(DensityUtil.dip2px(getContext(), 60) * (1 - 2 * percent));
                imageView.setScaleX((float) (Math.min(0.8 + 0.4 * percent, 1f)));
                imageView.setScaleY((float) (Math.min(0.9 + 0.2 * percent, 1f)));
            }
        }

        invalidate();


    }
}
