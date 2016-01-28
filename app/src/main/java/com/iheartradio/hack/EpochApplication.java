package com.iheartradio.hack;

import android.app.Application;
import android.os.Handler;

import com.iheartradio.hack.cast.CastThread;

public class EpochApplication extends Application {
    public static Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        CastThread.start();
    }
}
