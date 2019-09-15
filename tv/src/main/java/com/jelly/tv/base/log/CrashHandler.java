//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jelly.tv.base.log;

import android.util.Log;

import java.lang.Thread.UncaughtExceptionHandler;

public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = CrashHandler.class.getSimpleName();
    private UncaughtExceptionHandler mDefaultExceptionHandler;
    private static CrashHandler mCrashHandler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if(mCrashHandler == null) {
            mCrashHandler = new CrashHandler();
        }

        return mCrashHandler;
    }

    public synchronized void init() {
        Log.e("initCrashHandler", "init");
        this.mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable throwable) {
        if(this.mDefaultExceptionHandler != null) {
            LogTools.getInstance().printCrashLog(TAG, throwable.getMessage(), "", throwable);
            this.mDefaultExceptionHandler.uncaughtException(thread, throwable);
        }

    }
}
