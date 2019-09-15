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

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jelly.tv.activity.BaseActivity;
import com.jelly.tv.R;
import com.jelly.test.utils.LanguageInfo;
import com.jelly.test.utils.SharedPreferencesUtil;
import com.jelly.test.weight.AutoTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * MainActivity class that loads MainFragment
 */
public class SpinnerActivity extends BaseActivity implements View.OnClickListener{
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private AutoTextView mTextView02;
    private static int sCount = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_spinner);

        spinner = (Spinner) findViewById(R.id.spinner);
        final TextView show = (TextView) findViewById(R.id.tvw_show);
        mTextView02 = (AutoTextView) findViewById(R.id.switcher02);
        mTextView02.setText("Hello world!");
        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.prev).setOnClickListener(this);

        long time=System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 EEEE");
        Log.e("time","time1="+format.format(date));
        format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.e("time","time2="+format.format(date));
        format=new SimpleDateFormat("yyyy/MM/dd");
        Log.e("time","time3="+format.format(date));
        format=new SimpleDateFormat("HH:mm:ss");
        Log.e("time","time4="+format.format(date));
        format=new SimpleDateFormat("EEEE");
        Log.e("time","time5="+format.format(date));
        format=new SimpleDateFormat("E");
        Log.e("time","time6="+format.format(date));

        //数据
        final ArrayList data_list = new ArrayList<String>();
        String flag = SharedPreferencesUtil.getMulitString(this, LanguageInfo.KEY_LANGUAGE);
        if(TextUtils.equals(flag, LanguageInfo.KEY_CHINESE)){
            data_list.clear();
            data_list.add(getResources().getString(R.string.btn_china));
            data_list.add(getResources().getString(R.string.btn_english));
        }else if(TextUtils.equals(flag,LanguageInfo.KEY_ENGLISH)){
            data_list.clear();
            data_list.add(getResources().getString(R.string.btn_english));
            data_list.add(getResources().getString(R.string.btn_china));
        }else {
            data_list.clear();
            data_list.add(getResources().getString(R.string.btn_english));
            data_list.add(getResources().getString(R.string.btn_china));
        }
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,data_list);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                show.setText("你选择的是："+data_list.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                mTextView02.next();
                sCount++;
                break;
            case R.id.prev:
                mTextView02.previous();
                sCount--;
                break;
        }
        mTextView02.setText(sCount%2==0 ?
                sCount+"AAFirstAA" :
                sCount+"BBBBBBB");
        System.out.println("getH: ["+mTextView02.getHeight()+"]");
    }

    class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTextView02.setText(sCount%2==0 ?
                    sCount+"AAFirstAA" :
                    sCount+"BBBBBBB");
        }
    }
}
