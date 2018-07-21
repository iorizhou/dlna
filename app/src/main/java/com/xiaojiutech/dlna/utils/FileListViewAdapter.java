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

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.xiaojiutech.dlna.R;
import com.xiaojiutech.dlna.bean.MaterialBean;


import java.util.List;

public class FileListViewAdapter extends BaseAdapter {
    private List<MaterialBean> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;
    public FileListViewAdapter(Context ctx, List<MaterialBean> datas){
        this.mContext = ctx;
        mInflater = LayoutInflater.from(this.mContext);
        this.mDatas = datas;
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
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)view.getTag();
        }
        MaterialBean bean = (MaterialBean) getItem(i);
        viewHolder.time.setText(bean.getTime());
        viewHolder.name.setText(bean.getTitle());
        Log.i("view","bean.getLogo() = "+bean.getLogo());
        if (TextUtils.isEmpty(bean.getLogo())){
            viewHolder.icon.setImageResource(R.drawable.no_thumbnail);
        }else {
            viewHolder.icon.setImageBitmap(BitmapFactory.decodeFile(bean.getLogo()));
        }
        return view;
    }

    public class ViewHolder{
        public DynamicHeightImageView icon;
        public TextView name;
        public TextView time;
    }
}
