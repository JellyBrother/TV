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

package com.jelly.test.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;

import com.jelly.tv.activity.BaseActivity;
import com.jelly.tv.activity.GuideActivity;
import com.jelly.tv.R;
import com.jelly.test.example.GlideMainActivity;
import com.jelly.test.utils.LanguageInfo;
import com.jelly.test.utils.SharedPreferencesUtil;

import java.util.Locale;

/*
 * MainActivity class that loads MainFragment
 */
public class TestMainActivity extends BaseActivity implements View.OnClickListener{
    private String flag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);
        findViewById(R.id.btn_draw_text).setOnClickListener(this);
        findViewById(R.id.btn_china).setOnClickListener(this);
        findViewById(R.id.btn_english).setOnClickListener(this);
        findViewById(R.id.btn_spinner).setOnClickListener(this);
        findViewById(R.id.btn_gif).setOnClickListener(this);
        findViewById(R.id.btn_Marquee).setOnClickListener(this);
        findViewById(R.id.btn_biaoqian).setOnClickListener(this);
        findViewById(R.id.btn_glide).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_draw_text:
                Intent drawTextIntent = new Intent(this,DrawTextActivity.class);
                startActivity(drawTextIntent);
                break;
            case R.id.btn_china:
                flag = LanguageInfo.KEY_CHINESE;
                changeLanguage();
                break;
            case R.id.btn_english:
                flag = LanguageInfo.KEY_ENGLISH;
                changeLanguage();
                break;
            case R.id.btn_spinner:
                Intent spinnerIntent = new Intent(this,SpinnerActivity.class);
                startActivity(spinnerIntent);
                break;
            case R.id.btn_gif:
                Intent gifIntent = new Intent(this,GifActivity.class);
                startActivity(gifIntent);
                break;
            case R.id.btn_Marquee:
                Intent marqueeIntent = new Intent(this,MarqueeActivity.class);
                startActivity(marqueeIntent);
                break;
            case R.id.btn_biaoqian:
                Intent biaoqianIntent = new Intent(this,LableActivity.class);
                startActivity(biaoqianIntent);
                break;
            case R.id.btn_glide:
                Intent glideIntent = new Intent(this,GlideMainActivity.class);
                startActivity(glideIntent);
                break;
        }
    }

    /**
     * 改变app语言
     */
    private void changeLanguage() {
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if(TextUtils.equals(flag, LanguageInfo.KEY_CHINESE)){
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }else if(TextUtils.equals(flag,LanguageInfo.KEY_ENGLISH)){
            config.locale = Locale.ENGLISH;
        }
        SharedPreferencesUtil.putMulitString(this, LanguageInfo.KEY_LANGUAGE, flag);
        resources.updateConfiguration(config, dm);
        Intent intent = new Intent(this,GuideActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
