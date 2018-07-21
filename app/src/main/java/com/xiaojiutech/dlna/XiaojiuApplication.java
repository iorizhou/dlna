package com.xiaojiutech.dlna;

import android.app.Application;
import android.content.Context;

import com.xiaojiutech.dlna.utils.CrashHandler;

public class XiaojiuApplication extends  Application{
    private static XiaojiuApplication sInstance;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        CrashHandler.getInstance().init(getApplicationContext());
        sInstance = this;
    }

    public static XiaojiuApplication getInstace(){
        return sInstance;
    }
}
