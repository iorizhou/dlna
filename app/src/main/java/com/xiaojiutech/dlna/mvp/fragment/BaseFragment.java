package com.xiaojiutech.dlna.mvp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.xiaojiutech.dlna.R;
import com.xiaojiutech.dlna.XiaojiuApplication;
import com.xiaojiutech.dlna.bean.MaterialBean;
import com.xiaojiutech.dlna.server.engine.DLNAContainer;
import com.xiaojiutech.dlna.server.engine.MultiPointController;
import com.xiaojiutech.dlna.server.inter.IController;
import com.xiaojiutech.dlna.utils.AdmobConstants;
import com.xiaojiutech.dlna.utils.AudioLoadUtil;
import com.xiaojiutech.dlna.utils.Constants;
import com.xiaojiutech.dlna.utils.PictureLoadUtil;
import com.xiaojiutech.dlna.utils.VideoLoadUtil;

import org.cybergarage.upnp.Device;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BaseFragment extends Fragment {
    public static final String TAG = BaseFragment.class.getSimpleName();
    public InterstitialAd mInterstitialAd;

    public RewardedVideoAd mVideoAd;
    public LoadListener mLoadListener;
    public IController mController = new MultiPointController();
    public MaterialBean mMediaBean;
    public Device mDevice;
    public String mCurPlayUrl;
    public void setDevice(){
        mDevice = DLNAContainer.getInstance().getSelectedDevice();
    }
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

    public void play(MaterialBean bean){
        setDevice();
        this.mMediaBean = bean;
        try{
            String name = mMediaBean.getFilePath().substring(mMediaBean.getFilePath().lastIndexOf("/")+1);
            String dir = mMediaBean.getFilePath().substring(0,mMediaBean.getFilePath().lastIndexOf("/")+1);
            mCurPlayUrl = Constants.WEB_SERVER_IP+dir+ URLEncoder.encode(name,"utf-8");
        }catch (Exception e){
            mCurPlayUrl = Constants.WEB_SERVER_IP+mMediaBean.getFilePath();
        }

        if (mController == null || mDevice == null) {
            // usually can't reach here.
            Toast.makeText(getActivity(), getString(R.string.play_error_tip),
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Controller or Device is null, finish this activity");
            return;
        }

        new Thread() {
            public void run() {
                stopPlay();
                final boolean isSuccess = mController.play(mDevice, mCurPlayUrl);
                if (isSuccess) {
                    Log.d(TAG, "play success");
                } else {
                    Log.d(TAG, "play failed..");
                }
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG,"play success and start to get media duration");
                        if (isSuccess) {
                            Log.d(TAG,"play success");
                            Toast.makeText(getActivity(), getString(R.string.play_success_tip),Toast.LENGTH_SHORT).show();
                        }else {
                            Log.d(TAG,"play failed");
                            Toast.makeText(getActivity(), getString(R.string.play_error_tip),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            };
        }.start();
    }

    public void stopPlay(){
        try{
            mController.stop(mDevice);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,"stop play error. "+e.getMessage());
        }
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
