package com.jelly.tv.base.log;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * 缓冲目录类
 *
 *         1.	目的
 *         在项目开发过程中，为了有效方便管理项目中产生的文件，进行了项目中本地文件目录存放结构。
 *         2.	适用范围
 *         适用于公司采用Android编程语言开发的所有软件项目。
 *         3.	若没有SDCrad的情况下
 *         若没有SDCrad存在的情况下，项目中需要的本地文件保存目录为/data/data/com..xx(包名)/cache/
 *         /data/data/com..xx(包名)/cache/hw_log   日志文件保存目录
 *         /data/data/com..xx(包名)/cache/hw_apk  相关APK文件保存目录
 *         /data/data/com..xx(包名)/cache/hw_cache 缓存文件保存目录
 *         /data/data/com..xx(包名)/cache/hw_pic   图片文件保存目录
 *         /data/data/com..xx(包名)/cache/hw_video  视频文件保存目录
 *         /data/data/com..xx(包名)/cache/hw_music  音频文件保存目录
 *         /data/data/com..xx(包名)/cache/hw_temp  临时文件保存目录
 *         /data/data/com..xx(包名)/cache/hw_download  下载文件保存目录
 *         /data/data/com..xx(包名)/cache/hw_http  http请求缓冲目录
 *         /data/data/com..xx(包名)/cache/hw_update  app自身更新的下载目录
 *         /data/data/com..xx(包名)/cache/hw_exception  app异常闪退日志目录目录
 *         4.	若存在SDCard的情况
 *         若SDCrad存在的情况，项目中需要的本地文件保存目录为
 */
public final class CacheDirectory {
    private CacheDirectory() {
    }

    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    /**
     * 判断是否存在外部存储设备
     *
     * @return 如果不存在返回false
     */
    public static boolean hasExternalStorage() {
        Boolean externalStorage = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        return externalStorage;
    }

    /**
     * 判断是否有权限操作外部存储卡
     *
     * @param context 上下文
     * @return 如果没有权限返回false
     */
    public static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 创建并返回指定名称的缓冲目录路径
     *
     * @param context   上下文
     * @param cacheName 缓冲目录名称
     * @param IT  是否为应用业务类日志(true对用户可见的目录  false对用户不可见)
     * @return 缓冲目录的路径
     */
    public static String getCacheDir(Context context, String cacheName, boolean IT) {
        String dir = null;
        boolean flag = false;
        //判断是否有sdkcard,sdcard读写权限
        if (hasExternalStorage() && hasExternalStoragePermission(context)) {
            Log.d(CacheDirectory.class.getSimpleName(), "加载sdcard文件：" + cacheName);
            String cacheDir = "";
            if (IT) {
                cacheDir = "//" + cacheName;
            } else {
                cacheDir = "/System/" + cacheName;
            }
            File file = new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
            flag = true;
            if (!file.exists()) {
                flag = file.mkdirs();
                Log.d(CacheDirectory.class.getSimpleName(), "创建sdcard文件：" + cacheName + ":" + flag);
            }
            dir = file.getAbsolutePath();
        }

        if (!flag) {
            File file = new File(context.getCacheDir(), cacheName);
            Log.d(CacheDirectory.class.getSimpleName(), "加载缓冲文件：" + cacheName);
            if (!file.exists()) {
                boolean b = file.mkdirs();
                Log.d(CacheDirectory.class.getSimpleName(), "创建缓冲文件：" + cacheName + ":" + b);
            }
            dir = file.getAbsolutePath();

        }
        return dir;
    }

    public static String getH5CacheDir(Context context, String cacheName) {
        String dir = null;
        File file = new File(context.getCacheDir(), cacheName);
        Log.d(CacheDirectory.class.getSimpleName(), "加载缓冲文件：" + cacheName);
        if (!file.exists()) {
            boolean b = file.mkdirs();
            Log.d(CacheDirectory.class.getSimpleName(), "创建缓冲文件：" + cacheName + ":" + b);
        }
        dir = file.getAbsolutePath();
        return dir;
    }

    /**
     * 获得下载文件的目录
     *
     * @return
     */
    public static String getDownLoadDir(Context context) {
        return getCacheDir(context, "_download", true);
    }

    /**
     * 获得下载H5文件的目录
     *
     * @return
     */
    public static String getDownLoadH5Zip(Context context) {
        return getH5CacheDir(context, "._download_H5");
    }

    /**
     * 获得日志的目录
     *
     * @return
     */
    public static String getLogDir(Context context) {
        return getCacheDir(context, "_log", false);
    }

    /**
     * 获得apk目录
     *
     * @return
     */
    public static String getApkDir(Context context) {
        return getCacheDir(context, "_apk", true);
    }


    /**
     * 获得图像目录
     *
     * @return
     */
    public static String getPicDir(Context context) {
        return getCacheDir(context, "_pic", true);
    }

    /**
     * 获得视频目录
     *
     * @return
     */
    public static String getVideoDir(Context context) {
        return getCacheDir(context, "_video", true);
    }

    /**
     * 获得音乐目录
     *
     * @return
     */
    public static String getMusicDir(Context context) {
        return getCacheDir(context, "_music", true);
    }

    /**
     * 获得临时文件目录
     *
     * @return
     */
    public static String getTempDir(Context context) {
        return getCacheDir(context, "_temp", true);
    }

    /**
     * 获得WebView文件目录
     *
     * @return
     */
    public static String getWebViewDir(Context context) {
        return getCacheDir(context, "_web", true);
    }

    /**
     * 获得更新下载目录
     *
     * @param context
     * @return
     */
    public static String getUpdateDir(Context context) {
        return getCacheDir(context, "_update", true);
    }

    /**
     * 获得闪退日志目录
     *
     * @param context
     * @return
     */
    public static String getExceptionDir(Context context) {
        return getCacheDir(context, "_exception", false);
    }
}
