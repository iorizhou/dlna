package com.xiaojiutech.dlna.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.xiaojiutech.dlna.utils.AdmobConstants;

public class BaseFragment extends Fragment {
    public InterstitialAd mInterstitialAd;

    public RewardedVideoAd mVideoAd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInterstitialAd();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void  initInterstitialAd(){
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(AdmobConstants.INTERSTIALAD);
    }

    public void loadBannerAd(AdListener listener, AdView mBannerAd){
        AdRequest request = new AdRequest.Builder().build();
//        mBannerAd.setAdUnitId(AdmobConstants.BANNERAD);
//        mBannerAd.setAdSize(AdSize.SMART_BANNER);
        mBannerAd.setAdListener(listener);
        mBannerAd.loadAd(request);
    }

    public void loadVideoAd(RewardedVideoAdListener listener){
        mVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mVideoAd.setRewardedVideoAdListener(listener);
        mVideoAd.loadAd(AdmobConstants.REWARDAD,new AdRequest.Builder().build());
    }
}
