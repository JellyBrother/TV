//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jelly.tv.base.log;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressLint({"SimpleDateFormat"})
public class LogTools {
    private static final String DEFAULT_LOG_FILE_DIR_NAME = "MyTv";
    private static final String TAG = LogTools.class.getSimpleName();
    public static final String CALLER = "_caller";
    public static final String DATE_TIME = "_date_time";
    public static final String UUID = "uuid";
    public static final String APP_ID = "app_id";
    private static LogTools instance = new LogTools();
    private static HandlerThread sLogThread = new HandlerThread("LOG");
    private static Handler sLogHandler;
    protected Boolean writeToFileSwitch = Boolean.valueOf(true);
    protected Boolean printConsoleSwitch = Boolean.valueOf(true);
    private LogTools.DebugLevel mDebugLevel;
    private String mBigDataDirName;
    private String mCrashDirName;
    private String mSystemLogDirName;
    protected Context mContext;
    private File mBigDataDir;
    private File mCrashDir;
    private File mSystemDir;
    private File mBigDataFile;
    private File mCrashFile;
    private File mSystemFile;
    private String mAppId="com.china.jelly.mytv";
    private String mCurrentDate;
    private String mPackageName;
    private Map<String, Object> mInitBigDataParams;

    protected LogTools() {
        this.mDebugLevel = LogTools.DebugLevel.VERBOSE;
        this.mBigDataDirName = DEFAULT_LOG_FILE_DIR_NAME + File.separator + "default" + File.separator + "BigDataLog";
        this.mCrashDirName = DEFAULT_LOG_FILE_DIR_NAME + File.separator + "default" + File.separator + "CrashLog";
        this.mSystemLogDirName = DEFAULT_LOG_FILE_DIR_NAME + File.separator + "default" + File.separator + "SystemLog";
        this.mBigDataDir = null;
        this.mCrashDir = null;
        this.mSystemDir = null;
        this.mBigDataFile = null;
        this.mCrashFile = null;
        this.mSystemFile = null;
    }

    public static LogTools getInstance() {
        return instance;
    }

    public synchronized void init(Context context) {
        if(this.mContext == null) {
            this.mContext = context.getApplicationContext();
            String packageName = this.mContext.getPackageName();
            this.mCrashDirName = this.makeDirectoryByPackage(packageName, "CrashLog");
            this.mSystemLogDirName = this.makeDirectoryByPackage(packageName, "SystemLog");
            this.getCrashLogDir();
            this.getSystemLogDir();
        }
    }

    public synchronized void setupBigData(Context context, Map<String, Object> params) {
        if(this.mContext == null || TextUtils.isEmpty(this.mPackageName) || params == null) {
            this.mContext = context.getApplicationContext();
            this.mPackageName = this.mContext.getPackageName();
            if(null != params && !params.isEmpty()) {
                this.mInitBigDataParams = params;
            } else {
                this.mInitBigDataParams = new HashMap();
            }

            if(!this.mInitBigDataParams.containsKey("app_id")) {
                this.mInitBigDataParams.put("app_id", this.mContext.getPackageName());
            }

            this.mBigDataDirName = this.makeDirectoryByPackage(this.mPackageName, "BigDataLog");
            this.getBigDataLogDir();
            this.getBigDataLogFile();
        }
    }

    public synchronized File getBigDataLogDir() {
        if(this.mBigDataDir == null) {
            this.mBigDataDir = this.makeStorageDir(this.mBigDataDirName);
        }

        if(!this.mBigDataDir.exists() && !this.mBigDataDir.mkdirs()) {
            Log.e(TAG, "Log folder create failed:" + this.mBigDataDir.getAbsolutePath());
            return null;
        } else {
            return this.mBigDataDir;
        }
    }

    public synchronized File getCrashLogDir() {
        if(this.mCrashDir == null) {
            this.mCrashDir = this.makeStorageDir(this.mCrashDirName);
        }

        if(!this.mCrashDir.exists() && !this.mCrashDir.mkdirs()) {
            Log.e(TAG, "Crash folder create failed:" + this.mCrashDir.getAbsolutePath());
            return null;
        } else {
            return this.mCrashDir;
        }
    }

    public synchronized File getSystemLogDir() {
        if(this.mSystemDir == null) {
            this.mSystemDir = this.makeStorageDir(this.mSystemLogDirName);
        }

        if(!this.mSystemDir.exists() && !this.mSystemDir.mkdirs()) {
            Log.e(TAG, "SystemLog folder create failed:" + this.mSystemDir.getAbsolutePath());
            return null;
        } else {
            return this.mSystemDir;
        }
    }

    public File seekDaySystemLogFile(Date date) {
        File dir = this.getSystemLogDir();
        if(dir == null) {
            return dir;
        } else {
            String fileName = this.getFormativeDateStr(date, "yyyyMMdd") + ".log";
            return new File(dir, fileName);
        }
    }

    public synchronized void printFileOn() {
        if(!this.writeToFileSwitch.booleanValue()) {
            this.writeToFileSwitch = Boolean.valueOf(true);
        }

    }

    public synchronized void printFileOff() {
        if(this.writeToFileSwitch.booleanValue()) {
            this.writeToFileSwitch = Boolean.valueOf(false);
        }

    }

    public synchronized void printConsoleOn() {
        if(!this.printConsoleSwitch.booleanValue()) {
            this.printConsoleSwitch = Boolean.valueOf(true);
        }

    }

    public synchronized void printConsoleOff() {
        if(this.printConsoleSwitch.booleanValue()) {
            this.printConsoleSwitch = Boolean.valueOf(false);
        }

    }

    public LogTools.DebugLevel getDebugLevel() {
        return this.mDebugLevel;
    }

    public synchronized void setDebugLevel(LogTools.DebugLevel pDebugLevel) {
        if(pDebugLevel == null) {
            throw new IllegalArgumentException("pDebugLevel must not be null!");
        } else {
            this.mDebugLevel = pDebugLevel;
        }
    }

    public void v(String pTag, String pMessage) {
        this.v(pTag, pMessage, (Throwable)null);
    }

    public void v(String pTag, String pMessage, Throwable pThrowable) {
        this.v(pTag, pMessage, (String)null, pThrowable);
    }

    public void v(String pTag, String pMessage, String traceId, Throwable pThrowable) {
        if(this.mDebugLevel.isSameOrLessThan(LogTools.DebugLevel.VERBOSE)) {
            String logContent = LogLibUtils.dealNull(pMessage);
            if(this.printConsoleSwitch.booleanValue()) {
                if(pThrowable == null) {
                    Log.v(pTag, logContent);
                } else {
                    Log.v(pTag, logContent, pThrowable);
                }
            }

            if(pThrowable != null) {
                StringBuilder builder = (new StringBuilder(logContent)).append(" ").append(Log.getStackTraceString(pThrowable));
                logContent = builder.toString();
            }

            this.writeLogToFile(LogType.SYSTEM, "v", pTag, logContent, traceId);
        }
    }

    public void d(String pTag, String pMessage) {
        this.d(pTag, pMessage, (Throwable)null);
    }

    public void d(String pTag, String pMessage, Throwable pThrowable) {
        this.d(pTag, pMessage, (String)null, pThrowable);
    }

    public void d(String pTag, String pMessage, String traceId, Throwable pThrowable) {
        if(this.mDebugLevel.isSameOrLessThan(LogTools.DebugLevel.DEBUG)) {
            String logContent = LogLibUtils.dealNull(pMessage);
            if(this.printConsoleSwitch.booleanValue()) {
                if(pThrowable == null) {
                    Log.d(pTag, logContent);
                } else {
                    Log.d(pTag, logContent, pThrowable);
                }
            }

            if(pThrowable != null) {
                StringBuilder builder = (new StringBuilder(logContent)).append(" ").append(Log.getStackTraceString(pThrowable));
                logContent = builder.toString();
            }

            this.writeLogToFile(LogType.SYSTEM, "d", pTag, logContent, traceId);
        }

    }

    public void logBigData(Map<String, Object> params) {
        File logFile = null;
        if(null != params && params.containsKey("_caller")) {
            Object object = params.remove("_caller");
            if(null != object) {
                String caller = object.toString();
                logFile = this.getBigDataLogFile(this.getBigDataLogDir(caller), caller);
            } else {
                logFile = this.getBigDataLogFile();
            }
        } else {
            logFile = this.getBigDataLogFile();
        }

        this.logBigData(logFile, params);
    }

    public void i(String pTag, String pMessage) {
        this.i(pTag, pMessage, (Throwable)null);
    }

    public void i(String pTag, String pMessage, Throwable pThrowable) {
        this.i(pTag, pMessage, (String)null, pThrowable);
    }

    public void i(String pTag, String pMessage, String traceId, Throwable pThrowable) {
        if(this.mDebugLevel.isSameOrLessThan(LogTools.DebugLevel.INFO)) {
            String logContent = LogLibUtils.dealNull(pMessage);
            if(this.printConsoleSwitch.booleanValue()) {
                if(pThrowable == null) {
                    Log.i(pTag, logContent);
                } else {
                    Log.i(pTag, logContent, pThrowable);
                }
            }

            if(pThrowable != null) {
                StringBuilder builder = (new StringBuilder(logContent)).append(" ").append(Log.getStackTraceString(pThrowable));
                logContent = builder.toString();
            }

            this.writeLogToFile(LogType.SYSTEM, "i", pTag, logContent, traceId);
        }

    }

    public void w(String pTag, String pMessage) {
        this.w(pTag, pMessage, (Throwable)null);
    }

    public void w(String pTag, String pMessage, Throwable pThrowable) {
        this.w(pTag, pMessage, (String)null, pThrowable);
    }

    public void w(String pTag, String pMessage, String traceId, Throwable pThrowable) {
        if(this.mDebugLevel.isSameOrLessThan(LogTools.DebugLevel.WARNING)) {
            String logContent = LogLibUtils.dealNull(pMessage);
            if(this.printConsoleSwitch.booleanValue()) {
                if(pThrowable == null) {
                    Log.w(pTag, logContent);
                } else {
                    Log.w(pTag, logContent, pThrowable);
                }
            }

            if(pThrowable != null) {
                StringBuilder builder = (new StringBuilder(logContent)).append(" ").append(Log.getStackTraceString(pThrowable));
                logContent = builder.toString();
            }

            this.writeLogToFile(LogType.SYSTEM, "w", pTag, logContent, traceId);
        }

    }

    public void e(String pTag, String pMessage) {
        this.e(pTag, pMessage, (Throwable)null);
    }

    public void e(String pTag, String pMessage, Throwable pThrowable) {
        this.e(pTag, pMessage, (String)null, pThrowable);
    }

    public void e(String pTag, String pMessage, String traceId, Throwable pThrowable) {
        if(this.mDebugLevel.isSameOrLessThan(LogTools.DebugLevel.ERROR)) {
            String logContent = LogLibUtils.dealNull(pMessage);
            if(this.printConsoleSwitch.booleanValue()) {
                if(pThrowable == null) {
                    Log.e(pTag, logContent);
                } else {
                    Log.e(pTag, logContent, pThrowable);
                }
            }

            if(pThrowable != null) {
                StringBuilder builder = (new StringBuilder(logContent)).append(" ").append(Log.getStackTraceString(pThrowable));
                logContent = builder.toString();
            }

            this.writeLogToFile(LogType.SYSTEM, "e", pTag, logContent, traceId);
        }

    }

    public void p(String pTag, String pMessage) {
        this.p(pTag, pMessage, (Throwable)null);
    }

    public void p(String pTag, String pMessage, Throwable pThrowable) {
        this.p(pTag, pMessage, (String)null, pThrowable);
    }

    public void p(String pTag, String pMessage, String traceId, Throwable pThrowable) {
        if(this.mDebugLevel.isSameOrLessThan(LogTools.DebugLevel.PROCESS)) {
            String logContent = LogLibUtils.dealNull(pMessage);
            if(this.printConsoleSwitch.booleanValue()) {
                if(pThrowable == null) {
                    Log.i(pTag, logContent);
                } else {
                    Log.i(pTag, logContent, pThrowable);
                }
            }

            if(pThrowable != null) {
                StringBuilder builder = (new StringBuilder(logContent)).append(" ").append(Log.getStackTraceString(pThrowable));
                logContent = builder.toString();
            }

            this.writeLogToFile(LogType.SYSTEM, "p", pTag, logContent, traceId);
        }

    }

    public void printCrashLog(String pTag, String pMessage, String traceId, Throwable pThrowable) {
        String logContent = LogLibUtils.dealNull(pMessage);
        if(this.printConsoleSwitch.booleanValue()) {
            if(pThrowable == null) {
                Log.e(pTag, logContent);
            } else {
                Log.e(pTag, logContent, pThrowable);
            }
        }

        if(pThrowable != null) {
            logContent = pMessage + "\n" + Log.getStackTraceString(pThrowable);
        }

        this.writeLogToFile(LogType.CRASH, "e", pTag, logContent, traceId);
    }

    protected File makeStorageDir(String dirName) {
        File baseFolder = null;
        if(LogLibUtils.isExistSDCard()) {
            baseFolder = Environment.getExternalStorageDirectory();
        }

        if(null == baseFolder) {
            baseFolder = this.mContext.getExternalFilesDir("Documents");
            if(baseFolder == null) {
                baseFolder = this.mContext.getExternalCacheDir();
            }
        }

        if(null == baseFolder) {
            baseFolder = this.mContext.getCacheDir();
        }

        return new File(baseFolder, dirName);
    }

    private synchronized void writeBigDataToFile(String text, File logFile) {
        if(logFile != null && logFile.exists()) {
            LogTools.LogTask operationLogTask = new LogTools.LogTask(logFile, LogType.BIGDATA_STATISTICAL_OPERATION, "BigData", "BigData", text);
            sLogHandler.post(operationLogTask);
        }

    }

    protected synchronized void writeLogToFile(LogType logType, String logLevel, String tag, String text, String traceId) {
        if(this.writeToFileSwitch.booleanValue()) {
            if(checkPermission("android.permission.WRITE_EXTERNAL_STORAGE") && checkPermission("android.permission.READ_EXTERNAL_STORAGE")) {
                this.excuteWriteLogTask(logType, logLevel, tag, text, traceId);
            }

        }
    }

    public boolean checkPermission(String permission) {
        try {
            return mContext.getPackageManager().checkPermission(permission, mContext.getPackageName()) == 0;
        } catch (Error var3) {
            return mContext.getPackageManager().checkPermission(permission, mContext.getPackageName()) == 0;
        } catch (Exception var4) {
            return mContext.getPackageManager().checkPermission(permission, mContext.getPackageName()) == 0;
        }
    }

    private void excuteWriteLogTask(LogType logType, String logLevel, String tag, String text, String traceId) {
        if(!LogLibUtils.isAvailableSpace(104857600L) && this.mContext != null) {
            File systemLogFile = this.getSystemLogFile();
            if(LogType.CRASH == logType) {
                File systemLogTask = this.getCrashLogFile();
                LogTools.LogTask systemLogTask1;
                if(systemLogTask != null) {
                    this.createFile(systemLogTask);
                    systemLogTask1 = new LogTools.LogTask(systemLogTask, logType, logLevel, tag, text);
                    sLogHandler.post(systemLogTask1);
                }

                if(systemLogFile != null) {
                    this.createFile(systemLogFile);
                    systemLogTask1 = new LogTools.LogTask(systemLogFile, logType, logLevel, tag, text);
                    sLogHandler.post(systemLogTask1);
                }
            } else if(LogType.SYSTEM == logType && systemLogFile != null) {
                this.createFile(systemLogFile);
                LogTools.LogTask systemLogTask2 = new LogTools.LogTask(systemLogFile, logType, logLevel, tag, text);
                sLogHandler.post(systemLogTask2);
            }
        } else if(this.mContext == null) {
            Log.w(TAG, "context is null.please first init");
        } else {
            Log.w(TAG, "sd space isn\'t available");
        }

    }

    private void createFile(File systemLogFile) {
        if(!systemLogFile.exists()) {
            boolean createFolderSuccess = (new File(systemLogFile.getParent())).mkdirs();
            if(createFolderSuccess) {
                try {
                    boolean e = systemLogFile.createNewFile();
                    if(!e) {
                        this.e(TAG, "create file failed");
                    }
                } catch (IOException var4) {
                    this.d(TAG, "create file failed");
                }
            }
        }

    }

    protected synchronized void writeLogToFile(File logFile, String text) {
        if(this.writeToFileSwitch.booleanValue()) {
            if(!LogLibUtils.isAvailableSpace(104857600L) && this.mContext != null) {
                LogTools.LogTask logTask = new LogTools.LogTask(logFile, LogType.SIMPLE_TEXT, (String)null, (String)null, text);
                sLogHandler.post(logTask);
            }

        }
    }

    String getFormativeDateStr(Date date, String formatString) throws IllegalArgumentException, NullPointerException {
        return (new SimpleDateFormat(formatString)).format(date);
    }

    private void deleteOutDateLog(File dir) {
        if(dir.exists()) {
            File[] files = dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".log") || name.endsWith(".bak");
                }
            });
            File[] var3 = files;
            int var4 = files.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File file = var3[var5];

                try {
                    Date e = LogLibUtils.getDateBefore();
                    String logDateStr = file.getName().replace(".bak", "");
                    logDateStr = logDateStr.replace(".log", "").substring(0, 8);
                    if(!TextUtils.isEmpty(this.mPackageName)) {
                        logDateStr = logDateStr.replace(this.mPackageName, "");
                    }

                    Date logCalendar = LogLibUtils.fileName2Date(logDateStr);
                    if(logCalendar.before(e)) {
                        file.delete();
                    }
                } catch (ParseException var10) {
                    file.delete();
                }
            }

        }
    }

    private File openBigDataLogFile(File dir, String needWriteFileName, boolean isCreate) {
        try {
            File e = new File(dir, needWriteFileName);
            File fileDir = e.getParentFile();
            if(fileDir != null) {
                if(!fileDir.exists()) {
                    fileDir.mkdirs();
                }

                if(!e.exists() && isCreate) {
                    e.createNewFile();
                    if(this.mInitBigDataParams != null) {
                        this.logBigData(e, this.mInitBigDataParams);
                    }

                    this.deleteOutDateLog(dir);
                } else {
                    if(e.exists()) {
                        return e;
                    }

                    Log.e(TAG, "LogTools must be called setupBigData.");
                }
            }
        } catch (Exception var6) {
            Log.e(TAG, "", var6);
        }

        return null;
    }

    private File openOrCreateLogFile(final File dir, String needWriteFileName) {
        try {
            File e = new File(dir, needWriteFileName);
            File fileDir = e.getParentFile();
            if(null != fileDir) {
                if(!fileDir.exists()) {
                    fileDir.mkdirs();
                }

                if(!e.exists()) {
                    boolean isSuccess = e.createNewFile();
                    (new Thread(new Runnable() {
                        public void run() {
                            LogTools.this.deleteOutDateLog(dir);
                        }
                    })).start();
                }

                return e;
            }
        } catch (Exception var6) {
            Log.e(TAG, "", var6);
        }

        return null;
    }

    private synchronized File getCrashLogFile() {
        String needWriteFileName = this.getFormativeDateStr(new Date(), "yyyyMMddHHmmssSSS") + ".log";
        this.mCrashFile = this.openOrCreateLogFile(this.mCrashDir, needWriteFileName);
        return this.mCrashFile;
    }

    private synchronized File getBigDataLogFile() {
        if(null == this.mBigDataFile || !this.mBigDataFile.exists() || !LogLibUtils.isToday(this.mCurrentDate)) {
            this.mCurrentDate = LogLibUtils.getCurrentDateStr();
            if(TextUtils.isEmpty(this.mPackageName) || this.mInitBigDataParams == null || this.mInitBigDataParams.isEmpty()) {
                Log.e(TAG, "LogTools must be called setupBigData.");
                this.mBigDataFile = null;
                return this.mBigDataFile;
            }

            String needWriteFileName = this.mPackageName + this.getFormativeDateStr(new Date(), "yyyyMMdd") + ".log";
            this.mBigDataFile = this.openBigDataLogFile(this.mBigDataDir, needWriteFileName, true);
        }

        return this.mBigDataFile;
    }

    private synchronized File getBigDataLogFile(File file, String packageName) {
        String needWriteFileName = packageName + this.getFormativeDateStr(new Date(), "yyyyMMdd") + ".log";
        return this.openBigDataLogFile(file, needWriteFileName, false);
    }

    private synchronized File getBigDataLogDir(String dirName) {
        if(!TextUtils.isEmpty(dirName)) {
            File bigDataDir = this.makeStorageDir(this.makeDirectoryByPackage(dirName, "BigDataLog"));
            if(!bigDataDir.exists() && !bigDataDir.mkdirs()) {
                Log.e(TAG, "Log folder create failed:" + bigDataDir.getAbsolutePath());
                return null;
            } else {
                return bigDataDir;
            }
        } else {
            return null;
        }
    }

    private String makeDirectoryByPackage(String packageName, String subDir) {
        return DEFAULT_LOG_FILE_DIR_NAME + File.separator + packageName + File.separator + subDir;
    }

    private synchronized File getSystemLogFile() {
        if(null == this.mSystemFile || !LogLibUtils.isToday(this.mCurrentDate)) {
            this.mCurrentDate = LogLibUtils.getCurrentDateStr();
            String needWriteFileName = this.getFormativeDateStr(new Date(), "yyyyMMdd") + ".log";
            this.mSystemFile = this.openOrCreateLogFile(this.mSystemDir, needWriteFileName);
        }

        return this.mSystemFile;
    }

    private void logBigData(File logFile, Map<String, Object> params) {
        String logContent = LogLibUtils.map2String(params);
        this.writeBigDataToFile(logContent, logFile);
    }

    static {
        sLogThread.start();
        sLogHandler = new Handler(sLogThread.getLooper());
    }

    protected class LogTask implements Runnable {
        private LogType mLogType;
        private File mLogFile;
        private String mThreadId;
        private String mLogTag;
        private String mLogMessage;
        private String mLogLevel;
        private Date mNowTime;

        public LogTask(File logFile, LogType type, String logLevel, String logTag, String logMessage) {
            this.mLogType = type;
            this.mLogLevel = logLevel;
            this.mLogTag = logTag;
            this.mLogMessage = logMessage;
            this.mThreadId = String.valueOf(Thread.currentThread().getId());
            this.mNowTime = new Date();
            this.mLogFile = logFile;
        }

        public void run() {
            String needWriteMessage = this.formatMessage();

            try {
                this.writeLog(this.mLogFile, needWriteMessage);
            } catch (Exception var3) {
                Log.e(LogTools.TAG, "", var3);
            }

        }

        private String formatMessage() {
            String formatDate = LogTools.this.getFormativeDateStr(this.mNowTime, "HHmmss.SSS");
            String needWriteMessage;
            switch(mLogType.ordinal()) {
                case 1:
                    needWriteMessage = this.buildCrashMessage(formatDate);
                    break;
                case 2:
                    needWriteMessage = this.buildSystemMessage(formatDate);
                    break;
                case 3:
                    needWriteMessage = this.mLogMessage;
                    break;
                default:
                    needWriteMessage = this.mLogMessage;
            }

            return needWriteMessage;
        }

        private String buildCrashMessage(String currentDate) {
            StringBuffer buffer = new StringBuffer();
            return buffer.append(this.mLogLevel).append(" ").append(currentDate).append(" ").append("tid").append(this.mThreadId).append(" ").append("appId").append(LogTools.this.mAppId).append(" ").append(this.mLogTag).append(" ").append(this.mLogMessage).toString();
        }

        private String buildSystemMessage(String currentDate) {
            StringBuffer buffer = new StringBuffer();
            return buffer.append(this.mLogLevel).append(" ").append(currentDate).append(" ").append("tid").append(this.mThreadId).append(" ").append(this.mLogTag).append(" ").append(this.mLogMessage).toString();
        }

        private void writeLog(File file, String needWriteMessage) {
            FileWriter filerWriter = null;
            BufferedWriter bufWriter = null;

            try {
                filerWriter = new FileWriter(file, true);
                bufWriter = new BufferedWriter(filerWriter);
                bufWriter.write(needWriteMessage);
                bufWriter.newLine();
                bufWriter.flush();
                filerWriter.flush();
            } catch (Exception var9) {
                Log.e(LogTools.TAG, "", var9);
            } finally {
                closeSilently(filerWriter);
                closeSilently(bufWriter);
            }

        }
    }

    public void closeSilently(Closeable c) {
        if(c != null) {
            try {
                c.close();
            } catch (Exception var2) {
                Log.e(TAG, var2.getMessage(), var2);
            }
        }

    }

    public static enum DebugLevel implements Comparable<LogTools.DebugLevel> {
        NONE,
        ERROR,
        PROCESS,
        WARNING,
        INFO,
        DEBUG,
        VERBOSE;

        public static final LogTools.DebugLevel ALL;

        private DebugLevel() {
        }

        public boolean isSameOrLessThan(LogTools.DebugLevel pDebugLevel) {
            return this.compareTo(pDebugLevel) >= 0;
        }

        static {
            ALL = VERBOSE;
        }
    }
}
