package com.jelly.tv;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import com.jelly.tv.base.interfaces.IApplication;
import com.jelly.tv.base.log.AppCrashHandler;
import com.jelly.tv.base.log.LogTools;
import com.jelly.tv.base.log.ThreadTask;
import com.db.DbAdapterFactory;
import com.db.control.DbManagerImpl;
import com.db.util.LogUtil;

import java.util.LinkedList;
import java.util.List;

public class TVApplication extends Application implements IApplication {
    public static final String TAG = "ICApplication";
    private static TVApplication application;
    //当前环境变量
    public String envModel = "";
    //个人cv变量
    private String cvEnv = "";
    /**
     * 当前是否在更新
     */
    private boolean isUpdate = false;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        setIcaptainEnv();//根据配置文件判断当前是什么服务环境
        initDB();//初始化DB
        copyH5File();//复制h5的zip文件，然后解压
    }

    private void setIcaptainEnv() {
//        envModel = BuildConfig.EnvMode;//根据配置文件判断当前是什么服务环境
        LogTools.getInstance().init(this);
        if(TextUtils.equals("pro",envModel)){
            //日志打印控制台开关(发布生产要关闭.printConsoleOff；开启.printConsoleOn)
            LogTools.getInstance().printConsoleOff();
            //日志写入文件开关(发布生产要关闭.printFileOff；开启.printFileOn)
            LogTools.getInstance().printFileOff();
            //开启程序的Crash功能,设置该CrashHandler为程序的默认处理器
            AppCrashHandler.getInstance().init(getApplicationContext());
            //控制所有数据库日志打印，以“db_log”开头,默认是true
            LogUtil.isPrint = false;
        }else {
            //日志打印控制台开关(发布生产要关闭.printConsoleOff；开启.printConsoleOn)
            LogTools.getInstance().printConsoleOn();
            //日志写入文件开关(发布生产要关闭.printFileOff；开启.printFileOn)
            LogTools.getInstance().printConsoleOn();
            //开启程序的Crash功能,设置该CrashHandler为程序的默认处理器
            AppCrashHandler.getInstance().init(getApplicationContext());
            //控制所有数据库日志打印，以“db_log”开头,默认是true
            LogUtil.isPrint = true;
        }
    }

    public boolean initDB() {
        // 初始化db
        DbManagerImpl.app = application;
        return DbAdapterFactory.checkAadpter(application, false);
    }

    private void copyH5File() {
        ThreadTask.getInstance().executorNetThread(new Runnable() {

            @Override
            public void run() {
                try {
//                    FileUitls.copyH5FileInData(application,cvEnv);
                } catch (Exception e) {
                    LogTools.getInstance().e(TVApplication.TAG, e.toString());
                }
            }
        }, ThreadTask.ThreadPeriod.PERIOD_HIGHT);
    }

    public static TVApplication getInstance() {
        return application;
    }

    public String getEnvMode() {
        return envModel;
    }

    public boolean isUpdatestatus() {
        return isUpdate;
    }

    public void setIsUpdatestatus(boolean isUpdatestatus) {
        this.isUpdate = isUpdatestatus;
    }

    private List<Activity> activities = new LinkedList();

    @Override
    public void addActivityToStack(Activity activity) {
        activities.add(activity);
    }

    @Override
    public void removeActivityFromStack(Activity activity) {
        activities.remove(activity);
    }

    @Override
    public void exit() {
        finishAllActivity();
        System.exit(0);
    }

    @Override
    public void finishAllActivity() {
        for (int i = 0; i < this.activities.size(); ++i) {
            Activity activity = (Activity) this.activities.get(i);
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        this.activities.clear();
    }
}
