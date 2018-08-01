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

public class AudioLoadUtil {

    /**
     * 获取本地所有的图片
     *
     * @return list
     */
    public static List<MaterialBean> loadAllAudio(Context context,int index,int count) {
        String sort = MediaStore.Audio.Media.DATE_MODIFIED + " desc "+ " limit  " + index + "," + count;
        List<MaterialBean> list = new ArrayList<>();
        String[] projection = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media._ID
        };
        //全部图片
        String where = MediaStore.Audio.Media.MIME_TYPE + "=? or "
                + MediaStore.Audio.Media.MIME_TYPE + "=? or "
                + MediaStore.Audio.Media.MIME_TYPE + "=? or "
                + MediaStore.Audio.Media.MIME_TYPE + "=? or "
                + MediaStore.Audio.Media.MIME_TYPE + "=? or "
                + MediaStore.Audio.Media.MIME_TYPE + "=? or "
                + MediaStore.Audio.Media.MIME_TYPE + "=?";
        //指定格式
        String[] whereArgs = {"audio/mpeg", "audio/midi", "audio/ogg","audio/mp4","audio/wav","audio/x-ms-wma","audio/flac"};
        //查询
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, where, whereArgs,
                sort);
        if (cursor == null) {
            return list;
        }

        int fileIndex = 0;
        //遍历
        while (cursor.moveToNext()) {
            MaterialBean materialBean = new MaterialBean();
            //获取图片的名称
            materialBean.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
            materialBean.setLogo(null);
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)); // 大小
            //获取图片的生成日期
            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            String path = new String(data, 0, data.length - 1);
            File file = new File(path);
            long time = file.lastModified();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String t = format.format(time);
            materialBean.setTime(t);
            materialBean.setFilePath(path);
            Log.i("iorizhou","filepath = "+path);
            materialBean.setFileSize(size);
            materialBean.setChecked(false);
            materialBean.setFileType(2);
            materialBean.setFileId(fileIndex++);
            materialBean.setUploadedSize(0);
            materialBean.setTimeStamps(System.currentTimeMillis() + "");
            list.add(materialBean);
        }
        cursor.close();
        return list;
    }

}
