package com.xiaojiutech.dlna.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.xiaojiutech.dlna.bean.MaterialBean;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PictureLoadUtil {

    /**
     * 获取本地所有的图片
     *
     * @return list
     */
    public static List<MaterialBean> loadAllPictures(Context context) {
        List<MaterialBean> list = new ArrayList<>();
        String[] projection = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Video.Media._ID
        };
        //全部图片
        String where = MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=?";
        //指定格式
        String[] whereArgs = {"image/jpeg", "image/png", "image/jpg"};
        //查询
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, where, whereArgs,
                MediaStore.Images.Media.DATE_MODIFIED + " desc ");
        if (cursor == null) {
            return list;
        }
        String[] thumbColumns = {MediaStore.Images.Thumbnails.DATA};
        int fileIndex = 0;
        //遍历
        while (cursor.moveToNext()) {
            MaterialBean materialBean = new MaterialBean();
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            //获取图片的名称
            materialBean.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
            String[] idReplace = new String[]{id+""};
            Cursor thumbCursor = context.getContentResolver().query(
                    MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                    thumbColumns, MediaStore.Images.Thumbnails.IMAGE_ID
                            + "=?", idReplace, null);
            Log.i("picture load","cursor count = "+thumbCursor.getCount());
            if (thumbCursor.moveToFirst()){
                materialBean.setLogo(thumbCursor.getString(thumbCursor
                        .getColumnIndex(MediaStore.Images.Thumbnails.DATA)));
                thumbCursor.close();
            }
//            MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),id, MediaStore.Images.Thumbnails.MINI_KIND,null);
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)); // 大小
            //获取图片的生成日期
            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String path = new String(data, 0, data.length - 1);
            File file = new File(path);
            long time = file.lastModified();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String t = format.format(time);
            materialBean.setTime(t);
            materialBean.setFilePath(path);
            materialBean.setFileSize(size);
            materialBean.setChecked(false);
            materialBean.setFileType(1);
            materialBean.setFileId(fileIndex++);
            materialBean.setUploadedSize(0);
            materialBean.setTimeStamps(System.currentTimeMillis() + "");
            list.add(materialBean);
        }
        cursor.close();
        return list;
    }

}
