package com.xiaojiutech.dlna.mvp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.xiaojiutech.dlna.XiaojiuApplication;
import com.xiaojiutech.dlna.bean.MaterialBean;
import com.xiaojiutech.dlna.utils.AdmobConstants;
import com.xiaojiutech.dlna.utils.AudioLoadUtil;
import com.xiaojiutech.dlna.utils.PictureLoadUtil;
import com.xiaojiutech.dlna.utils.VideoLoadUtil;

import java.util.ArrayList;
import java.util.List;

public class BaseFragment extends Fragment {
    public static final String TAG = BaseFragment.class.getSimpleName();
    public InterstitialAd mInterstitialAd;

    public RewardedVideoAd mVideoAd;
    public LoadListener mLoadListener;
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    List<MaterialBean> datas = (List<MaterialBean>)msg.obj;
                    if (mLoadListener != null){
                        mLoadListener.onLoadCompleted(datas);
                    }
                    break;

            }
        }
    };
    public void setListener(LoadListener listener){
        mLoadListener = listener;
    }

    public void loadMaterials(final int type){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MaterialBean> list = new ArrayList<MaterialBean>();
                if (type==0){
                    list = VideoLoadUtil.getAllLocalVideos(XiaojiuApplication.getInstace());

                }else if(type == 1){
                    list = PictureLoadUtil.loadAllPictures(XiaojiuApplication.getInstace());
                }else if (type == 2){
                    list = AudioLoadUtil.loadAllAudio(XiaojiuApplication.getInstace());
                }
                Message msg = mHandler.obtainMessage(0);
                msg.obj = list;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

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

    public interface LoadListener{
        public void onLoadCompleted(List<MaterialBean> datas);
    }


}
