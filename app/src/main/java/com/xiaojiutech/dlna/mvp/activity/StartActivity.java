package com.xiaojiutech.dlna.mvp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.MobileAds;
import com.xiaojiutech.dlna.R;
import com.xiaojiutech.dlna.utils.AdmobConstants;

public class StartActivity extends Activity {

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(StartActivity.this,MainActivity.class));
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        MobileAds.initialize(this, AdmobConstants.ADMOB_APPID);
        mHandler.sendEmptyMessageDelayed(0,1500);
    }
}
