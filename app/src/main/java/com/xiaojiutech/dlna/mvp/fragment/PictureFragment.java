package com.xiaojiutech.dlna.mvp.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.xiaojiutech.dlna.R;
import com.xiaojiutech.dlna.bean.MaterialBean;
import com.xiaojiutech.dlna.mvp.activity.ControlActivity;
import com.xiaojiutech.dlna.server.engine.DLNAContainer;
import com.xiaojiutech.dlna.server.service.DLNAService;
import com.xiaojiutech.dlna.utils.Constants;
import com.xiaojiutech.dlna.utils.FileListViewAdapter;
import com.xiaojiutech.dlna.utils.PullListView;


import org.angmarch.views.NiceSpinner;
import org.angmarch.views.NiceSpinnerAdapter;
import org.cybergarage.upnp.Device;

import java.util.ArrayList;
import java.util.List;

public class PictureFragment extends BaseFragment implements View.OnClickListener{

    private List<MaterialBean> mVideoList;
    private StaggeredGridView mListView;
    private FileListViewAdapter mAdapter;
    public AdView mTopBannerAd,mFooterBannerAd;
    private View mHeaderView,mFooterView;
    private TextView mTitle;
    private NiceSpinner mDeviceSpinner;
    private NiceSpinnerAdapter mDeviceAdapter;
    private List<String> mDeviceNames = new ArrayList<String>();
    private List<Device> mDevices = new ArrayList<Device>();
    private List<MaterialBean> mDatas = new ArrayList<MaterialBean>();
    private Button mSearchDeviceBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_picture,container,false);
        mTitle = (TextView)view.findViewById(R.id.title);
        mTitle.setText(getString(R.string.picture));
        mSearchDeviceBtn = (Button)view.findViewById(R.id.search_device_btn);
        updateDeviceList(mDevices);
        mDeviceSpinner = (NiceSpinner) view.findViewById(R.id.device_spinner);
        mDeviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i<=mDevices.size()-1){
                    DLNAContainer.getInstance().setSelectedDevice(mDevices.get(i));
                    Log.i(TAG,"select device : "+mDevices.get(i).getFriendlyName());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        mDeviceNames.add("你鹏哥");
//        mDeviceNames.add("小九哥");
        if (mDeviceNames.size()>0){
            mDeviceSpinner.attachDataSource(mDeviceNames);
        }

        mListView = (StaggeredGridView)view.findViewById(R.id.pullListView);
        mHeaderView = inflater.inflate(R.layout.send_fragment_listview_headerview,null,false);
        mFooterView = inflater.inflate(R.layout.send_fragment_listview_headerview,null,false);
        mTopBannerAd = mHeaderView.findViewById(R.id.top_banner_ad);
        mFooterBannerAd = mFooterView.findViewById(R.id.top_banner_ad);
        mListView.addHeaderView(mHeaderView);
        mListView.addFooterView(mFooterView);
        mLoadListener = new LoadListener() {
            @Override
            public void onLoadCompleted(List<MaterialBean> datas) {
                Log.i(TAG,"LOAD Completed. size = "+datas.size());
                mDatas = datas;
                mAdapter.setDatas(mDatas);
                mAdapter.notifyDataSetChanged();
            }
        };
        return view;
    }

    private void updateDeviceList(List<Device> list){
        mDeviceNames.clear();

        for (Device device : list){
            mDeviceNames.add(device.getFriendlyName());
        }
    }
    private void startDLNAService() {
        Intent intent = new Intent(getActivity(), DLNAService.class);
        getActivity().startService(intent);
    }

    private void stopDLNAService() {
        Intent intent = new Intent(getActivity(), DLNAService.class);
        getActivity().stopService(intent);
    }
    public void showBannerAd(){
        loadBannerAd(new AdListener(){
            @Override
            public void onAdLoaded() {
                Log.i("admob","recv_top_banner_ad loaded");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.i("admob","recv_top_banner_ad onAdFailedToLoad = "+i);
            }
        },mTopBannerAd);
        loadBannerAd(new AdListener(){
            @Override
            public void onAdLoaded() {
                Log.i("admob","recv_top_banner_ad loaded");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.i("admob","recv_top_banner_ad onAdFailedToLoad = "+i);
            }
        },mFooterBannerAd);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new FileListViewAdapter(getActivity(),mDatas,this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    MaterialBean bean = mDatas.get(i-1);
                    Log.i(TAG,"URL = "+bean.getFilePath());
                    String url = Constants.WEB_SERVER_IP+bean.getFilePath();
                    if (bean!=null&& !TextUtils.isEmpty(url) && DLNAContainer.getInstance().getSelectedDevice() != null){
                        play(bean);
                    }else {
                        Toast.makeText(getActivity(),getString(R.string.dlna_nodeivce),Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(),getString(R.string.play_error_tip),Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"onItemClick ERROR : "+e.getMessage());
                }
            }
        });
        mAdapter.notifyDataSetChanged();
        showBannerAd();
        loadMaterials(1);
        startDLNAService();
        DLNAContainer.getInstance().setDeviceChangeListener(
                new DLNAContainer.DeviceChangeListener() {

                    @Override
                    public void onDeviceChange(final Device device) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getActivity(),"onDeviceChange = "+device.getFriendlyName(),Toast.LENGTH_SHORT).show();
                                Log.i(TAG,"onDeviceChange");
                                refreshDevice();
                            }
                        });
                    }
                });
    }
    private void refreshDevice(){
        mDevices = DLNAContainer.getInstance().getDevices();
        updateDeviceList(mDevices);
        if (mDeviceNames.size()>0){
            mDeviceSpinner.attachDataSource(mDeviceNames);
            DLNAContainer.getInstance().setSelectedDevice(mDevices.get(0));
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        refreshDevice();
    }



    @Override
    public void onClick(View view) {

    }
}
