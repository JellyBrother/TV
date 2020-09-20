package com.jelly.tv.base.interfaces;

import android.app.Activity;

/**
 * 对activity进行控制
 */
public interface IApplication {

    void addActivityToStack(Activity var1);

    void removeActivityFromStack(Activity var1);

    void exit();

    void finishAllActivity();
}

