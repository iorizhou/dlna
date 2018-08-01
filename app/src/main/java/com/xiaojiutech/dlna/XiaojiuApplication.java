package com.xiaojiutech.dlna;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.xiaojiutech.dlna.utils.CrashHandler;

import org.cybergarage.upnp.ControlPoint;

import java.io.File;

public class XiaojiuApplication extends  Application{
    private static XiaojiuApplication sInstance;
    public ControlPoint mControlPoint;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
    public DisplayImageOptions mVedioOptions;
    public DisplayImageOptions mPictureOptions;

    public DisplayImageOptions getmPictureOptions() {
        return mPictureOptions;
    }

    public DisplayImageOptions getmAudioOptions() {
        return mAudioOptions;
    }

    public DisplayImageOptions mAudioOptions;
    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
        initVideoOptions();
        initAudioOptions();
        initPictureOptions();
//        CrashHandler.getInstance().init(getApplicationContext());
        sInstance = this;
    }

    public DisplayImageOptions getVideoOptions() {
        return mVedioOptions;
    }


    public static XiaojiuApplication getInstace(){
        return sInstance;
    }

    public void initAudioOptions() {
        mAudioOptions = new DisplayImageOptions.Builder()
                // 设置图片在下载期间显示的图片
                .showImageOnLoading(R.drawable.music_logo)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.music_logo)
                // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.drawable.music_logo)
                // 设置下载的图片是否缓存在内存中
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisc(true)
                // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .considerExifParams(true)
                // 设置图片以如何的编码方式显示
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                // 设置图片的解码类型//
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                // 设置图片的解码配置
                // .decodingOptions(options)
                // .delayBeforeLoading(int delayInMillis)//int
                // delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                // 设置图片在下载前是否重置，复位
                .resetViewBeforeLoading(true)
                // 是否设置为圆角，弧度为多少
                .displayer(new RoundedBitmapDisplayer(20))
                // 是否图片加载好后渐入的动画时间
                .displayer(new FadeInBitmapDisplayer(400))
                // 构建完成
                .build();
    }
    public void initPictureOptions() {
        mPictureOptions = new DisplayImageOptions.Builder()
                // 设置图片在下载期间显示的图片
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.pic_logo)
                // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.drawable.pic_logo)
                // 设置下载的图片是否缓存在内存中
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisc(true)
                // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .considerExifParams(true)
                // 设置图片以如何的编码方式显示
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                // 设置图片的解码类型//
                .bitmapConfig(Bitmap.Config.RGB_565)
                // 设置图片的解码配置
                // .decodingOptions(options)
//                 .delayBeforeLoading(1500)//int
                // delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                // 设置图片在下载前是否重置，复位
                .resetViewBeforeLoading(true)
                // 是否设置为圆角，弧度为多少
                .displayer(new RoundedBitmapDisplayer(20))
                // 是否图片加载好后渐入的动画时间
                .displayer(new FadeInBitmapDisplayer(2000))
                // 构建完成
                .build();
    }

    public void initVideoOptions() {
        mVedioOptions = new DisplayImageOptions.Builder()
                // 设置图片在下载期间显示的图片
                .showImageOnLoading(R.drawable.video_logo)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.video_logo)
                // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.drawable.video_logo)
                // 设置下载的图片是否缓存在内存中
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisc(true)
                // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .considerExifParams(true)
                // 设置图片以如何的编码方式显示
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                // 设置图片的解码类型//
                .bitmapConfig(Bitmap.Config.RGB_565)
                // 设置图片的解码配置
//                 .decodingOptions(options)
//                 .delayBeforeLoading(1500)//int
                // delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                // 设置图片在下载前是否重置，复位
                .resetViewBeforeLoading(true)
                // 是否设置为圆角，弧度为多少
                .displayer(new RoundedBitmapDisplayer(20))
                // 是否图片加载好后渐入的动画时间
                .displayer(new FadeInBitmapDisplayer(400))
                // 构建完成
                .build();
    }
    public void initImageLoader() {
        // 获取默认的路径
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                // 设置内存图片的宽高
                .memoryCacheExtraOptions(480, 800)
                // default = device screen dimensions
                // 缓存到磁盘中的图片宽高
                .diskCacheExtraOptions(480, 800, null)
                // default = device screen dimensions
                // 缓存到磁盘中的图片宽高
                // .taskExecutor(null)
                // .taskExecutorForCachedImages()
                .threadPoolSize(3)
                // default 线程优先级
                .threadPriority(Thread.NORM_PRIORITY - 2)
                // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // // default设置在内存中缓存图像的多种尺寸
                //加载同一URL图片时,imageView从小变大时,从内存缓存中加载
                .denyCacheImageMultipleSizesInMemory()
                // 超过设定的缓存大小时,内存缓存的清除机制
                .memoryCache(new LruMemoryCache(20 * 1024 * 1024))
                // 内存的一个大小
                .memoryCacheSize(8 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                // default 将图片信息缓存到该路径下
                .diskCache(new UnlimitedDiskCache(cacheDir))
                // default 磁盘缓存的大小
                .diskCacheSize(100 * 1024 * 1024)
                // 磁盘缓存文件的个数
                .diskCacheFileCount(100)
                //磁盘缓存的文件名的命名方式//一般使用默认值 (获取文件名称的hashcode然后转换成字符串)或MD5 new Md5FileNameGenerator()源文件的名称同过md5加密后保存
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                // 设置默认的图片加载
                .imageDownloader(
                        new BaseImageDownloader(getApplicationContext())) // default
                // 使用默认的图片解析器
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }




    public void setControlPoint(ControlPoint controlPoint) {
        mControlPoint = controlPoint;
    }


    public ControlPoint getControlPoint() {
        return mControlPoint;
    }
}
