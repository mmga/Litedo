package com.mmga.litedo;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mmga on 2015/10/22.
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
        init(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        AssetManager assetMgr = context.getAssets();
        Typeface font = Typeface.createFromAsset(assetMgr, "fonts/yuehei.ttf");
        setTypeface(font);
    }


}
