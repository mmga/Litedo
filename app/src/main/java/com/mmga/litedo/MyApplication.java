package com.mmga.litedo;

import android.content.Context;

import org.litepal.LitePalApplication;

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
