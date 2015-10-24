package com.mmga.litedo;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

/**
 * Created by mmga on 2015/10/23.
 */
public class MySoundPool {

    private static SoundPool soundPool;
    private static int soundAdd;
    private static int soundDelete;


    private final Context mContext;

    public MySoundPool(Context context) {
        mContext = context;
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
        soundAdd = soundPool.load(mContext, R.raw.poka01, 1);
        soundDelete = soundPool.load(mContext, R.raw.coin04, 1);
    }

    public static void playSoundAdd() {
        soundPool.play(soundAdd, 0.3f, 0.3f, 0, 0, 1.0f);
        Log.d(">>>>>", "soundadd");



    }

    public static void playSoundDelete() {
        soundPool.play(soundDelete, 0.3f, 0.3f, 0, 0, 1.0f);
        Log.d(">>>>>", "sounddelete");

    }
}
