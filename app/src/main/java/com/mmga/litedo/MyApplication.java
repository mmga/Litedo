package com.mmga.litedo;

import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by mmga on 2015/10/24.
 */
public class MyApplication extends LitePalApplication {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

    public static Context getContext() {
        return context;
    }

}
