/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.jelly.tv.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.jelly.test.utils.LanguageInfo;
import com.jelly.test.utils.SharedPreferencesUtil;

import java.util.Locale;

/*
 * MainActivity class that loads MainFragment
 */
public class BaseActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 语音切换后，即使用户设置了系统字体大小，默认还是app的字体大小
     * @return
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        String flag = SharedPreferencesUtil.getMulitString(this, LanguageInfo.KEY_LANGUAGE);

        if(TextUtils.equals(flag, LanguageInfo.KEY_CHINESE)){
            configuration.locale = Locale.SIMPLIFIED_CHINESE;
        }else if(TextUtils.equals(flag,LanguageInfo.KEY_ENGLISH)){
            configuration.locale = Locale.ENGLISH;
        }else {
            configuration.locale = Locale.ENGLISH;
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }
}
