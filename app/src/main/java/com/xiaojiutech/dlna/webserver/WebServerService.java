package com.xiaojiutech.dlna.webserver;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class WebServerService extends Service {
    private AndroidWebServer mWebServer;
    public class MyBinder extends Binder {

        public WebServerService getService(){
            return WebServerService.this;
        }

    }
    private MyBinder binder = new MyBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public AndroidWebServer getmWebServer() {
        return mWebServer;
    }

    public boolean startAndroidWebServer(String ip, int port) {
            try {
                if (port == 0) {
                    throw new RuntimeException("port error");
                }
                mWebServer = new AndroidWebServer(port);
                mWebServer.start();
                return true;
            } catch (Exception e) {
                Log.i("WebServerService","startAndroidWebServer ERROR : "+e.getMessage());
                e.printStackTrace();
                return false;
            }
    }
}
