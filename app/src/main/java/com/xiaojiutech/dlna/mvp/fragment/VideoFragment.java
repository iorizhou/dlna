package com.xiaojiutech.dlna.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.xiaojiutech.dlna.utils.Constants;
import com.xiaojiutech.dlna.utils.FileListViewAdapter;
import com.xiaojiutech.dlna.utils.FileOpenIntentUtil;
import com.xiaojiutech.dlna.utils.PullListView;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends BaseFragment implements View.OnClickListener{

    private List<MaterialBean> mVideoList;
    private StaggeredGridView mListView;
    private FileListViewAdapter mAdapter;
    public AdView mTopBannerAd,mFooterBannerAd;
    private View mHeaderView,mFooterView;
    private TextView mTitle;
    private Button mClearDb;
    private List<MaterialBean> mDatas = new ArrayList<MaterialBean>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_picture,container,false);
        mTitle = (TextView)view.findViewById(R.id.title);
        mTitle.setText(getString(R.string.video));
        mClearDb = (Button)view.findViewById(R.id.btn_clear_db);
        mClearDb.setOnClickListener(this);
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
        mAdapter = new FileListViewAdapter(getActivity(),mDatas);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MaterialBean bean = mDatas.get(i);
                String url = Constants.WEB_SERVER_IP+bean.getFilePath();
                Log.i(TAG,"URL = "+url);
            }
        });
        mAdapter.notifyDataSetChanged();
        showBannerAd();
        loadMaterials(0);
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_clear_db:
//                new AlertDialogUtil(getActivity()).showAlertDialog(getString(R.string.app_name), "确定清空所有文件接收记录吗?", "确 定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        mFileDao.deleteInTx(mRecvList);
//                        mRecvList.clear();
//                        mAdapter.setDatas(mRecvList);
//                        mAdapter.notifyDataSetChanged();
//                        Toast.makeText(getActivity(),"已清空所有文件接收记录",Toast.LENGTH_SHORT).show();
//                    }
//                },"取 消",null,true);
                break;
        }
    }
}
