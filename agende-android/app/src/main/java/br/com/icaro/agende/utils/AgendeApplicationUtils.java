package br.com.icaro.agende.utils;

import android.app.Application;
import android.content.Context;

public class AgendeApplicationUtils extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getAgendeAppContext() {
        return appContext;
    }
}
