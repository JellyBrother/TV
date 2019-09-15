//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jelly.tv.base.log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

class LogLibUtils {
    private static final String TAG = LogLibUtils.class.getSimpleName();
    private static final StatFs STAT_FS = new StatFs(Environment.getExternalStorageDirectory().getPath());

    public LogLibUtils() {
    }

    public static final String getCurrentDateStr() {
        return (new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH)).format(new Date());
    }

    public static final boolean isToday(String date) {
        return !TextUtils.isEmpty(date)?getCurrentDateStr().equals(date):false;
    }

    public static final Date fileName2Date(String fileName) throws ParseException {
        return (new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH)).parse(fileName);
    }

    public static Date getDateBefore() {
        Calendar now = Calendar.getInstance();
        now.set(5, now.get(5) - 7);
        now.set(10, 0);
        now.set(12, 0);
        now.set(13, 0);
        return now.getTime();
    }

    public static final String map2String(Map<String, Object> params) {
        StringBuilder builder = new StringBuilder();
        if(params != null) {
            Object data;
            if(params.containsKey("_date_time")) {
                data = params.remove("_date_time");
                if(data != null && data instanceof Long) {
                    builder.append(formatDateTime(((Long)data).longValue()));
                } else {
                    builder.append(getCurrentDateTimeStr());
                }
            } else {
                builder.append(getCurrentDateTimeStr());
            }

            Iterator var3 = params.keySet().iterator();

            while(var3.hasNext()) {
                String key = (String)var3.next();
                data = params.get(key);
                builder.append(" ").append(key).append("=").append(data != null?data.toString():"");
            }
        } else {
            builder.append(getCurrentDateTimeStr());
        }

        return builder.toString();
    }

    public static String getVersion(Context context) {
        try {
            PackageManager e = context.getPackageManager();
            PackageInfo info = e.getPackageInfo(context.getPackageName(), 16384);
            return info.versionName;
        } catch (Exception var3) {
            Log.e(TAG, "", var3);
            return "";
        }
    }

    public static String generateAppInfo(Context context) {
        StringBuilder appBuild = new StringBuilder();
        appBuild.append(context.getPackageName()).append("(").append(getVersion(context)).append(")");
        return appBuild.toString();
    }

    public static boolean isExistSDCard() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static long getAvailableSize() {
        long availableSize;
        if(VERSION.SDK_INT >= 18) {
            availableSize = STAT_FS.getBlockSizeLong() * STAT_FS.getAvailableBlocksLong();
        } else {
            long blockSize = (long)STAT_FS.getBlockSize();
            long availableBlocks = (long)STAT_FS.getAvailableBlocks();
            availableSize = blockSize * availableBlocks;
        }

        return availableSize;
    }

    public static boolean isAvailableSpace(long bytes) {
        return getAvailableSize() <= bytes;
    }

    public static String dealNull(String message) {
        return message == null?"null":message;
    }

    private static final String getCurrentDateTimeStr() {
        return (new SimpleDateFormat("yyyyMMddHHmmss.SSS", Locale.ENGLISH)).format(new Date());
    }

    private static final String formatDateTime(long time) {
        return (new SimpleDateFormat("yyyyMMddHHmmss.SSS", Locale.ENGLISH)).format(new Date(time));
    }

    private static String getProductModel() {
        return Build.MODEL;
    }

    private static String getSysReleaseVersion() {
        return "Android-" + VERSION.RELEASE;
    }
}
