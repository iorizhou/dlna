package com.xiaojiutech.dlna.mvp.activity;


import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.xiaojiutech.dlna.utils.ActivityHolder;
import com.xiaojiutech.dlna.utils.AdmobConstants;





public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    public FirebaseAnalytics mFirebaseAnalytics;

    public InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHolder.getInstance().addActivity(this);
        //google analytics init
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    public void  initInterstitialAd(){
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(AdmobConstants.INTERSTIALAD);
    }
    public void showInterstitialAds(AdListener listener){
        mInterstitialAd.setAdListener(listener);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }
    @Override
    protected void onDestroy() {
        ActivityHolder.getInstance().removeActivity(this);
        super.onDestroy();
        //注销广播

    }



}
