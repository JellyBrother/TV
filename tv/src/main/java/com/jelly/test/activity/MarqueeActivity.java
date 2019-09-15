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

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.jelly.tv.activity.BaseActivity;
import com.jelly.tv.R;
import com.jelly.test.weight.MarqueeView;
import com.jelly.test.weight.MarqueeView2;
import com.jelly.test.weight.MarqueeView3;

/*
 * MainActivity class that loads MainFragment
 */
public class MarqueeActivity extends BaseActivity {
    private String[] strings = {"跑马灯测试", "赤橙黄绿青蓝紫", "猜",
            "这是一串很长很长很长很长的数字11233333666699999"};
    private MarqueeView3 marqueeView3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_marquee);

        MarqueeView marqueeView = (MarqueeView) findViewById(R.id.tv_marquee);
        marqueeView.setFocusable(true);
        marqueeView.requestFocus();
        marqueeView.setText("我使劲跑");//设置文本
        marqueeView.startScroll(); //开始

        MarqueeView2 marqueeView2 = (MarqueeView2) findViewById(R.id.tv_marquee2);

        marqueeView3 = (MarqueeView3) findViewById(R.id.tv_marquee3);
        marqueeView3.setBackgroundColor(Color.RED);
        marqueeView3.setText(strings[(int) Math.round(Math.random() * 10)
                % strings.length]);

//        LinearLayout.LayoutParams lp = (LayoutParams) marqueeView3.getLayoutParams();
//        lp.width = 800;
//        lp.height = 50;
//        marqueeView.setLayoutParams(lp);

//        LinearLayout.LayoutParams lp = (LayoutParams) marqueeView3.getLayoutParams();
//        lp.width = 1000;
//        lp.height = 200;
//        marqueeView.setLayoutParams(lp);
    }

    public void setLayout(int i) {
        LinearLayout.LayoutParams lp = (LayoutParams) marqueeView3.getLayoutParams();
        lp.width = 600;
        lp.height = 200;
        lp.leftMargin = 200 + i * 5;
        lp.topMargin = 10 + i * 30;
        marqueeView3.setLayoutParams(lp);
    }
}
