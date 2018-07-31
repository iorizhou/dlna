package com.xiaojiutech.dlna.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaojiutech.dlna.R;
import com.xiaojiutech.dlna.XiaojiuApplication;
import com.xiaojiutech.dlna.bean.MaterialBean;
import com.xiaojiutech.dlna.mvp.fragment.BaseFragment;
import com.xiaojiutech.dlna.server.engine.DLNAContainer;


import java.util.List;

public class FileListViewAdapter extends BaseAdapter {
    private List<MaterialBean> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private BaseFragment mFragment;
    public FileListViewAdapter(Context ctx, List<MaterialBean> datas,BaseFragment fragment){
        this.mContext = ctx;
        mInflater = LayoutInflater.from(this.mContext);
        this.mDatas = datas;
        mImageLoader = ImageLoader.getInstance();
        this.mFragment = fragment;
    }

    public void setDatas(List<MaterialBean> data){
        this.mDatas = data;
    }

    @Override
    public int getCount() {
        return this.mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_file,null);
            viewHolder.icon = (DynamicHeightImageView) view.findViewById(R.id.icon);
            viewHolder.name = (TextView)view.findViewById(R.id.name);
            viewHolder.time = (TextView)view.findViewById(R.id.time);
            viewHolder.mPlay = (ImageView)view.findViewById(R.id.play);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)view.getTag();
        }
        final MaterialBean bean = (MaterialBean) getItem(i);
        viewHolder.time.setText(bean.getTime());
        viewHolder.name.setText(bean.getTitle());
        viewHolder.mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Constants.WEB_SERVER_IP+bean.getFilePath();
                if (bean!=null&& !TextUtils.isEmpty(url) && DLNAContainer.getInstance().getSelectedDevice() != null){
                    mFragment.play(bean);
                }else {
                    Toast.makeText(mContext,mContext.getString(R.string.dlna_nodeivce),Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.i("view","bean.getLogo() = "+bean.getLogo());
        if (bean.getFileType()==0){
            if (TextUtils.isEmpty(bean.getLogo())){
                mImageLoader.displayImage("drawable://"+R.drawable.video_logo,viewHolder.icon,XiaojiuApplication.getInstace().getVideoOptions());
            }else {
                mImageLoader.displayImage("file://"+bean.getLogo(),viewHolder.icon,XiaojiuApplication.getInstace().getVideoOptions());
            }
        }else if (bean.getFileType() == 2){
            mImageLoader.displayImage("drawable://"+R.drawable.music_logo,viewHolder.icon,XiaojiuApplication.getInstace().getmAudioOptions());
        }else {
            mImageLoader.displayImage("file://"+bean.getFilePath(),viewHolder.icon,XiaojiuApplication.getInstace().getmPictureOptions());
        }

//        if (TextUtils.isEmpty(bean.getLogo())){
//            mImageLoader.displayImage("drawable://"+R.drawable.no_thumbnail,viewHolder.icon,XiaojiuApplication.getInstace().getmImageLoaderOptions());
//        }else {
//            mImageLoader.displayImage("file://"+bean.getLogo(),viewHolder.icon,XiaojiuApplication.getInstace().getmImageLoaderOptions());
//        }
        return view;
    }

    public class ViewHolder{
        public DynamicHeightImageView icon;
        public TextView name;
        public TextView time;
        public ImageView mPlay;
    }
}
