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

import com.jelly.tv.activity.BaseActivity;
import com.jelly.tv.R;
import com.jelly.test.weight.GifView;

/*
 * MainActivity class that loads MainFragment
 */
public class GifActivity extends BaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gif);

        GifView gif1 = (GifView) findViewById(R.id.gif1);
        // 设置背景gif图片资源
        gif1.setMovieResource(R.raw.test);
        GifView gif2 = (GifView) findViewById(R.id.gif2);
        gif2.setMovieResource(R.raw.aa);
        // 设置暂停
        // gif2.setPaused(true);
//        oneshot设置成true就只会循环一次，设置成false会循环多次
    }
    /**
     * 现在有两个网络请求，规定五秒请求完，跳转新界面
     * 1、定义网络请求完的标记A、B
     * 2、当网络请求成功，将标记设为true
     * 3、定义一个五秒执行的handler，在内部判断：如果A、B都是true就不走跳转
     * 4、在两个请求成功里面各自判断对方值是否true，是true就跳转；
     */


    /**
     * 检测版本更新
     */
//    public void checkVersion() {
//        Message msg = null;
//        long startTime = System.currentTimeMillis();//起始的时间
//        try {
//            //1.从服务获取版本信息
//            URL url = new URL(getResources().getString(R.string.versionurl));
//            //打开链接
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//            con.setConnectTimeout(5000);//5秒超时时间
//            int responseCode = con.getResponseCode();
//            if (responseCode == 200) {
//                //成功 把io流转json成字符串
//                StringBuilder mess = readStream(con);
//                //json数据解析成对象
//                versionMessage = parseJson(mess + "");
//                //判断版本是否一致
//                if (mCurrentVersionCode == versionMessage.versionCode) {
//                    //版本一致
//                    msg = mHandler.obtainMessage(LOADMAIN);
//                } else {
//                    //版本不一致
//                    msg = mHandler.obtainMessage(NEWVERSION);
//                }
//            } else {
//                // 默认的初始化
//                msg = mHandler.obtainMessage(ERROR);
//                msg.arg1 = 10092;//url链接错误
//            }
//        } catch (MalformedURLException e) {
//            // Error
//            msg = mHandler.obtainMessage(ERROR);
//            msg.arg1 = 10090;//url链接错误
//            e.printStackTrace();
//        } catch (IOException e) {
//            // Error
//            msg = mHandler.obtainMessage(ERROR);
//            msg.arg1 = 10088;//文件找不到
//            e.printStackTrace();
//        } catch (JSONException e) {
//            // Error
//            msg = mHandler.obtainMessage(ERROR);
//            msg.arg1 = 10086;//json格式错误
//            e.printStackTrace();
//        } finally{
//            //统一发消息
//            long endTime = System.currentTimeMillis();
//            if (endTime - startTime < mDuration) {
//                //代码执行的时间小于动画的时间，让动画走完
//                SystemClock.sleep(mDuration - (endTime - startTime)) ;
//            }
//            //发送消息
//            mHandler.sendMessage(msg);
//        }
//        //2. 比较版本
//        //3. 如果有新版本 提示用户下载更新
//        //1. 下载
//        //2. 取消下载
//        //4. 没有进入主界面
//    }
}
