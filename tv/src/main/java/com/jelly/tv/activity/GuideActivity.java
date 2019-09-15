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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jelly.tv.R;
import com.jelly.test.activity.TestMainActivity;

/*
 * MainActivity class that loads MainFragment
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener{
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        findViewById(R.id.jump_demo_main).setOnClickListener(this);
        findViewById(R.id.jump_mycode_test).setOnClickListener(this);
        findViewById(R.id.jump_mycode_work).setOnClickListener(this);
        findViewById(R.id.jump_mycode_work_tv).setOnClickListener(this);

//        ResourceDecoder decoder = new WebpResourceDecoder(this);
//        Glide.get(this).getRegistry().append(InputStream.class, Drawable.class, decoder);

        // webp support
//        ResourceDecoder decoder = new WebpResourceDecoder(this);
//        ResourceDecoder byteDecoder = new WebpBytebufferDecoder(this);
//        GlideApp.get(this).getRegistry()
//                .prepend(InputStream.class, Drawable.class, decoder)
//                .prepend(ByteBuffer.class, Drawable.class, byteDecoder);

        // webp支持
//        ResourceDecoder decoder2 = new WebpResourceDecoder(this);
//        Glide.get(this).getRegistry().append(InputStream.class, Drawable.class, decoder2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jump_mycode_test:
                Intent TestMainIntent = new Intent(this, TestMainActivity.class);
                startActivity(TestMainIntent);
                break;
            case R.id.jump_mycode_work:
                Intent workMainIntent = new Intent(this, WorkMainActivity.class);
                startActivity(workMainIntent);
                break;
            case R.id.jump_mycode_work_tv:
                Intent tvMainIntent = new Intent(this, TVMainActivity.class);
                startActivity(tvMainIntent);
                break;
            case R.id.jump_demo_main:
                Toast.makeText(this,"开发中。。。",Toast.LENGTH_LONG).show();
                break;
        }
    }

//    /**
//     * 语音切换后，即使用户设置了系统字体大小，默认还是app的字体大小
//     * @return
//     */
//    @Override
//    public Resources getResources() {
//        Resources resources = super.getResources();
//        Configuration configuration = new Configuration();
//        configuration.setToDefaults();
//        String flag = SharedPreferencesUtil.getMulitString(this, LanguageInfo.KEY_LANGUAGE);
//
//        if(TextUtils.equals(flag, LanguageInfo.KEY_CHINESE)){
//            configuration.locale = Locale.SIMPLIFIED_CHINESE;
//        }else if(TextUtils.equals(flag,LanguageInfo.KEY_ENGLISH)){
//            configuration.locale = Locale.ENGLISH;
//        }else {
//            configuration.locale = null;
//        }
//        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//        return resources;
//    }
}
