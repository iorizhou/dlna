package com.xiaojiutech.dlna;

import android.app.Application;
import android.content.Context;

import com.xiaojiutech.dlna.utils.CrashHandler;

import org.cybergarage.upnp.ControlPoint;

public class XiaojiuApplication extends  Application{
    private static XiaojiuApplication sInstance;
    public ControlPoint mControlPoint;
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


    public void setControlPoint(ControlPoint controlPoint) {
        mControlPoint = controlPoint;
    }


    public ControlPoint getControlPoint() {
        return mControlPoint;
    }
}
