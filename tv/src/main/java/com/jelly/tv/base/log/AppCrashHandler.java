package com.jelly.tv.base.log;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;

import com.jelly.tv.TVApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author tWX333516 on 2016/7/23
 */
public final class AppCrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "Exception";
    public String crashDir = "";
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static AppCrashHandler instance = new AppCrashHandler();
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期,作为日志文件名的一部分
    @SuppressLint("SimpleDateFormat")
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");

    /**
     * 保证只有一个CrashHandler实例
     */
    private AppCrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static AppCrashHandler getInstance() {
        return instance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        crashDir = CacheDirectory.getExceptionDir(mContext);
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理

            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LogTools.getInstance().e(TAG, e.toString());
            }
            //清空acitivty
            TVApplication.getInstance().finishAllActivity();//解决循环依赖
            //重新启动应用
            AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis(), GuideActivity.intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }

    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        LogTools.getInstance().e(TAG, getExceptionInfo(ex));
        //收集设备参数信息
        collectDeviceInfo(mContext);

        saveCrashInfo2File(ex);
        //if (ICApplication.isSendCarshToServer) {
        //    //发送Crash日志
        //    sendCrashToServer(getExceptionInfo(ex));
        //} else {
        //    //保存本地Crash日志
        //}
        return true;
    }

//    //发送异常信息到服务端
//    private void sendCrashToServer(String exceptionInfo) {
//        StringBuffer deviceInfo = new StringBuffer();
//        for (Map.Entry<String, String> entry : infos.entrySet()) {
//            String key = entry.getKey();
//            if (!TextUtils.equals("CPU_ABI", key) && !TextUtils.equals("CPU_ABI2", key)
//                    && !TextUtils.equals("DISPLAY", key) && !TextUtils.equals("FINGERPRINT", key)
//                    && !TextUtils.equals("UNKNOWN", key) && !TextUtils.equals("BOOTLOADER", key)
//                    && !TextUtils.equals("IS_DEBUGGABLE", key)) {
//                String value = entry.getValue();
//
//                deviceInfo.append(key + "=" + value + ";");
//            }
//        }
//        //FrameHttpServices.sendCrashInfoToServer(mContext, 0, null, exceptionInfo, "", deviceInfo.toString());
//    }

    //收集设备参数信息
    private void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            LogTools.getInstance().e(TAG, "an error occured when collect package info", e);
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);

                infos.put(field.getName(), field.get(null).toString());

                //LogTools.getInstance().d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                LogTools.getInstance().e(TAG, "an error occured when collect crash info", e);
            }
        }
        infos.put("SDK_INT", String.valueOf(Build.VERSION.SDK_INT));
        infos.put("RELEASE", Build.VERSION.RELEASE);
    }

    //收集异常信息
    private String getExceptionInfo(Throwable ex) {
        PrintWriter printWriter = null;
        try {
            Writer writer = new StringWriter();
            printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            String result = writer.toString();
            return result;
        } catch (Exception e) {
            LogTools.getInstance().e("getExceptionInfo", ex.toString());
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
        return "";
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */

    private synchronized String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        StringBuffer deviceInfo = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            deviceInfo.append(key + "=" + value + ";\n");
        }
        sb.append(deviceInfo);
        String result = writer.toString();
        sb.append(result);
        FileOutputStream fos = null;
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "_" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(crashDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                fos = new FileOutputStream(crashDir + File.separator + fileName);
                fos.write(sb.toString().getBytes("utf-8"));
            }
            return fileName;
        } catch (Exception e) {
            LogTools.getInstance().e("AppCrashHandler", e.toString());
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LogTools.getInstance().e("saveCrashInfo2File", ex.toString());
                }
            }
        }
        return null;
    }
}
