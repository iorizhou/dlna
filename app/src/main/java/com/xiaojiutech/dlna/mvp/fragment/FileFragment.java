package com.xiaojiutech.dlna.mvp.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.lfilepickerlibrary.adapter.PathAdapter;
import com.leon.lfilepickerlibrary.filter.LFileFilter;
import com.leon.lfilepickerlibrary.ui.LFilePickerActivity;
import com.leon.lfilepickerlibrary.utils.FileUtils;
import com.leon.lfilepickerlibrary.widget.EmptyRecyclerView;
import com.xiaojiutech.dlna.R;
import com.xiaojiutech.dlna.bean.MaterialBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileFragment extends BaseFragment {
    private EmptyRecyclerView mRecylerView;
    private View mEmptyView;
    private TextView mTvPath;
    private TextView mTvBack;
    private String mPath;
    private List<File> mListFiles;
    private ArrayList<String> mListNumbers = new ArrayList<String>();//存放选中条目的数据地址
    private PathAdapter mPathAdapter;
    private LFileFilter mFilter;
    private boolean mIsAllSelected = false;
    private String[] mFileFilterTypes = new String[]{"png","jpg","jpeg","gif","bmp","tiff","exif","webp","mp3","wav","wma","flac","ogg","ape","aac","mp4","avi","mkv","rm","rmvb","3gp","flv"};
    private Menu mMenu;
    private Boolean mInterceptBack = true;
    private HashMap<String,LFilePickerActivity.PositionBean> mPosMap = new HashMap<String,LFilePickerActivity.PositionBean>();

    public boolean isInterceptBack(){
        return mInterceptBack;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_file,container,false);
        mRecylerView = (EmptyRecyclerView) view.findViewById(com.leon.lfilepickerlibrary.R.id.recylerview);
        mTvPath = (TextView) view.findViewById(com.leon.lfilepickerlibrary.R.id.tv_path);
        mTvBack = (TextView) view.findViewById(com.leon.lfilepickerlibrary.R.id.tv_back);
        mEmptyView = view.findViewById(com.leon.lfilepickerlibrary.R.id.empty_view);
        return view;
    }
    private boolean checkSDState() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mTvPath.setText(mPath);
        mFilter = new LFileFilter(mFileFilterTypes);
        mListFiles = FileUtils.getFileList(mPath, mFilter, false, 0);
        mPathAdapter = new PathAdapter(mListFiles, getActivity(), mFilter, false, false, 0);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecylerView.setAdapter(mPathAdapter);
        mRecylerView.setmEmptyView(mEmptyView);

        initListener();
    }

    @Override
    public void onBackPressed() {
        mTvBack.performClick();
    }

    private void setShowPath(String path) {
        mTvPath.setText(path);
    }
    private void initListener() {
        // 返回目录上一级
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mInterceptBack){
                    Log.i(TAG,"!mInterceptBack . so tvback pressed directly return");
                    return;
                }

                String tempPath = new File(mPath).getParent();
                if (tempPath == null) {
                    mInterceptBack = false;
                    Log.e(TAG,"tempPath is null");
                    return;
                }
                mPath = tempPath;
                mInterceptBack = true;
                mListFiles = FileUtils.getFileList(mPath, mFilter, false, 0);
                mPathAdapter.setmListData(mListFiles);
                mPathAdapter.updateAllSelelcted(false);
                mIsAllSelected = false;

                setShowPath(mPath);
                if(mRecylerView.getLayoutManager() != null && mPosMap.get(mPath)!=null) {
                    ((LinearLayoutManager) mRecylerView.getLayoutManager()).scrollToPositionWithOffset(mPosMap.get(mPath).lastPosition, mPosMap.get(mPath).lastOffset);
                }
            }
        });


        mPathAdapter.setOnItemClickListener(new PathAdapter.OnItemClickListener() {
            @Override
            public void click(int position) {
                //单选模式直接返回
                if (mListFiles.get(position).isDirectory()) {
                    chekInDirectory(position);
                    return;
                }
                MaterialBean bean = new MaterialBean();
                bean.setFilePath(mListFiles.get(position).getAbsolutePath());
                play(bean);
            }
        });
    }

    private void chekInDirectory(int position) {
        mPath = mListFiles.get(position).getAbsolutePath();
        setShowPath(mPath);
        //更新数据源
        mListFiles = FileUtils.getFileList(mPath, mFilter, false, 0);
        mPathAdapter.setmListData(mListFiles);
        mPathAdapter.notifyDataSetChanged();
        mRecylerView.scrollToPosition(0);
        mInterceptBack = true;
    }
}
